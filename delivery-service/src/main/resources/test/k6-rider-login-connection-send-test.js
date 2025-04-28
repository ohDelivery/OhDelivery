import http from 'k6/http';
import ws from 'k6/ws';
import {check, sleep} from 'k6';

export let options = {
  vus: 10,          // 동시 사용자 수
  duration: '1m',    // 테스트 지속 시간
};

export default function () {
  // 1. 현재 VU 번호 기반 유저명 설정
  const vu = __VU;
  const username = `rider${vu - 1}`; // VU는 1부터 시작하므로 -1

  // 2. 로그인 요청
  const loginRes = http.post('http://localhost:8080/auth/login',
      JSON.stringify({
        username: username,
        password: 'test',
      }), {
        headers: {'Content-Type': 'application/json'},
      });

  check(loginRes, {
    '로그인 성공': (res) => res.status === 200,
  });

  const token = loginRes.json('data').accessToken;

  // 3. WebSocket 연결 (query param 방식)
  const url = `ws://localhost:8080/ws/delivery/rider?token=${token}`;
  const params = {tags: {user: username}};

  const res = ws.connect(url, params, function (socket) {
    socket.on('open', function () {
      console.log(`[${username}] WebSocket 연결 완료`);

      const message = {
        longitude: 126.9780,
        latitude: 37.5665,
        timestamp: Date.now()
      };

      // 메시지 전송
      socket.send(JSON.stringify(message))

      socket.on('message', function (msg) {
        console.log(`[${username}] 메시지 수신: ${msg}`);
      });

      // 5초 후 종료
      socket.setTimeout(function () {
        console.log(`[${username}] WebSocket 종료`);
        socket.close();
      }, 5000);
    });

    socket.on('error', (e) => {
      console.error(`[${username}] WebSocket 오류: ${e.error()}`);
    });

    socket.on('close', () => {
      console.log(`[${username}] WebSocket 닫힘`);
    });
  });

  check(res, {
    'WebSocket 101 상태': (r) => r && r.status === 101,
  });

  sleep(1); // 조금 여유를 줌
}
