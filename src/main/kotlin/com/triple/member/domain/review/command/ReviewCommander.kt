package com.triple.member.domain.review.command

import com.triple.member.domain.review.Review
import com.triple.member.domain.review.exception.AlreadyRegisteredReviewException
import com.triple.member.domain.review.exception.NotRegisteredReviewException
import com.triple.member.infrastructure.review.ReviewEntity
import com.triple.member.infrastructure.review.ReviewPhotoEntity
import com.triple.member.infrastructure.review.ReviewPhotoRepository
import com.triple.member.infrastructure.review.ReviewRepository
import org.springframework.stereotype.Component
import kotlin.streams.toList

@Component
class ReviewCommander(
    private val reviewRepository: ReviewRepository,
    private val reviewPhotoRepository: ReviewPhotoRepository,
) {

    fun addReview(review: Review) {
        reviewRepository.findByReviewId(review.reviewId)
            .ifPresent { throw AlreadyRegisteredReviewException(review.reviewId) }

        val reviewEntity = review.convertToReviewEntity()
        reviewRepository.save(reviewEntity)

        val attachedPhotoSet =
            if (review.attachedPhotoIds.isEmpty()) mutableSetOf<String>() else review.attachedPhotoIds.toSet() as MutableSet<String>
        addReviewPhotos(reviewEntity, attachedPhotoSet)
    }

    fun updateReview(review: Review) {
        val reviewEntity = reviewRepository.findByReviewId(review.reviewId)
            .orElseThrow { throw NotRegisteredReviewException(review.reviewId) }

        updatePhoto(reviewEntity, review)
        reviewEntity.updateReview(review)
    }

    fun deleteReview(review: Review) {
        val reviewEntity = reviewRepository.findByReviewId(review.reviewId)
            .orElseThrow { throw NotRegisteredReviewException(review.reviewId) }

        reviewPhotoRepository.deleteAllByReviewId(review.reviewId)
        reviewRepository.delete(reviewEntity)
    }

    private fun updatePhoto(
        reviewEntity: ReviewEntity,
        review: Review,
    ) {
        val dbPhotoIds = reviewEntity.reviewPhotos.stream()
            .map { i -> i.attachedPhotoId }
            .toList()

        val requestPhotoIds = review.attachedPhotoIds


        val willDeletePhotos = mutableSetOf<String>()
        for (attachedPhotoId in dbPhotoIds) {
            if (!review.attachedPhotoIds.contains(attachedPhotoId)) willDeletePhotos.add(attachedPhotoId)
        }


        val willCreatedPhotos = mutableSetOf<String>()
        for (attachedPhotoId in requestPhotoIds) {
            if (!dbPhotoIds.contains(attachedPhotoId)) willCreatedPhotos.add(attachedPhotoId)
        }


        deleteReviewPhotos(reviewEntity, willDeletePhotos)
        addReviewPhotos(reviewEntity, willCreatedPhotos)
    }

    private fun addReviewPhotos(
        reviewEntity: ReviewEntity,
        addPhotoIds: MutableSet<String>,
    ) {
        if (addPhotoIds.isNotEmpty()) {
            addPhotoIds.stream()
                .map { addPhotoId -> ReviewPhotoEntity(addPhotoId) }
                .forEach { reviewPhotoEntity ->
                    run {
                        reviewEntity.addPhoto(reviewPhotoEntity)
                        reviewPhotoRepository.save(reviewPhotoEntity)
                    }
                }
        }
    }

    private fun deleteReviewPhotos(
        reviewEntity: ReviewEntity,
        deletePhotoIds: MutableSet<String>,
    ) {
        if (deletePhotoIds.isNotEmpty()) {
            reviewEntity.deletePhoto(deletePhotoIds)
            reviewPhotoRepository.deleteAllByPhotoId(deletePhotoIds)
        }
    }

    fun checkFirstReview(review: Review): Boolean {
        val reviewEntity = reviewRepository.findTop1ByPlaceIdOrderByCreatedAtAsc(review.placeId)

        if (reviewEntity.isEmpty())
            return false

        return reviewEntity.first().userId == review.userId
    }
}
