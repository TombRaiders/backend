# Tomb-Raiders 백엔드

Tomb-Raiders 프로젝트의 백엔드 시스템입니다.

## 기술 스택
- **언어**: Java 21
- **프레임워크**: Spring Boot 3.5.8
- **데이터베이스**: MySQL, PostgreSQL, H2 (테스트용)
- **캐시**: Redis
- **보안**: Spring Security, JWT, jBCrypt
- **문서화**: OpenAPI (Swagger)
- **인프라**: AWS S3, Docker

## 시작하기

### 사전 준비 사항
- Docker 및 Docker Compose
- Java 21 (로컬 개발용)

### 실행 방법 (Docker)

`.env.example` 파일을 환경 변수 파일로 사용하여 프로젝트를 실행하려면 아래 명령어를 사용하세요:

```bash
docker compose --env-file .env.example up -d
```

### 로컬 개발 환경 설정

1. 저장소를 클론합니다.
2. 설정: `.env.example` 파일을 참고하여 `.env` 파일을 생성하고 필요한 설정을 입력합니다.
3. 빌드 및 실행:
   ```bash
   ./gradlew bootRun
   ```

## API 문서

애플리케이션이 실행 중일 때 아래 주소에서 Swagger UI를 확인할 수 있습니다:
`http://localhost:8080/swagger-ui/index.html` (기본 포트 기준)

## 테스트 및 커버리지

- 테스트 실행: `./gradlew test`
- Checkstyle 검사: `./gradlew checkstyleMain`
- Jacoco 보고서: `build/reports/jacoco/test/html/index.html`
