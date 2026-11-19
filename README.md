# backend · 포스트잇 투두

Java 21과 Spring Boot를 사용하는 조회용 스켈레톤입니다. Flyway가 H2의 `todos` 테이블을 만들고, 서버 시작 시 `TodoInitializer`가 테이블이 비어 있으면 시연용 포스트잇 20개를 저장합니다. 이전 시연용 포스트잇 세 개만 있는 DB에는 나머지 17개를 추가합니다. `GET /api/todos`에서 목록을 확인할 수 있습니다. DB 파일은 `data/`에 저장됩니다.

```powershell
.\gradlew.bat bootRun
```

macOS 또는 Linux에서는 `./gradlew bootRun`을 실행합니다. 기본 포트는 8080입니다.

Spring의 호출 관계는 `TodoController → TodoService → TodoRepository → todos`입니다. DevLink에서는 이 저장소를 BE로 등록하고 FE 저장소의 연결 대상에 지정합니다. 쓰기 요청 DTO와 오류 응답은 쓰기 API를 구현하는 단계에서 추가합니다.
