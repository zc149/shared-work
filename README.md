# 📌 프로젝트 개요

## 1. 프로젝트 배경 및 목표

### 🎯 프로젝트 배경
- JWT를 활용한 인증 및 인가 방식 구현
- Redis를 통한 인메모리 데이터 저장 및 웹소켓 기반 실시간 채팅 기능 학습
- OAuth 2.0 작동 원리 이해 (📆 2024.10.11 추가)

### 🎯 학습 목표 및 일정
- JWT 기반 토큰 인증 및 Redis 세션 최적화 구현
- 웹소켓을 통한 실시간 통신 기능 구현
- OAuth 2.0 로그인 구현 (📆 2024.10.11 추가)
- 테스트 코드 작성 완료 (📆 2024.10.18 추가)
- Static vs Redis 성능 비교 테스트 완료 (📆 2025.05.01 추가)

### ⏱ 개발 기간
- 2024.08.01 ~ 2024.08.13 (1인 개발)

---

## 2. 기술 스택

![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-%23005C0F.svg?style=for-the-badge&logo=Thymeleaf&logoColor=white)
![Bootstrap](https://img.shields.io/badge/bootstrap-%238511FA.svg?style=for-the-badge&logo=bootstrap&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)
![Nginx](https://img.shields.io/badge/nginx-%23009639.svg?style=for-the-badge&logo=nginx&logoColor=white)

---

## 3. 시스템 아키텍처

![아키텍처](https://github.com/zc149/shared-work/blob/main/%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98.png)

---

## 4. 주요 기능

![메인화면](https://github.com/zc149/shared-work/blob/main/%EB%A9%94%EC%9D%B8%ED%99%94%EB%A9%B4.png)

### ✅ TODO 리스트
- 사용자가 할 일을 일정에 맞춰 관리할 수 있는 기능

### ✅ 실시간 채팅
- 팀원과 실시간으로 소통 가능한 채팅 기능 (웹소켓 기반)

---

## 5. 개선 사항 및 성능 분석

### 🔄 Static 전역 변수 vs Redis (성능 비교 및 도입 배경)

실시간 채팅 메시지의 인메모리 저장 전략으로 Static Map과 Redis를 비교 테스트했습니다.

#### 🧪 단일 테스트 시나리오
- 조건: 하나의 채팅방에 10,000개의 메시지를 저장 및 조회
- 결과:
  - 저장: Redis ❌ 약 26배 느림
  - 조회: Redis ❌ 약 15배 느림

> 단일 JVM 내에서는 static map 접근 속도가 매우 빠르기 때문에 유리함.

#### 🧵 병렬 테스트 시나리오
- 조건: 10개의 스레드가 각각 채팅방을 생성하고 10,000개의 메시지를 저장 및 조회
- 결과:
  - 저장: Redis ✅ 약 31% 빠름
  - 조회: Redis ✅ 약 91% 빠름

> 병렬 환경에서는 static map의 동기화 비용이 성능 저하 요인.  
> 반면 Redis는 병렬 처리를 효과적으로 수행하며 키 격리를 통해 경합을 최소화함.

#### 🧩 Redis 채택 이유

- 서버 재시작 시 데이터 손실 없이 복원 가능 (AOF/RDB 기반)
- 여러 서버에서 하나의 Redis를 공유 가능 → 수평 확장에 유리
- TTL 설정, pub/sub, 비동기 처리 등 고급 기능 제공 → 유지보수 및 확장성 강화

✅ 결론: 단순 성능을 넘어, 안정성과 유연한 아키텍처 설계를 위해 Redis는 효과적인 선택이었음.

---

### ⚙️ Redis 활용 전략: 채팅 부하 분산

| 항목 | 기존 방식 | 개선 방식 (Redis 활용) |
|------|------------|---------------------------|
| 메시지 저장 | 매 메시지 DB 저장 | Redis에 저장 후, 방 종료 시 DB로 이동 |
| 장점 | 없음 | DB 부하 감소, 응답 속도 향상 |

---

### 🔐 Stateless JWT 인증 구조

- 세션 서버가 필요 없는 Stateless 아키텍처
- Access Token 짧은 유효시간 설정 → Refresh Token으로 재발급
- 비용 및 인프라 관리 측면에서 효율적

---

## 6. 부가 기능

- ✅ OAuth 2.0 소셜 로그인 (📆 2024.10.11 추가)
