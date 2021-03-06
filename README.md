## 여행자 클럽 마일리지 서비스
사용자들이 장소에 리뷰를 작성할 때 포인트를 부여하고, 전체/개인에 대한 포인트 부여 히스토리와 누적 포인트를 관리하고자 합니다

## 개발 스펙
* Kotlin
* Spring Boot
* Spring Data JPA
* h2 database
* JUnit
* Spring RestDocs

## tree
```bash
├── README.md
├── build.gradle.kts
└── src
    ├── docs
    │   └── asciidoc
    │       ├── executePointEvent.adoc
    │       ├── getUserPoints.adoc
    │       └── index.adoc
    ├── main
    │   ├── kotlin
    │   │   └── com
    │   │       └── triple
    │   │           └── member
    │   │               ├── MemberApplication.kt
    │   │               ├── application
    │   │               │   ├── aggregate
    │   │               │   │   └── PointReviewAggregate.kt
    │   │               │   └── point
    │   │               │       └── PointService.kt
    │   │               ├── common
    │   │               │   ├── handler
    │   │               │   │   └── PointExceptionHandler.kt
    │   │               │   └── validation
    │   │               │       └── EnumValidatorOfHttpRequest.kt
    │   │               ├── config
    │   │               │   └── JpaAuditingConfig.kt
    │   │               ├── domain
    │   │               │   ├── point
    │   │               │   │   ├── command
    │   │               │   │   │   └── PointCommander.kt
    │   │               │   │   ├── enums
    │   │               │   │   │   ├── PointActionType.kt
    │   │               │   │   │   ├── PointHistoryIncreaseType.kt
    │   │               │   │   │   └── PointType.kt
    │   │               │   │   ├── exception
    │   │               │   │   │   └── PointUserNotFoundException.kt
    │   │               │   │   └── vo
    │   │               │   │       └── PointUserVO.kt
    │   │               │   └── review
    │   │               │       ├── Review.kt
    │   │               │       ├── command
    │   │               │       │   └── ReviewCommander.kt
    │   │               │       └── exception
    │   │               │           ├── AlreadyRegisteredReviewException.kt
    │   │               │           └── NotRegisteredReviewException.kt
    │   │               ├── infrastructure
    │   │               │   ├── BaseTimeEntity.kt
    │   │               │   ├── point
    │   │               │   │   ├── PointEntity.kt
    │   │               │   │   ├── PointHistoryEntity.kt
    │   │               │   │   ├── PointHistoryRepository.kt
    │   │               │   │   └── PointRepository.kt
    │   │               │   └── review
    │   │               │       ├── ReviewEntity.kt
    │   │               │       ├── ReviewPhotoEntity.kt
    │   │               │       ├── ReviewPhotoRepository.kt
    │   │               │       └── ReviewRepository.kt
    │   │               └── interfaces
    │   │                   └── point
    │   │                       ├── param
    │   │                       │   ├── PointOfErrorResponse.kt
    │   │                       │   ├── PointOfHttpRequest.kt
    │   │                       │   └── PointOfHttpResponse.kt
    │   │                       └── rest
    │   │                           └── PointController.kt
    │   └── resources
    │       ├── application.yml
    │       ├── data.sql
    │       └── static
    │           └── docs
    │               ├── executePointEvent.html
    │               ├── getUserPoints.html
    │               └── index.html
    └── test
```

## API 문서 확인
> Spring RestDocs로 API 문서를 작성하였습니다<br/>
> build 후 확인이 가능합니다 <br/>
> ./gradlew build -x test 테스트를 제외할 시 API 문서는 생성되지 않습니다

![RestDocs](./explain/picture/restdocs.png)
```shell
http://localhost:8080/docs/index.html
```

## h2 확인
> in-memory db인 h2를 사용하였습니다<br/>
> ddl-auto는 create이기에 drop-create 순으로 ddl을 실행합니다<br/>
> 초기 데이터는 data.sql에 정의 되어 있습니다

![h2_로그인화면](./explain/picture/h2_1.png)
![h2_접속화면](./explain/picture/h2_2.png)
```shell
http://localhost:8080/h2/
JDBC url: jdbc:h2:mem://localhost/~/test
USER Name: sa
password: 
```

## test coverage
> 36개의 test가 작성되었습니다<br/>
> line coverage는 97% 정도입니다

![coverage](./explain/picture/coverage.png)

## 실행 방법
```shell
./gradlew clean
./gradlew build
java -jar ./build/libs/member.jar
```
