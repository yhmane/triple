package com.triple.member.application.point

import com.ninjasquad.springmockk.MockkBean
import com.triple.member.domain.point.command.PointCommander
import com.triple.member.domain.point.exception.PointUserNotFoundException
import com.triple.member.domain.point.vo.PointUserVO
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
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
