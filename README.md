# backend · 포스트잇 투두

Java 21과 Spring Boot를 사용하는 조회용 스켈레톤입니다. `GET /api/todos`는 H2 파일 DB에 저장된 시연용 투두 세 개를 반환합니다. Flyway가 테이블과 초기 데이터를 준비하며 DB 파일은 `data/`에 저장됩니다.

```powershell
.\gradlew.bat bootRun
```

macOS 또는 Linux에서는 `./gradlew bootRun`을 실행합니다. 기본 포트는 8080입니다.

Spring의 호출 관계는 `TodoController → TodoService → TodoRepository → todos`입니다. DevLink에서는 이 저장소를 BE로 등록하고 FE 저장소의 연결 대상에 지정합니다. 쓰기 요청 DTO와 오류 응답은 쓰기 API를 구현하는 단계에서 추가합니다.
