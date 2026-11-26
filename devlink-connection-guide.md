# DevLink 연결 정리

이 PC에서 Claude Code와 Codex를 DevLink Agent로 연결할 때 맞춰야 하는 것을 정리한 문서입니다. 2026-11-26 기준이며, 경로는 이 PC의 실제 값입니다.

## 1. 로컬에서 돌아가는 구성 요소

```text
[Claude Code] ──stdio── [MCP 서버] ──로컬 HTTP── [브로커] ──WebSocket── [중앙 서버]
```

| 구성 요소 | 실체 | 빌드/실행 | 맞춰야 하는 것 |
| --- | --- | --- | --- |
| Claude Code | 데스크톱 앱 | 빌드 없음. 세션을 열 때 MCP 서버를 자식 프로세스로 띄움 | 설정을 바꾸면 세션을 새로 열어야 함 |
| MCP 서버 | `C:\dev\DevLink_client\packages\mcp-server\dist\index.js` | `npm --prefix C:\dev\DevLink_client\packages\mcp-server run build` | 브로커와 버전이 같아야 함 |
| 브로커 | `C:\Users\SSAFY\.cargo\bin\devlink.exe` | `devlink --config <프로필> start`로 프로필마다 하나씩 실행 | 프로필의 토큰이 유효해야 뜸 |

DevLink 소스를 업데이트하면 MCP 서버와 `devlink` CLI를 둘 다 다시 빌드해야 버전이 맞습니다.

## 2. 설정 위치

| 위치 | 파일 | 담는 것 | 누가 읽나 |
| --- | --- | --- | --- |
| 프로필 | `C:\Users\SSAFY\.devlink\op-infra\config.json`과 같은 폴더의 `.devlink-runtime\` | Agent ID, Agent 토큰, 프로젝트 폴더, 로그인 세션, `broker.json`(브로커 주소) | `devlink` CLI와 브로커 |
| 전역 Claude 설정 | `C:\Users\SSAFY\.claude.json`의 `mcpServers.devlink` | MCP 서버 실행 명령과 `DEVLINK_BROKER_FILE` 경로 | Claude Code (모든 폴더) |
| 프로젝트 | `.mcp.json` | 프로젝트용 MCP 서버 정의 | Claude Code. 단, 데스크톱 앱에서는 로드되지 않았음 |
| 프로젝트의 `.claude` | `.claude\settings.local.json` | `.mcp.json` 서버 승인 | Claude Code. 넣었지만 효과 없었음 |
| 프로젝트의 `.codex` | `.codex\config.toml`, `.codex\devlink.env` | Codex용 MCP 서버 정의와 `DEVLINK_BROKER_FILE` 경로 | Codex |

- 전역 Claude 설정은 `.claude` 폴더가 아니라 홈 폴더의 `.claude.json` 파일입니다.
- 기본 프로필은 `C:\Users\SSAFY\.devlink\config.json`입니다. `devlink` 명령에서 `--config`를 빼면 항상 이 프로필을 봅니다.

## 3. 서로 맞아야 하는 연결 고리

```text
Claude Code를 연 폴더
   ║ (같아야 함)
프로필의 프로젝트 폴더 ── 프로필의 Agent ── 그 프로필로 띄운 브로커
                                               ║ (같은 프로필)
MCP 설정의 DEVLINK_BROKER_FILE ───────── 프로필의 broker.json
   │
   └ args의 MCP 진입점 ── 빌드된 dist (브로커와 같은 버전)
