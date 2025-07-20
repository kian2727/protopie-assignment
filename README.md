# Protopie Assignment

## 프로젝트 구조

### 멀티 모듈 레이어드 아키텍처
프로젝트는 다음과 같은 레이어로 구성되어 있습니다:

```
protopie-assignment/
├── subproject/
│   ├── application/     # 비즈니스 로직
│   ├── boot/           # 애플리케이션 설정 및 실행
│   ├── domain/         # 도메인 모델 및 인터페이스
│   ├── infrastructure/ # 데이터베이스 및 외부 시스템 연동
│   └── presentation/   # API 엔드포인트 및 DTO
├── database/           # DB 스크립트
├── docker-compose.yaml # Docker 설정
└── Dockerfile         # 애플리케이션 Docker 설정
```

### 레이어드 아키텍처 선택 이유
1. **관심사의 분리**
   - 각 레이어는 독립적인 책임을 가지며, 다른 레이어의 구현 세부사항에 의존하지 않습니다.
   - 코드의 응집도를 높이고 결합도를 낮춰 유지보수성이 향상됩니다.

2. **테스트 용이성**
   - 각 레이어를 독립적으로 테스트할 수 있습니다.
   - 모킹(Mocking)을 통해 외부 의존성을 격리하여 단위 테스트가 가능합니다.

3. **변경의 유연성**
   - 특정 레이어의 구현을 변경해도 다른 레이어에 영향을 미치지 않습니다.
   - 예: 데이터베이스를 변경하더라도 비즈니스 로직은 수정할 필요가 없습니다.

### 각 레이어의 역할

#### 1. Domain Layer (domain)
- 핵심 비즈니스 로직과 규칙을 포함
- 외부 의존성이 없는 순수한 코틀린 코드로 구성
- 엔티티와 값 객체, 도메인 서비스 인터페이스 정의

#### 2. Application Layer (application)
- 도메인 객체를 조작하는 유스케이스 구현
- 트랜잭션 관리
- 도메인 이벤트 처리

#### 3. Infrastructure Layer (infrastructure)
- 데이터베이스 연동 (PostgreSQL + Exposed)
- 외부 시스템 통합 ( kafak(TODO) )
- 도메인 레이어에 정의된 인터페이스 구현

#### 4. Presentation Layer (presentation)
- HTTP API 엔드포인트 정의
- 요청/응답 DTO 처리
- 사용자 인증/인가 처리

#### 5. Boot Layer (boot)
- 애플리케이션 설정 및 구동
- 의존성 주입 설정
- 환경 설정 관리

## 기술 스택
- Kotlin
- Ktor
- hikaricp
- PostgreSQL
- Docker
- Kotest
- gradle


## 실행 방법

### Docker 실행
```bash
# 기존 이미지 삭제 후 재빌드
docker-compose down --rmi all && docker-compose up -d --build

# 컨테이너 중지, 이미지 삭제 후 새로 빌드하여 실행
docker-compose down --rmi all && docker-compose up -d --build
```


### API 문서
API 정의는 OpenAPI (Swagger) 스펙으로 작성되어 있으며, 다음 경로에서 확인할 수 있습니다:
```
subproject/boot/src/main/resources/openapi/documentation.yaml
```
서버 실행후 [localhost:8080/openapi](http:/localhost:8080/openapi) 에서 확인가능

주요 API 엔드포인트:
- `GET /health`: 서버 상태 확인
- `POST /signup`: 회원가입
- `POST /signin`: 로그인
- `GET /users`: 사용자 목록 조회 (관리자 전용)
- `GET /users/{userId}`: 사용자 정보 조회
- `PUT /users/{userId}`: 사용자 정보 수정
- `DELETE /users/{userId}`: 회원 탈퇴

## 데이터베이스 구조

### Users 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | UUID | PRIMARY KEY, DEFAULT uuid_generate_v4() | 사용자 고유 식별자 |
| email | VARCHAR(255) | NOT NULL, UNIQUE | 사용자 이메일 (중복 불가) |
| password | VARCHAR(255) | NOT NULL | 비밀번호 |
| username | VARCHAR(100) | - | 사용자 이름 |
| role | VARCHAR(100) | NOT NULL | 사용자 역할 (USER/ADMIN) |
| is_active | BOOLEAN | DEFAULT TRUE | 계정 활성화 상태 |
| deleted_at | TIMESTAMP | - | 삭제 일시 (Soft Delete) |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 생성 일시 |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 수정 일시 |

- `idx_users_email`: 이메일 검색 성능 향상을 위한 인덱스
- UUID를 사용하여 분산 환경에서의 ID 충돌 방지
- 이메일 유니크 제약으로 중복 가입 방지
- Soft Delete 패턴으로 데이터 이력 관리
- 타임스탬프 자동 기록으로 생성/수정 시간 추적