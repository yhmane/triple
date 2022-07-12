## tree
프로젝트의 전체적인 구조입니다
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
        └── kotlin
            └── com
                └── triple
                    └── member
                        ├── MemberApplicationTests.kt
                        ├── application
                        │   ├── aggregate
                        │   │   └── PointReviewAggregateTest.kt
                        │   └── point
                        │       └── PointServiceTest.kt
                        ├── domain
                        │   ├── point
                        │   │   └── command
                        │   │       └── PointCommanderTest.kt
                        │   └── review
                        │       └── command
                        │           └── ReviewCommanderTest.kt
                        ├── infrastructure
                        │   ├── point
                        │   │   ├── PointHistoryRepositoryTest.kt
                        │   │   └── PointRepositoryTest.kt
                        │   └── review
                        │       └── ReviewRepositoryTest.kt
                        └── interfaces
                            ├── AbstractRestDocs.kt
                            └── point
                                ├── rest
                                │   └── PointControllerTest.kt
                                └── restdocs
                                    ├── PointExecuteEventRestDocs.kt
                                    └── PointGetUserPointsRestDocs.kt  
```    