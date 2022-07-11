package com.triple.member.domain.point.command

import com.triple.member.domain.point.enums.PointHistoryIncreaseType
import com.triple.member.domain.point.enums.PointType
import com.triple.member.domain.point.exception.PointUserNotFoundException
import com.triple.member.domain.point.vo.PointUserVO
import com.triple.member.domain.review.Review
import com.triple.member.infrastructure.point.PointEntity
import com.triple.member.infrastructure.point.PointHistoryRepository
import com.triple.member.infrastructure.point.PointRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class PointCommander(
    private val pointRepository: PointRepository,
    private val pointHistoryRepository: PointHistoryRepository,
) {
    fun getUserPoint(userId: String): PointUserVO = pointRepository.findByUserId(userId)
        .orElseThrow { throw PointUserNotFoundException(userId) }
        .convertToPointUserVO()

    fun plusPoint(review: Review, firstReview: Boolean) {
        val pointEntity = pointRepository.findByUserId(review.userId)
            .orElse(PointEntity(userId = review.userId))

        increasePoint(review, pointEntity, PointType.REVIEW)
        if (review.attachedPhotoIds.isNotEmpty()) increasePoint(review, pointEntity, PointType.PHOTO)
        pointRepository.save(pointEntity)

        if (firstReview) increasePoint(review, pointEntity, PointType.BONUS)
    }

    fun updatePoint(review: Review, firstReview: Boolean) {
        val pointEntity = pointRepository.findByUserId(review.userId)
            .orElseThrow { throw PointUserNotFoundException(review.userId) }

        val pointPhotoHistory = getPointHistoryListByPointType(review.reviewId, PointType.PHOTO)
        if (review.attachedPhotoIds.isEmpty()) {
            /**
             * 사진 정책 - 리뷰의 사진이 존재하지 않는다
             *
             * 최근 사진히스토리가 있고 +1 가점을 받았을 경우, 점수를 1점 회수한다
             */
            if (
                pointPhotoHistory.isNotEmpty() &&
                pointPhotoHistory[0].increaseType == PointHistoryIncreaseType.PLUS
            ) decreasePoint(review, pointEntity, PointType.PHOTO)
        } else {
            /**
             * 사진 정책 - 리뷰의 사진이 존재한다
             *
             * 최근 사진 히스토리가 없거나, 최근 사진 히스토리가 -1일 경우, 가점 1점을 부여한다
             */
            if (
                pointPhotoHistory.isEmpty() ||
                pointPhotoHistory[0].increaseType == PointHistoryIncreaseType.MINUS
            ) increasePoint(review, pointEntity, PointType.PHOTO)
        }

        if (firstReview) {
            val pointBonusHistory = getPointHistoryListByPointType(review.reviewId, PointType.BONUS)
            /**
             * 보너스 정책 - 첫 리뷰일 경우
             *
             * 최근 보너스 히스토리가 없거나, 최근 보너스 히스토리가 -1일 경우, 가점 1점을 부여한다
             */
            if (
                pointBonusHistory.isEmpty() ||
                pointBonusHistory[0].increaseType == PointHistoryIncreaseType.MINUS
            ) increasePoint(review, pointEntity, PointType.BONUS)
        }
    }

    fun minusPoint(review: Review, firstReview: Boolean) {
        val pointEntity = pointRepository.findByUserId(review.userId)
            .orElseThrow { throw PointUserNotFoundException(review.userId) }

        val pointBonusHistory = getPointHistoryListByPointType(review.reviewId, PointType.BONUS)
        if (
            pointBonusHistory.isNotEmpty() &&
            pointBonusHistory.first().increaseType == PointHistoryIncreaseType.PLUS
        ) decreasePoint(review, pointEntity, PointType.BONUS)

        val pointPhotoHistory = getPointHistoryListByPointType(review.reviewId, PointType.PHOTO)
        if (
            pointPhotoHistory.isNotEmpty() &&
            pointPhotoHistory.first().increaseType == PointHistoryIncreaseType.PLUS
        ) decreasePoint(review, pointEntity, PointType.PHOTO)

        decreasePoint(review, pointEntity, PointType.REVIEW)
    }

    private fun increasePoint(
        review: Review,
        pointEntity: PointEntity,
        type: PointType,
    ) {
        pointEntity.points += 1
        pointHistoryRepository.save(
            review.convertToPointHistoryEntity(pointEntity.points)
                .apply {
                    pointType = type
                    increaseType = PointHistoryIncreaseType.PLUS
                }
        )
    }

    private fun decreasePoint(
        review: Review,
        pointEntity: PointEntity,
        type: PointType,
    ) {
        pointEntity.points -= 1
        pointHistoryRepository.save(
            review.convertToPointHistoryEntity(pointEntity.points)
                .apply {
                    pointType = type
                    increaseType = PointHistoryIncreaseType.MINUS
                }
        )
    }

    private fun getPointHistoryListByPointType(
        reviewId: String,
        pointType: PointType
    ) = pointHistoryRepository.findByReviewIdAndPointType(reviewId, pointType, Pageable.ofSize(1))
}
