package com.triple.member.domain.point.command

import com.ninjasquad.springmockk.MockkBean
import com.triple.member.domain.point.exception.PointUserNotFoundException
import com.triple.member.infrastructure.point.PointEntity
import com.triple.member.infrastructure.point.PointRepository
import io.mockk.every
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

    @Test
    fun `commander - getUserPointWithSuccess`() {
        // Given
        val givenPointId = 1L
        val givenUserId = "triple_user_1"
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
        val givenUserId = "triple_user_12344"
        every { pointRepository.findByUserId(givenUserId) } returns Optional.empty()

        // When
        val exception = assertThrows(PointUserNotFoundException::class.java) { pointCommander.getUserPoint(givenUserId) }

        // Then
        assertThat("$givenUserId 를 찾을 수 없습니다").isEqualTo(exception.message)

    }
}
