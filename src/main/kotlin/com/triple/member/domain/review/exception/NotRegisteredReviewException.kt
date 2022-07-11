package com.triple.member.domain.review.exception

class NotRegisteredReviewException(
    private val reviewId: String,
) : RuntimeException() {

    override val message: String by lazy {
        "등록되지 않은 reviewId 입니다, reviewId: $reviewId"
    }
}
