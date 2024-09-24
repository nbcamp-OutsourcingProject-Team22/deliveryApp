## 프로젝트 개요

## 기술스택

![인텔리제이](https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![깃허브](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)
![깃이그노어](https://img.shields.io/badge/gitignore.io-204ECF?style=for-the-badge&logo=gitignore.io&logoColor=white)
![깃](https://img.shields.io/badge/GIT-E44C30?style=for-the-badge&logo=git&logoColor=white)
Development
![자바](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![스프링](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![mysql](https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white)
Communication
![슬랙](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white)
![노션](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white)

## API

| 담당자 | 진행상황 | HTTP 메소드 | API 기능   | URI                              | Authorization                   | Request Body                                                                                                   | Response 200                                                                                                                                                        | Response 400                                            | Response 403                              | Response 404                                               |
|-----|------|----------|----------|----------------------------------|---------------------------------|----------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------|-------------------------------------------|------------------------------------------------------------|
| 송민지 | 완료   | POST     | 회원 가입    | /members/sign-up                 |                                 | {“email”: “email@email.com”, ”username”: ”mj”, ”password”: ”Asdf1234!”}                                        | {”code” : 200, ”message” : ”회원가입 성공”}                                                                                                                               | {”code” : “400”, ”message” : “중복된 이메일이거나 username입니다.”} |                                           |                                                            |
| 송민지 | 완료   | POST     | 로그 인     | /members/sign-in                 |                                 | {“email”: “email@email.com”, ”password”: ”Asdf1234!”}                                                          | {”code” : 200, ”data” : bearer ey~ }                                                                                                                                | {”code” : “400”, ”message” : “이메일이나 비밀번호를 확인해주세요”}      |                                           |                                                            |
| 송민지 | 완료   | PUT      | 회원 탈퇴    | /members/withdrawal              |                                 | {“withdrawalOfMembership”: false}                                                                              | {”code” : 200, ”message” : ”탈퇴 완료”}                                                                                                                                 | {”code” : “400”, ”message” : “회원정보를 확인해주세요”}            |                                           |                                                            |
| 김경민 | 완료   | POST     | 가게 생성    | /stores                          | “Authorization” : “Bearer xxxx” | {“storeName” : “TestStore”, ”openingTime” : “09:00:00”, ”closingTime” : “18:00:00”, ”minOrderAmount” : “7000”} | {”code” : 200, ”message” : ”가게 생성 완료”}                                                                                                                              | {”code” : 400, ”message” : ”가게이름은 공백 일 수 없습니다”}         | {”code” : 403, ”message” : ”사장 권한이 아닙니다”} |                                                            |
| 김경민 | 완료   | PUT      | 가게 수정    | /stores/{storeId}                | “Authorization” : “Bearer xxxx” | {“storeName” : “TestStore”, ”openingTime” : “09:00:00”, ”closingTime” : “18:00:00”, ”minOrderAmount” : “7000”} | {”code” : 200, ”message” : ”가게 수정 완료”}                                                                                                                              |                                                         | {”code” : 403, ”message” : ”가게 주인이 아닙니다”} |                                                            | {”code” : 404, ”message” : ”가게를 찾을 수 없습니다”}                |
| 김경민 | 완료   | DELETE   | 가게 폐업    | /stores/{storeId}                | “Authorization” : “Bearer xxxx” |                                                                                                                | {”code” : 200, ”message” : ”가게 폐업 완료”}                                                                                                                              |                                                         | {”code” : 403, ”message” : ”사장 권한이 아닙니다”} |                                                            | {”code” : 404, ”message” : ”가게를 찾을 수 없습니다”}                |
| 김경민 | 완료   | GET      | 가게 조회    | /stores/list?page=1&size=10      | “Authorization” : “Bearer xxxx” |                                                                                                                | {”code” : 200, ”message” : ”가게 다건 조회”, ”data” :[ { “storeName” : “TestStore”, ”openingTime” : “09:00:00”, ”closingTime” : “18:00:00”, ”minOrderAmount” : “7000”} ]} |                                                         |                                           |                                                            |
| 김경민 | 완료   | GET      | 가게 조회    | /stores/{storeId}                | “Authorization” : “Bearer xxxx” |                                                                                                                | {”code” : 200, ”message” : ”가게 단건 조회”, ”data” :[ { “storeName” : “TestStore”, ”openingTime” : “09:00:00”, ”closingTime” : “18:00:00”, ”minOrderAmount” : “7000” }]} |                                                         |                                           | {”code” : 404, ”message” : ”가게를 찾을 수 없습니다”}                |
| 배주희 | 완료   | POST     | 메뉴 생성    | /stores/{storeId}/menus          | “Authorization” : “Bearer xxxx” | {“storeId” : 1, “menuName” : “Menu Name”, “price” : 10000, “description” : “Description”}                      | {”code” : 200, ”data” : {“menuId” : 1, “storeId” : 1, “menuName” : “Menu Name”, “price” : 10000, “description” : “Description”}}                                    |                                                         |                                           | {”code” : 404, ”message” : ”권한이 없거나 필수 정보가 누락되었습니다.”}      |                                                   |                                                            |
| 배주희 | 완료   | PUT      | 메뉴 수정    | /stores/{storeId}/menus/{menuId} | “Authorization” : “Bearer xxxx” | {“storeId” : 1, “menuName” : “Updated Menu Name”, “price” : 12000, “description” : “Updated Description”}      | {”code” : 200, ”data” : {“menuId” : 1, “storeId” : 1, “menuName” : “Updated Menu Name”, “price” : 12000, “description” : “Updated Description”}}                    |                                                         |                                           | {”code” : 404, ”message” : ”권한이 없거나 수정하려는 메뉴가 존재하지 않습니다.”} |                                                        |                                                            |
| 배주희 | 완료   | DELETE   | 메뉴 삭제    | /stores/{storeId}/menus/{menuId} | “Authorization” : “Bearer xxxx” |                                                                                                                | {”code” : 200, ”message” : ”메뉴가 성공적으로 삭제되었습니다.”}                                                                                                                    |                                                         |                                           | {”code” : 404, ”message” : ”권한이 없거나 삭제하려는 메뉴가 존재하지 않습니다.”} |                                                   |                                              |
| 김창민 | 완료   | POST     | 주문 요청    | /user/orders/request             | “Authorization” : “Bearer xxxx” | {”storeId” : , ”menuId” : }                                                                                    | {”code” : 200, ”message” : ”주문 저장에 성공하였습니다.”, ”data” :[ { “orderId” : }]}                                                                                           |                                                         |                                           | {”code” : 404, ”message” : ”가게를 찾을 수 없습니다.”}               |                                                             |                                                             |
| 김창민 | 완료   | GET      | 주문 상태 조회 | /user/orders/{orderId}           | “Authorization” : “Bearer xxxx” |                                                                                                                | {”code” : 200, ”message” : ”주문 조회에 성공하였습니다.”, ”data” :[ { “status” : “” }]}                                                                                         |                                                         | {”code” : 403, ”message” : ”권한이 없습니다.”}   | {”code” : 404, ”message” : ”주문을 찾을 수 없습니다.”}               |                                             |
| 김창민 | 완료   | PUT      | 주문 수락    | /owner/orders/{orderId}/accept   | “Authorization” : “Bearer xxxx” |                                                                                                                | {”code” : 200, ”message” : ”주문 수락에 성공하였습니다.”}                                                                                                                       | {”code” : 400, ”message” : ”만료된 주문입니다.”}                | {”code” : 403, ”message” : ”권한이 없습니다.”}   | {”code” : 404, ”message” : ”주문을 찾을 수 없습니다.”}               |
| 김창민 | 완료   | PUT      | 주문 거절    | /owner/orders/{orderId}/reject   | “Authorization” : “Bearer xxxx” |                                                                                                                | {”code” : 200, ”message” : ”주문 거절에 성공하였습니다.”}                                                                                                                       | {”code” : 400, ”message” : ”만료된 주문입니다.”}                | {”code” : 403, ”message” : ”권한이 없습니다.”}   | {”code” : 404, ”message” : ”주문을 찾을 수 없습니다.”}               |
| 김창민 | 완료   | PUT      | 주문 거절    | /owner/orders/{orderId}/next     | “Authorization” : “Bearer xxxx” |                                                                                                                | {”code” : 200, ”message” : ”주문 진행에 성공하였습니다.”, ”data” :[ { “status” : “” }]}                                                                                         | {”code” : 400, ”message” : ”만료된 주문입니다.”}                | {”code” : 403, ”message” : ”권한이 없습니다.”}   | {”code” : 404, ”message” : ”주문을 찾을 수 없습니다.”}               |
| 고 결 | 완료   | POST     | 리뷰 생성    | /reviews                         | “Authorization” : “Bearer xxxx” | {”orderId” : , ”rating” : , ”contents” : }                                                                     | {”code” : 200, ”message” : ”리뷰 작성이 성공하였습니다.”, ”data” : [{ “storeName” : “”, ”rating” : “”, ”contents” : “” }]}                                                      | {”code” : 400, ”message” : ”배달이 완료되지 않았습니다.”}           |                                           |                                                            |
| 고 결 | 완료   | GET      | 리뷰 조회    | /reviews/stores/{storeId}        | “Authorization” : “Bearer xxxx” |                                                                                                                | {”code” : 200, ”message” : ”리뷰 조회 성공”, ”data” : [ { “reviewId” : , ”orderId” : , ”rating” : , ”contents” : ”” } ]}                                                  |                                                         |

## ERD

![image](https://github.com/user-attachments/assets/e4f631eb-fb24-4c6a-aa76-1ab45f7a6067)

## 트러블슈팅

## 프로젝트 팀원

| 이 름 | MBTI | 직책 |                  깃허브 주소                   |
|-----|:----:|:--:|:-----------------------------------------:|
| 배주희 | ISFJ | 팀장 |    [배주희](https://github.com/vege4944)     |
| 김경민 | ISFP | 팀원 |     [김경민](https://github.com/kkm4512)     |
| 김창민 | INFJ | 팀원 | [김창민](https://github.com/Rlackdals981010) |
| 고 결 | INTP | 팀원 |    [고결](https://github.com/gyeol9012)     |
| 송민지 | ISTJ | 팀원 |   [송민지](https://github.com/mj-song00?)    |
