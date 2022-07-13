package com.triple.member.domain.point.command

import com.ninjasquad.springmockk.MockkBean
import com.triple.member.domain.point.enums.PointActionType
import com.triple.member.domain.point.enums.PointHistoryIncreaseType
import com.triple.member.domain.point.enums.PointType
import com.triple.member.domain.point.exception.PointUserNotFoundException
import com.triple.member.domain.review.Review
import com.triple.member.infrastructure.point.PointEntity
import com.triple.member.infrastructure.point.PointHistoryEntity
import com.triple.member.infrastructure.point.PointHistoryRepository
import com.triple.member.infrastructure.point.PointRepository
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest(classes = [PointCommander::class])
class PointCommanderTest {

    @Autowired
    private lateinit var pointCommander: PointCommander

    @MockkBean
    private lateinit var pointRepository: PointRepository

    @MockkBean
    private lateinit var pointHistoryRepository: PointHistoryRepository

    @Test
    fun `commander - getUserPointWithSuccess`() {
        // Given
        val givenPointId = 1L
        val givenUserId = "3ede0ef2-92b7-4817-a5f3-0c575361f799"
        val givenPoints = 10L
        every { pointRepository.findByUserId(givenUserId) } returns Optional.of(PointEntity(
            pointId = givenPointId,
            userId = givenUserId,
            points = givenPoints,
        ))

        // When
        val pointUserVO = pointCommander.getUserPoint(givenUserId)

        // Then
        assertAll(
            { assertThat(givenUserId).isEqualTo(pointUserVO.userId) },
            { assertThat(givenPoints).isEqualTo(pointUserVO.points) },
        )
    }

    @Test
    fun `commander - getUserPointWithFailure`() {
        // Given
        val givenUserId = "3ede0ef2-92b7-4817-a5f3-0c575361f799"
        every { pointRepository.findByUserId(givenUserId) } returns Optional.empty()

        // When
        val exception = assertThrows(PointUserNotFoundException::class.java) { pointCommander.getUserPoint(givenUserId) }

        // Then
        assertThat("userId 를 찾을 수 없습니다, userId: $givenUserId").isEqualTo(exception.message)

    }

    @Test
    fun plusPoint() {
        // Given
        val givenReview = Review(
            reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
            userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            content = "제품이 좋아요",
            placeId = "2e4baf1c-5acb-4efb-a1af-eddada31b00f",
            attachedPhotoIds = mutableListOf<String>("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"),
            pointActionType = PointActionType.ADD,
        )
        val givenPointEntity = Optional.of(PointEntity(userId = givenReview.userId))
        val givenFirstReview = true
        every { pointRepository.findByUserId(givenReview.userId) } returns givenPointEntity
        every { pointRepository.save(any()) } returns mockk()
        every { pointHistoryRepository.save(any()) } returns mockk()


        // When, Then
        pointCommander.plusPoint(givenReview, givenFirstReview)
    }

    @Test
    fun `updatePoint - 사진 점수 가점`() {
        // Given
        val givenReview = Review(
            reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
            userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            content = "제품이 좋아요",
            placeId = "2e4baf1c-5acb-4efb-a1af-eddada31b00f",
            attachedPhotoIds = mutableListOf<String>("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"),
            pointActionType = PointActionType.MOD,
        )
        val givenUserId = givenReview.userId
        val givenFirstReview = true
        val givenPointEntity = Optional.of(PointEntity(userId = givenReview.userId))
        val givenPointHistoryEntity = listOf(
            PointHistoryEntity(
                historyId = 1L,
                reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
                userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
                pointType = PointType.PHOTO,
                increaseType = PointHistoryIncreaseType.MINUS
            )
        )
        every { pointRepository.findByUserId(givenUserId) } returns givenPointEntity
        every { pointHistoryRepository.findByReviewIdAndPointType(any(), any(), any()) } returns givenPointHistoryEntity
        every { pointHistoryRepository.save(any()) } returns mockk()


        // When, Then
        pointCommander.updatePoint(givenReview, givenFirstReview)
    }

    @Test
    fun `updatePoint - 사진 점수 회수`() {
        // Given
        val givenReview = Review(
            reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
            userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            content = "제품이 좋아요",
            placeId = "2e4baf1c-5acb-4efb-a1af-eddada31b00f",
            attachedPhotoIds = mutableListOf(),
            pointActionType = PointActionType.MOD,
        )
        val givenUserId = givenReview.userId
        val givenFirstReview = true
        val givenPointEntity = Optional.of(PointEntity(userId = givenReview.userId))
        val givenPointHistoryEntity = listOf(
            PointHistoryEntity(
                historyId = 1L,
                reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
                userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
                pointType = PointType.PHOTO,
                increaseType = PointHistoryIncreaseType.PLUS
            )
        )
        every { pointRepository.findByUserId(givenUserId) } returns givenPointEntity
        every { pointHistoryRepository.findByReviewIdAndPointType(any(), any(), any()) } returns givenPointHistoryEntity
        every { pointHistoryRepository.save(any()) } returns mockk()


        // When, Then
        pointCommander.updatePoint(givenReview, givenFirstReview)
    }