```

1. **폴더**: Host를 연 폴더와 프로필에 바인딩된 프로젝트 폴더가 같아야 합니다.
2. **브로커 경로**: MCP 설정의 `DEVLINK_BROKER_FILE`이 연결하려는 Agent의 프로필 `broker.json`을 가리켜야 합니다. 이 값이 어느 Agent로 붙을지를 정합니다.
3. **브로커 실행**: 그 프로필의 브로커가 떠 있어야 합니다. 프로필이 다르면 여러 브로커를 동시에 실행할 수 있습니다.
4. **버전**: MCP 서버 빌드와 브로커 버전이 같아야 합니다.
5. **도구 유형**: Agent의 도구 유형과 Host가 같아야 합니다.
6. **세션**: Agent 하나는 살아 있는 세션 하나만 소유합니다. 세션을 새로 열 때마다 `register_agent({})`를 다시 호출해야 합니다.

## 4. 이 PC의 현재 구성

| Agent | Host | 프로필 | MCP 설정 위치 |
| --- | --- | --- | --- |
| OP_Infra (ID 35) | Claude Code | `C:\Users\SSAFY\.devlink\op-infra` | 전역 `C:\Users\SSAFY\.claude.json` |
| OP_UI (ID 34) | Codex | `C:\Users\SSAFY\.devlink` (기본) | 프로젝트 `.codex\config.toml` |

두 Agent 모두 프로젝트 폴더는 `C:\Users\SSAFY\Demo-Post\backend`입니다.

전역 Claude 설정이 `op-infra`를 가리키므로, 다른 폴더에서 연 Claude Code도 OP_Infra 브로커에 붙습니다.

## 5. 매번 하는 순서 (OP_Infra, Claude Code)

### 5.1 브로커 실행

```powershell
devlink --config C:\Users\SSAFY\.devlink\op-infra\config.json start
```

- `--config`를 빼면 기본 프로필(OP_UI)의 브로커가 뜹니다.
- 다른 프로필의 브로커는 끌 필요가 없습니다.

### 5.2 브로커 상태 확인

```powershell
devlink --config C:\Users\SSAFY\.devlink\op-infra\config.json setup status
```

- `Broker: Running`, `Central: Connected`, `Identity: Verified`, `Credentials: Valid`가 모두 나와야 합니다.
- `Host session: Unbound`는 이 단계에서 정상입니다.

### 5.3 Claude Code 세션 새로 열기

- 프로필에 바인딩된 폴더에서 엽니다.
- MCP 프로세스는 시작할 때 설정과 코드를 한 번만 읽습니다. 브로커를 켜기 전이나 설정을 바꾸기 전에 열어 둔 세션은 다시 열어야 합니다.

### 5.4 세션 안에서 연결

```text
DevLink 연결 상태와 내 Agent 정보를 확인해줘. get_my_context에 OP_Infra(ID 35)가 나오면 register_agent({})로 연결해줘.
```

- `get_connection_status`: `nextActions`에 `register_agent`가 있어야 합니다.
- `get_my_context`: Agent가 OP_Infra(ID 35)이고 프로젝트 경로가 지금 폴더여야 합니다.
- `register_agent({})`: 반환된 `agentId`가 35면 온라인입니다.

OP_UI(Codex)는 `--config` 없이 `devlink start`를 쓰고, Codex 세션에서 같은 요청을 OP_UI(ID 34) 기준으로 합니다.

## 6. 처음이거나 뭔가 바뀌었을 때만

### 6.1 로그인이 만료됐을 때

```powershell
devlink --config C:\Users\SSAFY\.devlink\op-infra\config.json auth login --email <이메일>
```

로그인 세션은 프로필마다 따로 저장됩니다. `--config` 없이 로그인하면 기본 프로필에만 들어갑니다.

### 6.2 프로젝트 폴더를 바꿀 때

```powershell
devlink --config C:\Users\SSAFY\.devlink\op-infra\config.json agent use --id 35 --project <새 폴더>
```

- 브로커를 먼저 꺼야 합니다.
- 바꾼 뒤에는 토큰이 `token_required`가 되므로 6.3을 이어서 해야 합니다.

### 6.3 토큰 재발급과 설정 적용

```powershell
devlink --config C:\Users\SSAFY\.devlink\op-infra\config.json setup --tool claude-code --issue-token --apply-mcp --node-path "C:\Program Files\nodejs\node.exe" --mcp-entry C:\dev\DevLink_client\packages\mcp-server\dist\index.js
```

- `--tool`은 Agent의 도구 유형과 같아야 합니다.
- 이 PC에서는 Node와 MCP 진입점 자동 탐지가 실패해서 `--node-path`와 `--mcp-entry`를 직접 지정해야 합니다.
- 이 명령은 브로커 시작까지 합니다.

### 6.4 DevLink를 업데이트했을 때

```powershell
npm --prefix C:\dev\DevLink_client\packages\mcp-server run build
```

`devlink --version`과 MCP 서버 빌드 버전이 같아야 합니다.

## 7. 겪었던 증상과 원인

| 증상 | 원인 | 해결 |
| --- | --- | --- |
| `hostSession=conflict` | 다른 프로세스가 이미 그 Agent를 소유 중 | 그 Agent는 그대로 두고, 다른 Agent는 별도 프로필로 연결 |
| `USER_SESSION_EXPIRED` | `--config` 없이 로그인해서 다른 프로필에 세션이 저장됨 | 같은 `--config`로 다시 로그인 |
| `SETUP_PROJECT_BINDING_CONFLICT` | 프로필의 프로젝트 폴더와 다른 폴더에서 `setup` 실행 | `agent use --project`로 먼저 폴더 변경 |
| `SETUP_TOOL_UNAVAILABLE` | Node와 MCP 진입점 자동 탐지 실패 | `--node-path`, `--mcp-entry` 지정 |
| Claude Code가 엉뚱한 Agent 브로커에 붙음 | 전역 `.claude.json`의 `DEVLINK_BROKER_FILE`이 다른 프로필을 가리킴 | 경로를 연결할 프로필의 `broker.json`으로 변경 |
| `get_my_context`가 `BROKER_CONTEXT_INVALID` | MCP 서버 빌드(0.1.2)가 브로커(0.1.5)보다 오래됨 | MCP 서버 다시 빌드 |
| 세션을 다시 열었더니 Agent가 OFFLINE | 소유하던 MCP 프로세스가 끝남. 새 프로세스는 자동으로 다시 묶이지 않음 | 새 세션에서 `register_agent({})` 다시 호출 |
| `devlink agent status`가 다른 Agent를 보여줌 | `--config`를 빼서 기본 프로필을 조회함 | `--config`를 붙여 조회 |
