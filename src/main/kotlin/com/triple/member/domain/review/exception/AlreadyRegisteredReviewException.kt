package com.triple.member.domain.review.exception

class AlreadyRegisteredReviewException(
    private val reviewId: String
) : RuntimeException() {

    override val message: String by lazy {
        "이미 등록된 reviewId 입니다, reviewId: $reviewId"
    }
}