    @Test
    fun `updatePoint - 보너스 점수 가점`() {
        // Given
        val givenReview = Review(
            reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
            userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            content = "제품이 좋아요",
            placeId = "2e4baf1c-5acb-4efb-a1af-eddada31b00f",
            attachedPhotoIds = mutableListOf(),
            pointActionType = PointActionType.MOD,
        )
        val givenUserId = givenReview.userId
        val givenFirstReview = true
        val givenPointEntity = Optional.of(PointEntity(userId = givenReview.userId))
        val givenPointHistoryEntity = listOf(
            PointHistoryEntity(
                historyId = 1L,
                reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
                userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
                pointType = PointType.BONUS,
                increaseType = PointHistoryIncreaseType.MINUS
            )
        )
        every { pointRepository.findByUserId(givenUserId) } returns givenPointEntity
        every { pointHistoryRepository.findByReviewIdAndPointType(any(), any(), any()) } returns givenPointHistoryEntity
        every { pointHistoryRepository.save(any()) } returns mockk()


        // When, Then
        pointCommander.updatePoint(givenReview, givenFirstReview)
    }

    @Test
    fun updatePointWithFailure() {
        // Given
        val givenReview = Review(
            reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
            userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            content = "제품이 좋아요",
            placeId = "2e4baf1c-5acb-4efb-a1af-eddada31b00f",
            attachedPhotoIds = mutableListOf<String>("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"),
            pointActionType = PointActionType.MOD,
        )
        val givenUserId = givenReview.userId
        val givenFirstReview = true
        every { pointRepository.findByUserId(givenUserId) } returns Optional.empty()


        // When
        val exception = assertThrows(PointUserNotFoundException::class.java) {pointCommander.updatePoint(givenReview, givenFirstReview) }

        // Then
        assertThat("userId 를 찾을 수 없습니다, userId: $givenUserId").isEqualTo(exception.message)
    }

    @Test
    fun `minusPoint - 리뷰 점수 회수`() {
        // Given
        val givenReview = Review(
            reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
            userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            content = "제품이 좋아요",
            placeId = "2e4baf1c-5acb-4efb-a1af-eddada31b00f",
            attachedPhotoIds = mutableListOf(),
            pointActionType = PointActionType.DELETE,
        )
        val givenUserId = givenReview.userId
        val givenPointEntity = Optional.of(PointEntity(userId = givenReview.userId))
        val givenPointHistoryEntity = listOf(
            PointHistoryEntity(
                historyId = 1L,
                reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
                userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
                pointType = PointType.REVIEW,
                increaseType = PointHistoryIncreaseType.PLUS
            )
        )
        every { pointRepository.findByUserId(givenUserId) } returns givenPointEntity
        every { pointHistoryRepository.findByReviewIdAndPointType(any(), any(), any()) } returns givenPointHistoryEntity
        every { pointHistoryRepository.save(any()) } returns mockk()


        // When, Then
        pointCommander.minusPoint(givenReview)
    }

    @Test
    fun `minusPoint - 사진 점수 회수`() {
        // Given
        val givenReview = Review(
            reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
            userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            content = "제품이 좋아요",
            placeId = "2e4baf1c-5acb-4efb-a1af-eddada31b00f",
            attachedPhotoIds = mutableListOf(),
            pointActionType = PointActionType.DELETE,
        )
        val givenUserId = givenReview.userId
        val givenPointEntity = Optional.of(PointEntity(userId = givenReview.userId))
        val givenPointHistoryEntity = listOf(
            PointHistoryEntity(
                historyId = 1L,
                reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
                userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
                pointType = PointType.PHOTO,
                increaseType = PointHistoryIncreaseType.PLUS
            )
        )
        every { pointRepository.findByUserId(givenUserId) } returns givenPointEntity
        every { pointHistoryRepository.findByReviewIdAndPointType(any(), any(), any()) } returns givenPointHistoryEntity
        every { pointHistoryRepository.save(any()) } returns mockk()


        // When, Then
        pointCommander.minusPoint(givenReview)
    }

    @Test
    fun `minusPoint - 보너스 점수 회수`() {
        // Given
        val givenReview = Review(
            reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
            userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            content = "제품이 좋아요",
            placeId = "2e4baf1c-5acb-4efb-a1af-eddada31b00f",
            attachedPhotoIds = mutableListOf(),
            pointActionType = PointActionType.DELETE,
        )
        val givenUserId = givenReview.userId
        val givenPointEntity = Optional.of(PointEntity(userId = givenReview.userId))
        val givenPointHistoryEntity = listOf(
            PointHistoryEntity(
                historyId = 1L,
                reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
                userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
                pointType = PointType.BONUS,
                increaseType = PointHistoryIncreaseType.PLUS
            )
        )
        every { pointRepository.findByUserId(givenUserId) } returns givenPointEntity
        every { pointHistoryRepository.findByReviewIdAndPointType(any(), any(), any()) } returns givenPointHistoryEntity
        every { pointHistoryRepository.save(any()) } returns mockk()


        // When, Then
        pointCommander.minusPoint(givenReview)
    }

    @Test
    fun minusPointWithFailure() {
        // Given
        val givenReview = Review(
            reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
            userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            content = "제품이 좋아요",
            placeId = "2e4baf1c-5acb-4efb-a1af-eddada31b00f",
            attachedPhotoIds = mutableListOf<String>("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"),
            pointActionType = PointActionType.DELETE,
        )
        val givenUserId = givenReview.userId
        every { pointRepository.findByUserId(givenUserId) } returns Optional.empty()


        // When
        val exception = assertThrows(PointUserNotFoundException::class.java) {pointCommander.minusPoint(givenReview) }

        // Then
        assertThat("userId 를 찾을 수 없습니다, userId: $givenUserId").isEqualTo(exception.message)
    }
}
