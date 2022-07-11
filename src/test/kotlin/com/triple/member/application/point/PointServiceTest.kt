package com.triple.member.application.point

import com.ninjasquad.springmockk.MockkBean
import com.triple.member.domain.point.command.PointCommander
import com.triple.member.domain.point.exception.PointUserNotFoundException
import com.triple.member.domain.point.vo.PointUserVO
import com.triple.member.interfaces.point.param.PointOfHttpRequest
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    classes = [PointService::class]
)
class PointServiceTest {

    @Autowired
    private lateinit var pointService: PointService

    @MockkBean
    private lateinit var pointCommander: PointCommander

    @ParameterizedTest
    @MethodSource("providedPointUser")
    fun `application - getUserPointWithSuccess`(givenUserId: String, givenPoints: Long) {
        // Given
        every { pointCommander.getUserPoint(givenUserId) } returns PointUserVO(
            userId = givenUserId,
            points = givenPoints
        )

        // When
        val expected = pointService.getUserPoints(givenUserId)

        // Then
        assertAll(
            { assertThat(givenUserId).isEqualTo(expected.userId) },
            { assertThat(givenPoints).isEqualTo(expected.points) },
        )
    }

    @Test
    fun `application - getUserPointWithFailure`() {
        // Given
        val givenUserId = "triple_user_12344"
        every { pointCommander.getUserPoint(givenUserId) } throws PointUserNotFoundException(givenUserId)

        // When
        val exception = assertThrows(PointUserNotFoundException::class.java) { pointService.getUserPoints(givenUserId) }

        // Then
        assertThat("$givenUserId 를 찾을 수 없습니다").isEqualTo(exception.message)
    }

    @ParameterizedTest
    @ValueSource(strings = ["ADD", "MOD", "DELETE"])
    fun `application - executePointEvent`(givenPointActionType: String) {
        // Given
        val givenPointOfHttpRequest = PointOfHttpRequest(
            type = "REVIEW",
            action = givenPointActionType,
            reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
            content = "!",
            attachedPhotoIds = listOf("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"),
            userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            placeId = "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
        )
        every { pointCommander.addPoint(givenPointOfHttpRequest) } returns mockk()
        every { pointCommander.updatePoint(givenPointOfHttpRequest) } returns mockk()
        every { pointCommander.deletePoint(givenPointOfHttpRequest) } returns mockk()

        // When, Then
        pointService.executePointEvent(givenPointOfHttpRequest)
    }

    companion object {
        @JvmStatic
        fun providedPointUser() = listOf(
            Arguments.of(
                "triple_user_1", 3L
            ),
            Arguments.of(
                "triple_user_2", 5L
            ),
        )
    }
}
