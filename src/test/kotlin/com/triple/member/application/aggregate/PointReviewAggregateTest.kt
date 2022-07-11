package com.triple.member.application.aggregate

import com.ninjasquad.springmockk.MockkBean
import com.triple.member.domain.point.command.PointCommander
import com.triple.member.domain.review.command.ReviewCommander
import com.triple.member.domain.review.exception.AlreadyRegisteredReviewException
import com.triple.member.domain.review.exception.NotRegisteredReviewException
import com.triple.member.interfaces.point.param.PointOfHttpRequest
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    classes = [PointReviewAggregate::class]
)
class PointReviewAggregateTest {

    @Autowired
    private lateinit var pointReviewAggregate: PointReviewAggregate

    @MockkBean
    private lateinit var pointCommander: PointCommander

    @MockkBean
    private lateinit var reviewCommander: ReviewCommander

    @Test
    fun `executePointEvent add review `() {
        // Given
        val givenPointOfHttpRequest = PointOfHttpRequest(
            type = "REVIEW",
            action = "ADD",
            reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
            content = "!",
            attachedPhotoIds = mutableListOf("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"),
            userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            placeId = "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
        )
        val givenReview = givenPointOfHttpRequest.convertToReview()
        val firstReview = true
        every { reviewCommander.addReview(givenReview) } returns mockk()
        every { reviewCommander.checkFirstReview(givenReview) } returns firstReview
        every { pointCommander.plusPoint(givenReview, firstReview) } returns mockk()

        // When, Then
        pointReviewAggregate.executePointEvent(givenPointOfHttpRequest)
    }

    @Test
    fun `executePointEvent add review failure by reviewId Conflict`() {
        // Given
        val givenPointOfHttpRequest = PointOfHttpRequest(
            type = "REVIEW",
            action = "ADD",
            reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
            content = "!",
            attachedPhotoIds = mutableListOf("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"),
            userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            placeId = "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
        )
        val givenReviewId = givenPointOfHttpRequest.reviewId
        every { reviewCommander.addReview(givenPointOfHttpRequest.convertToReview()) } throws
                AlreadyRegisteredReviewException(givenReviewId)

        // When
        val exception = Assertions.assertThrows(AlreadyRegisteredReviewException::class.java)
        { pointReviewAggregate.executePointEvent(givenPointOfHttpRequest) }

        // Then
        assertThat("이미 등록된 reviewId 입니다, reviewId: $givenReviewId").isEqualTo(exception.message)
    }

    @Test
    fun `executePointEvent update review`() {
        // Given
        val givenPointOfHttpRequest = PointOfHttpRequest(
            type = "REVIEW",
            action = "MOD",
            reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
            content = "!",
            attachedPhotoIds = mutableListOf("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"),
            userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            placeId = "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
        )
        val givenReview = givenPointOfHttpRequest.convertToReview()
        val givenFirstReview = true
        every { reviewCommander.updateReview(givenReview) } returns mockk()
        every { reviewCommander.checkFirstReview(any()) } returns givenFirstReview
        every { pointCommander.updatePoint(givenReview, givenFirstReview ) } returns mockk()

        // When, Then
        pointReviewAggregate.executePointEvent(givenPointOfHttpRequest)
    }

    @Test
    fun `executePointEvent update review failure by reviewId Not Found`() {
        // Given
        val givenPointOfHttpRequest = PointOfHttpRequest(
            type = "REVIEW",
            action = "MOD",
            reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
            content = "!",
            attachedPhotoIds = mutableListOf("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"),
            userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            placeId = "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
        )
        val givenReviewId = givenPointOfHttpRequest.reviewId
        every { reviewCommander.updateReview(givenPointOfHttpRequest.convertToReview()) } throws
                NotRegisteredReviewException(givenReviewId)

        // When
        val exception = Assertions.assertThrows(NotRegisteredReviewException::class.java)
        { pointReviewAggregate.executePointEvent(givenPointOfHttpRequest) }

        // Then
        assertThat("등록되지 않은 reviewId 입니다, reviewId: $givenReviewId").isEqualTo(exception.message)
    }

    @Test
    fun `executePointEvent delete review`() {
        // Given
        val givenPointOfHttpRequest = PointOfHttpRequest(
            type = "REVIEW",
            action = "DELETE",
            reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
            content = "!",
            attachedPhotoIds = mutableListOf("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"),
            userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            placeId = "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
        )
        val givenReview = givenPointOfHttpRequest.convertToReview()
        val givenFirstReview = true
        every { reviewCommander.deleteReview(givenReview) } returns mockk()
        every { reviewCommander.checkFirstReview(givenReview) } returns givenFirstReview
        every { pointCommander.minusPoint(givenReview, givenFirstReview) } returns mockk()

        // When, Then
        pointReviewAggregate.executePointEvent(givenPointOfHttpRequest)
    }

    @Test
    fun `executePointEvent delete review failure by reviewId Not Found`() {
        // Given
        val givenPointOfHttpRequest = PointOfHttpRequest(
            type = "REVIEW",
            action = "DELETE",
            reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
            content = "!",
            attachedPhotoIds = mutableListOf("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"),
            userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            placeId = "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
        )
        val givenReviewId = givenPointOfHttpRequest.reviewId
        every { reviewCommander.deleteReview(givenPointOfHttpRequest.convertToReview()) } throws
                NotRegisteredReviewException(givenReviewId)

        // When
        val exception = Assertions.assertThrows(NotRegisteredReviewException::class.java)
        { pointReviewAggregate.executePointEvent(givenPointOfHttpRequest) }

        // Then
        assertThat("등록되지 않은 reviewId 입니다, reviewId: $givenReviewId").isEqualTo(exception.message)
    }
}
