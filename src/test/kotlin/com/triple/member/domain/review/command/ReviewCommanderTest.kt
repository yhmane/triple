package com.triple.member.domain.review.command

import com.ninjasquad.springmockk.MockkBean
import com.triple.member.domain.point.enums.PointActionType
import com.triple.member.domain.review.Review
import com.triple.member.domain.review.exception.NotRegisteredReviewException
import com.triple.member.infrastructure.review.ReviewEntity
import com.triple.member.infrastructure.review.ReviewPhotoEntity
import com.triple.member.infrastructure.review.ReviewPhotoRepository
import com.triple.member.infrastructure.review.ReviewRepository
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest(classes = [ReviewCommander::class])
class ReviewCommanderTest {

    @Autowired
    private lateinit var reviewCommander: ReviewCommander

    @MockkBean
    private lateinit var reviewRepository: ReviewRepository

    @MockkBean
    private lateinit var reviewPhotoRepository: ReviewPhotoRepository

    @Test
    fun addReview() {
        // Given
        val givenReview = Review(
            reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
            userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            content = "제품이 좋아요",
            placeId = "2e4baf1c-5acb-4efb-a1af-eddada31b00f",
            attachedPhotoIds = mutableListOf<String>("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"),
            pointActionType = PointActionType.ADD,
        )
        every { reviewRepository.findByReviewId(givenReview.reviewId) } returns Optional.empty()
        every { reviewRepository.save(any()) } returns mockk()
        every { reviewPhotoRepository.save(any()) } returns mockk()
        every { reviewRepository.findTop1ByPlaceIdOrderByCreatedAtAsc(any()) } returns listOf(givenReview.convertToReviewEntity())

        // When, Then
        reviewCommander.addReview(givenReview)
    }

    @Test
    fun checkFirstReview() {
        // Given
        val givenReview = Review(
            reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
            userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            content = "제품이 좋아요",
            placeId = "2e4baf1c-5acb-4efb-a1af-eddada31b00f",
            attachedPhotoIds = mutableListOf<String>("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"),
            pointActionType = PointActionType.ADD,
        )
        every { reviewRepository.findTop1ByPlaceIdOrderByCreatedAtAsc(any()) } returns listOf(givenReview.convertToReviewEntity())

        // When
        val firstReview = reviewCommander.checkFirstReview(givenReview)

        // Then
        assertThat(firstReview).isTrue
    }

    @Test
    fun updateReview() {
        // Given
        val givenReview = Review(
            reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
            userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            content = "제품이 좋아요",
            placeId = "2e4baf1c-5acb-4efb-a1af-eddada31b00f",
            attachedPhotoIds = mutableListOf<String>("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"),
            pointActionType = PointActionType.MOD,
        )
        val givenReviewEntity = ReviewEntity(
            reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
            userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            content = "제품이 좋아요",
            placeId = "2e4baf1c-5acb-4efb-a1af-eddada31b00f",
            reviewPhotos = mutableSetOf(ReviewPhotoEntity(attachedPhotoId = "e4d1a64e-a531-46de-88d0-ff0ed70c0bb8"), ReviewPhotoEntity(attachedPhotoId = "a4d1a64e-a531-46de-88d0-ff0ed70c2548"))
        )
        every { reviewRepository.findByReviewId(givenReview.reviewId) } returns Optional.of(givenReviewEntity)
        every { reviewPhotoRepository.deleteAllByPhotoId(any()) } returns mockk()
        every { reviewPhotoRepository.save(any()) } returns mockk()


        // When, Then
        reviewCommander.updateReview(givenReview)
    }

    @Test
    fun updateReviewWithFailure() {
        // Given
        val givenReview = Review(
            reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
            userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            content = "제품이 좋아요",
            placeId = "2e4baf1c-5acb-4efb-a1af-eddada31b00f",
            attachedPhotoIds = mutableListOf<String>("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"),
            pointActionType = PointActionType.MOD,
        )
        val givenReviewId = givenReview.reviewId
        every { reviewRepository.findByReviewId(givenReviewId) } returns Optional.empty()


        // When
        val exception = Assertions.assertThrows(NotRegisteredReviewException::class.java) { reviewCommander.updateReview(givenReview) }

        // Then
        assertThat("등록되지 않은 reviewId 입니다, reviewId: $givenReviewId").isEqualTo(exception.message)
    }

    @Test
    fun deleteReview() {
        // Given
        val givenReview = Review(
            reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
            userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            content = "제품이 좋아요",
            placeId = "2e4baf1c-5acb-4efb-a1af-eddada31b00f",
            attachedPhotoIds = mutableListOf<String>("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"),
            pointActionType = PointActionType.DELETE,
        )
        val givenReviewEntity = ReviewEntity(
            reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
            userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            content = "제품이 좋아요",
            placeId = "2e4baf1c-5acb-4efb-a1af-eddada31b00f",
            reviewPhotos = mutableSetOf(ReviewPhotoEntity(attachedPhotoId = "e4d1a64e-a531-46de-88d0-ff0ed70c0bb8"), ReviewPhotoEntity(attachedPhotoId = "a4d1a64e-a531-46de-88d0-ff0ed70c2548"))
        )
        every { reviewRepository.findByReviewId(givenReview.reviewId) } returns Optional.of(givenReviewEntity)
        every { reviewPhotoRepository.deleteAllByReviewId(givenReview.reviewId) } returns mockk()
        every { reviewRepository.delete(givenReviewEntity) } returns mockk()


        // When, Then
        reviewCommander.deleteReview(givenReview)
    }

    @Test
    fun deleteReviewWithFailure() {
        // Given
        val givenReview = Review(
            reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
            userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            content = "제품이 좋아요",
            placeId = "2e4baf1c-5acb-4efb-a1af-eddada31b00f",
            attachedPhotoIds = mutableListOf<String>("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"),
            pointActionType = PointActionType.DELETE,
        )
        val givenReviewId = givenReview.reviewId
        every { reviewRepository.findByReviewId(givenReviewId) } returns Optional.empty()


        // When
        val exception = Assertions.assertThrows(NotRegisteredReviewException::class.java) { reviewCommander.deleteReview(givenReview) }

        // Then
        assertThat("등록되지 않은 reviewId 입니다, reviewId: $givenReviewId").isEqualTo(exception.message)
    }
}
