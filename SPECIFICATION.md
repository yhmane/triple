## SPECIFICATIONS
리뷰 작성이 이뤄질 때마다 리뷰 작성 이벤트가 발생하고, 아래 API로 이벤트를 전달합니다.

```shell
POST /events
{
  "type": "REVIEW",
  "action": "ADD", /* "MOD", "DELETE" */
  "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
  "content": " !",
  "attachedPhotoIds": ["e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"],
  "userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
  "placeId": "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
}
```

* type : 미리 정의된 String을 가지고 있습니다. 리뷰 이벤트는 "REVIEW"
* action : 리뷰 - 생성 ADD, 수정 MOD, 삭제 DELETE
* reviewId : UUID 포맷으로 reviewId를 나타냄
* content : 리뷰 내용
* attachedPhotoIds : 리뷰에 첨부된 이미들의 id (배열)
* userId : 리뷰 작성자 Id
* placeId : 리뷰가 작성된 장소의 Id

한 사용자는 장소마다 리뷰를 1개만 작성할 수 있고, 리뷰는 수정 또는 삭제가 가능하다. 리뷰 보상점수는 아래와 같다
* 내용 점수
  * 1자 이상 텍스트 : 1점
  * 1장 이상 사진 첨부 : 1점
* 보너스 점수
  * 특정 장소에 첫 리뷰 작성 : 1점

## REQUIREMENTS
* rdb - mysql 5.7 이상 필요, ddl (table, index)도 필요
* 두개의 API 작성
  * POST /events 포인트 적립 (ADD/MOD/DELETE)
  * 포인트 조회
* 상세 요구사항
  * REST API application
  * Java, Kotlin, Python, Javascript(typescript)
  * Framework, Library 자유, Data Storage 추가 필요시 자유
  * 테스트 케이스가 있음 좋음
  * README 파일 작성 (application 실행 방법 첨부)
  * github upload & url 공유


## REMARKS
* 포인트 증감 이력이 필요
* 사용자마다 현시점의 포인트 조회하거나 계산이 가능
* table full scan을 하지 않는 인덱스가 필요
* 리뷰 작성후 삭제시 내용 점수 및 보너스 점수 회수
* 리뷰 수정시 상황에 맞춰 점수를 부여하거나 회수
  * 글만 작성된 리뷰에 사진 추가시 점수 1점 부여
  * 글과 사진이 있는 리뷰에 사진을 모두 삭제하면 1점 회수
* 사용자 입장에서 본 첫 리뷰 일때 보너스 점수 1점 부여
  * 어떤 장소에 사용자 A가 리뷰를 남겼다가 삭제하고, 사용자 B가 리뷰를 남겼을 경우 사용자 B에게 보너스 점수 1점 부여
  * 어떤 장소에 사용자 A가 리뷰를 남겼다가 삭제하는데, 사용자 B가 리뷰가 삭제되기 이전 리뷰를 남겼을 경우 사용자 B에게 보너스 점수를 부여하지 않음
