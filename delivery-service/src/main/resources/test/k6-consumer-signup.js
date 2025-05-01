import http from 'k6/http';

export default function () {
  for (let i = 0; i < 1000; i++) {
    const payload = JSON.stringify({
      username: `consumer${i}`,
      name: 'test',
      password: 'test',
      role: 'CONSUMER',
      slackId: 'test',
    });

    const res = http.post('http://localhost:8080/api/users/join', payload, {
      headers: {'Content-Type': 'application/json'},
    });

    console.log(`User ${i} signup status: ${res.status}`);
  }
}
