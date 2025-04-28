import {check, sleep} from 'k6';
import ws from 'k6/ws';
import http from 'k6/http';

export const options = {
  setupTimeout: '180s',
  vus: 1000, // 100명 동시
  // duration: '1m', // 1분 동안
  stages: [
    {duration: "3s", target: 100},  // 10초 동안 100명 Ramp-Up
    {duration: "3s", target: 200},  // 10초 동안 200명 Ramp-Up
    {duration: "3s", target: 300},  // 10초 동안 300명 Ramp-Up
    {duration: "3s", target: 400},  // 10초 동안 400명 Ramp-Up
    {duration: "3s", target: 500},  // 10초 동안 500명 Ramp-Up
    {duration: "3s", target: 600},  // 10초 동안 500명 Ramp-Up
    {duration: "3s", target: 700},  // 10초 동안 500명 Ramp-Up
    {duration: "3s", target: 800},  // 10초 동안 500명 Ramp-Up
    {duration: "3s", target: 900},  // 10초 동안 500명 Ramp-Up
    {duration: "3s", target: 1000},  // 10초 동안 500명 Ramp-Up
    {duration: "3m", target: 1000},   // 1분 동안 500명 유지
    {duration: "30s", target: 0},    // 10초 동안 0명으로 Ramp-Down
  ],
};

const BASE_URL = 'http://localhost:8080';
const LOGIN_ENDPOINT = '/auth/login';
const WEBSOCKET_URL = 'ws://localhost:8080/ws/delivery/rider';

export function setup() {
  const tokens = [];

  for (let i = 0; i < 1000; i++) {
    const loginPayload = JSON.stringify({
      username: `rider${i}`,
      password: 'test',
    });

    const loginParams = {
      headers: {'Content-Type': 'application/json'},
    };

    const loginRes = http.post(`${BASE_URL}${LOGIN_ENDPOINT}`, loginPayload,
        loginParams);

    check(loginRes, {
      '로그인 성공': (res) => res.status === 200,
    });

    const token = loginRes.json('data').accessToken;
    if (!token) {
      console.error(`rider${i} 로그인 실패: 토큰 없음`);
    } else {
      console.log(`rider${i} 로그인 성공`);
      tokens.push(token);
    }
  }

  return {tokens};
}

export default function (data) {
  const token = data.tokens[__VU - 1]; // VU 번호에 맞는 토큰 사용
  if (!token) {
    console.error(`VU ${__VU} 토큰 없음`);
    return;
  }

  const url = `${WEBSOCKET_URL}?token=${token}`;

  const params = {
    headers: {Authorization: `Bearer ${token}`}
  };

  const res = ws.connect(url, params, function (socket) {
    console.log(`VU ${__VU} WebSocket 연결됨`);

    // 초기 위치 (서울 시청 좌표 기준)
    let longitude = 126.9780;
    let latitude = 37.5665;

    socket.on('open', function () {
      socket.setInterval(function () {
        // 위치를 무작위로 약간 이동
        longitude += (Math.random() - 0.5) * 0.001; // 이동폭 증가
        latitude += (Math.random() - 0.5) * 0.001;

        const payload = {
          longitude: parseFloat(longitude.toFixed(6)), // 소수점 6자리
          latitude: parseFloat(latitude.toFixed(6)),
          timestamp: Date.now(),
        };

        socket.send(JSON.stringify(payload));
        console.log(`VU ${__VU} 위치 전송:`, payload);
      }, 1000);

      socket.setTimeout(function () {
        console.log(`VU ${__VU} WebSocket 닫기`);
        socket.close();
      }, 120000); // 1분 후 닫기
    });

    socket.on('close', () => console.log(`VU ${__VU} WebSocket 연결 종료`));
    socket.on('error', (e) => console.error(`VU ${__VU} 에러:`, e.error()));
  });

  check(res, {'WebSocket 연결 101 상태': (r) => r && r.status === 101});

  sleep(1); // 1초 쉬어주기
}
