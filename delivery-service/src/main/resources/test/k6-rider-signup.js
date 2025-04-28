import http from 'k6/http';

export default function () {
  for (let i = 500; i < 1000; i++) {
    const payload = JSON.stringify({
      username: `rider${i}`,
      name: 'test',
      password: 'test',
      role: 'RIDER',
      slackId: 'test',
    });

    const res = http.post('http://localhost:8080/api/users/join', payload, {
      headers: {'Content-Type': 'application/json'},
    });

    console.log(`User ${i} signup status: ${res.status}`);
  }
}
