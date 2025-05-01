import ws from 'k6/ws';
import http from 'k6/http';
import {check, sleep} from 'k6';

export const options = {
  setupTimeout: '180s',
  vus: 1000,
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
    {duration: "5m", target: 1000},   // 1분 동안 500명 유지
    {duration: "30s", target: 0},    // 10초 동안 0명으로 Ramp-Down
  ],
};

const BASE_URL = 'http://localhost:8080';
const LOGIN_ENDPOINT = '/auth/login';
const WEBSOCKET_URL = 'ws://localhost:8080/ws/delivery/consumer';

export function setup() {
  const tokens = [];

  for (let i = 0; i < 1000; i++) {
    const loginPayload = JSON.stringify({
      username: `consumer${i}`,
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
      console.error(`consumer${i} 로그인 실패: 토큰 없음`);
    } else {
      console.log(`consumer${i} 로그인 성공`);
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

  const riderId = __VU;
  const url = `${WEBSOCKET_URL}?token=${token}`;

  const res = ws.connect(url, {}, function (socket) {
    console.log(`클라이언트 ${__VU} WebSocket 연결됨`);

    socket.on('open', () => {
      // 서버에 riderId 등록
      socket.send(JSON.stringify({riderId}));

      socket.setTimeout(() => {
        console.log(`클라이언트 ${__VU} WebSocket 닫기`);
        socket.close();
      }, 300000); // 3분 유지
    });

    socket.on('message', (msg) => {
      const data = JSON.parse(msg);
      console.log(`클라이언트 ${__VU} 위치 수신:`, data);
    });

    socket.on('close', () => console.log(`클라이언트 ${__VU} 연결 종료`));
    socket.on('error', (e) => console.error(`클라이언트 ${__VU} 에러:`, e.error()));
  });

  check(res, {'클라이언트 WebSocket 연결 성공': (r) => r && r.status === 101});

  sleep(1); // 계속 유지되도록
}
