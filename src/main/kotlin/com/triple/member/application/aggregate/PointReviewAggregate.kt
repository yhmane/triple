package com.triple.member.application.aggregate

import com.triple.member.domain.point.command.PointCommander
import com.triple.member.domain.point.enums.PointActionType
import com.triple.member.domain.review.command.ReviewCommander
import com.triple.member.interfaces.point.param.PointOfHttpRequest
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class PointReviewAggregate(
    private val reviewCommander: ReviewCommander,
    private val pointCommander: PointCommander,
) {

    @Transactional
    fun executePointEvent(pointOfHttpRequest: PointOfHttpRequest) {
        when (pointOfHttpRequest.pointActionType) {
            PointActionType.ADD -> addReview(pointOfHttpRequest)
            PointActionType.MOD -> updateReview(pointOfHttpRequest)
            PointActionType.DELETE -> deleteReview(pointOfHttpRequest)
        }
    }

    private fun addReview(pointOfHttpRequest: PointOfHttpRequest) {
        val review = pointOfHttpRequest.convertToReview()
        reviewCommander.addReview(review)
        val firstReview = reviewCommander.checkFirstReview(review)

        pointCommander.plusPoint(review, firstReview)
    }

    private fun updateReview(pointOfHttpRequest: PointOfHttpRequest) {
        val review = pointOfHttpRequest.convertToReview()
        reviewCommander.updateReview(review)
        val firstReview = reviewCommander.checkFirstReview(review)

        pointCommander.updatePoint(review, firstReview)
    }

    private fun deleteReview(pointOfHttpRequest: PointOfHttpRequest) {
        val review = pointOfHttpRequest.convertToReview()
        reviewCommander.deleteReview(review)
        pointCommander.minusPoint(review)
    }
}
