package com.triple.member.domain.review

import com.triple.member.domain.point.enums.PointActionType
import com.triple.member.infrastructure.point.PointHistoryEntity
import com.triple.member.infrastructure.review.ReviewEntity

data class Review(

    val reviewId: String,
    val userId: String,
    val content: String,
    val placeId: String,
    val attachedPhotoIds: MutableList<String>,
    val pointActionType: PointActionType,
) {
    fun convertToReviewEntity() =
        ReviewEntity(
            reviewId = reviewId,
            userId = userId,
            content = content,
            placeId = placeId,
        )

    fun convertToPointHistoryEntity(points: Long) =
        PointHistoryEntity(
            reviewId = reviewId,
            userId = userId,
            actionType = pointActionType,
            points = points,
        )
}