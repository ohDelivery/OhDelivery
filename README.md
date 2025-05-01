# 리드미

---

# 🚚 오배송 (Oh Delivery)

**오배송**은 배달 라이더를 위한 **주문 매칭, 실시간 알림, 인센티브 지급 및 상담 지원 서비스**입니다.

멀티모듈 아키텍처를 기반으로 대규모 트래픽을 고려한 **모니터링 및 실시간 처리 구조**를 설계하고 구현하였습니다.

---

## 🚀 서비스 주요 기능

### 🛵 라이더 매칭

- 배달 이벤트 발생 시, 가게 위치 기준으로 인근 라이더를 탐색하여 매칭 이벤트 발행
- 매칭 이벤트 발생 시, 라이더들에게 알림 전송 (Slack, WebSocket)
- 알림을 받은 라이더들 중 **선착순 응답자**와 배달 매칭
→ **Redisson 분산락**을 활용해 동시 응답 충돌 방지

### 📦 배달 경로 및 위치 추적

- Naver Open API를 활용하여 주소 기반 **위도/경도 정보 조회**, 최단 경로 제공
- **Redis GEO**와 **WebSocket**을 활용한 라이더 **실시간 위치 추적**

### 💰 라이더 인센티브

- 배달 수행에 따른 라이더 **배달료 및 인센티브 자동 책정**
- **Kafka Streams**를 활용하여 인센티브 정산을 위한 통계 데이터 처리

### 💬 상담 및 실시간 채팅

- 사용자의 상담 요청 시 **상담원 자동 매칭**, Redis를 통한 상담원 상태 관리
- 매칭 완료 후 **채팅방 자동 오픈** (WebSocket 기반)
- 채팅 메시지는 **STOMP 프로토콜 + WebSocket**을 통해 전달되며,
→ **Kafka**를 통해 비동기 처리 후 **MongoDB에 저장**

---

## 👥 팀원 및 역할

| 이름 | 역할 |
| --- | --- |
| 박태훈 | 팀장
- 매칭,라이더 서비스 도메인 설계 및 구현
- Kafka 기반 이벤트 시스템 설계 및 적용
- Command 패턴, CQRS 구조 적용을 통한 비지니스 로직 분리
- aws ECS기반 배 |
| 김형찬 | - 테크 리더
- 프로젝트 구성 및 개발 환경 구축 
- Naver Maps API를 이용한 라이더 최단 경로
- Redis GEO/WebSocket을 이용한 라이더 실시간 위치 추적. |
| 박보현 | - 인증/인가 서비스 구현
- Api Gateway 구현
- 회원가입, 로그인, 로그아웃 기능 구현
- WebSocket +STOMP + Kafka + MongoDB 구조로 상담원 채팅 서비스 구현 |
| 박경린 | - 인센티브
- Kafka Stream을 활용한 인센티브 로직 구현 및 파티션을 이용한 병렬처리
- 인센티브 중복 지급 방지를 위한 StateStore 이용 및 데이터 과부하 방지를 위해 하루 기준 StateStore 데이터 제거
- Prometheus+Grafana를 이용한 모니터링 
- DLQ 를 이용한 재처리 실패 처리
- DLQ 전송시 Prometheus+Grafana 를 이용한 에러 이벤트 Slack 알림 전송- |
| 김지현 | - 라이더 매칭 알림 서비스 구현
- 음식점 주소(위,경도) 기준으로 주변의 라이더 조회 후 Slack, WebSocket으로 비동기 알림 전송
- 알림 전송 실패시 3회 재시도 후 DLT로 전송, Slack 알림 전송 처리
- Redis/LeastBusy으로 상담원 매칭 서비스 구현 |

---

## 🛠️ 기술 스택

🌿 **Framework**

- Java 17
- Spring Boot 3.4
- Eureka
- Feign Client
- JPA
- QueryDsl

💽 **Database**

- PostgreSQL: 하나의 데이터베이스에 여러 스케마를 구성할 수 있어 선택
- Redis :  빠른 활용이 필요한 데이터 저장 및 락을 위해 사용

📡 **Messaging**

- Kafka : 서비스 간 메시지브로커와 데이터 스트림으로 메시지와 데이터 처리
- WebSocket : 위치 추적, 채팅, 알림 통신을 열어 지속적인 실시간 데이터 통신에 활용

📊**Monitoring**

- Prometheus : 매칭, 위치 추적 등 서비스 성능 모니터링
- Grafana : 모니터링을 보기 편하게 도식화

**🗄️Infrastructure**

- Aurora :  postgresql DB를 aws의 aurora로 배포
- Elastic Container Registry : 각 서비스의 컨테이너 이미지를 AWS ECR로 관리
- Elastic Container Service :  각 서비스를 ECS를 통해 Fargate로 배포.

---

## 🗂️ ERD

![image.png](attachment:31b4c22c-fdf5-48ba-bb7e-10d9a3acd034:image.png)

---

## 🏗️ System Architecture

![image.png](attachment:aae80cd3-9c6d-446e-83bc-fc9244839be6:image.png)

---

## ✨ 프로젝트 특징

- ✅ Eureka, Gateway 를 활용한 서비스 등록·탐색 구현
- ✅ Kafka 기반 비동기 이벤트 처리
- ✅ 보상 트랜잭션 구현
- ✅ DLQ 기반 장애 대응 및 재처리 로직
- ✅ 라이더 매칭 실시간 알림 (RabbitMQ, Slack, WebSocket)
- ✅ 라이더 실시간 위치 추적 (Redis GEO, WebSocket)
- ✅ Prometheus, Grafana 기반 모니터링, Slack 메시지를 통한 에러 메시지 전송

---
