# 1. 프로젝트 배경 및 목표
- ### 프로젝트 배경 :
  
  - JWT를 활용한 인증 및 인가 방식을 구현하고, Redis를 활용하여 인메모리 데이터 저장 및 웹 소켓을 통한 실시간 채팅 기능을 학습하기 위함

  - OAuth 2.0 작동원리 이해 (2024.10.11 추가)
  
- ### 학습 목표 및 일정 :

  - JWT를 통한 토큰 기반 인증 이해, Redis를 통한 세션 관리 최적화, 웹 소켓을 통한 실시간 통신 구현.

  - OAuth 2.0 로그인 구현 (2024.10.11 추가)
 
  - 테스트 코드 작성 완료 (2024.10.18 추가)

- ### 개발 기간 : 2024.08.01 ~ 2024.08.13 / 1인


# 2. 기술 스택
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-%23005C0F.svg?style=for-the-badge&logo=Thymeleaf&logoColor=white)
![Bootstrap](https://img.shields.io/badge/bootstrap-%238511FA.svg?style=for-the-badge&logo=bootstrap&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)
![Nginx](https://img.shields.io/badge/nginx-%23009639.svg?style=for-the-badge&logo=nginx&logoColor=white)


# 3. 시스템 아키텍처
![아키텍처](https://github.com/zc149/shared-work/blob/main/%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98.png)

# 4. 주요기능
![메인화면](https://github.com/zc149/shared-work/blob/main/%EB%A9%94%EC%9D%B8%ED%99%94%EB%A9%B4.png)

#### 1. TODO 리스트 : 사용자가 할 일을 일정에 맞춰 관리할 수 있는 기능입니다.
#### 2. 실시간 채팅 : 팀원과 실시간으로 소통할 수 있는 채팅 기능입니다.

# 5. 개선 사항

- ### Redis를 활용하여 실시간 채팅 데이터 DB의 부하를 줄임
  - **기존**: 채팅방의 이전 메시지 내역을 저장 및 불러오기 위해 메시지 전송마다 DB를 거치기 때문에 부하가 높음.
  - **개선**: 메시지 전송 시 Redis에 데이터를 저장한 후, 채팅방이 닫히거나 열리는 순간에만 DB에 접근하여 부하를 줄임.

- ### Stateless인 JWT를 활용하여 비용 감소
  - Session DB가 필요 없는 Stateless JWT를 선택하여 비용 관점에서 유리함. 
  - Access Token의 만료 시간을 짧게 설정하고, 만료 시 Refresh Token을 통해 Access Token 및 Refresh Token을 재발급.

# 6. 부가 기능
  - OAuth 2.0 로그인 기능 (2024.10.11 추가)
