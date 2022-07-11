package com.triple.member.infrastructure.point

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.time.LocalDateTime

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PointRepositoryTest {

    @Autowired
    private lateinit var pointRepository: PointRepository

    @Test
    fun test() {
        // Given
        val givenPointId = 1L
        val givenUserId = "3ede0ef2-92b7-4817-a5f3-0c575361f745"
        val givenPoints = 3L
        val givenCreatedAt = LocalDateTime.now().minusHours(1L)
        val givenUpdatedAt = LocalDateTime.now().minusHours(1L)

        // When
        val pointEntity = pointRepository.findByUserId("3ede0ef2-92b7-4817-a5f3-0c575361f745")
            .get()

        // Then
        assertAll(
            { assertThat(givenPointId).isEqualTo(pointEntity.pointId) },
            { assertThat(givenUserId).isEqualTo(pointEntity.userId) },
            { assertThat(givenPoints).isEqualTo(pointEntity.points) },
            { assertThat(givenCreatedAt).isBefore(pointEntity.createdAt) },
            { assertThat(givenUpdatedAt).isBefore(pointEntity.updatedAt) },
        )
    }
}
