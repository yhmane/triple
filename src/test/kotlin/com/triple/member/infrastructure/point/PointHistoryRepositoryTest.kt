package com.triple.member.infrastructure.point

import com.triple.member.domain.point.enums.PointActionType
import com.triple.member.domain.point.enums.PointHistoryIncreaseType
import com.triple.member.domain.point.enums.PointType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PointHistoryRepositoryTest {

    @Autowired
    private lateinit var pointHistoryRepository: PointHistoryRepository

    @Test
    fun test() {
        // Given
        val givenHistoryId = 2L
        val givenReviewId = "240a0658-dc5f-4878-9381-ebb7b2667772"
        val givenUserId = "3ede0ef2-92b7-4817-a5f3-0c575361f799"
        val givenActionType = PointActionType.ADD
        var givenPointType = PointType.PHOTO
        var givenIncreaseType = PointHistoryIncreaseType.PLUS
        val givenPoints = 2L
        val givenCreatedAt = LocalDateTime.now().minusHours(1L)
        val givenUpdatedAt = LocalDateTime.now().minusHours(1L)


        // When
        val pointHistoryEntity = pointHistoryRepository.findByReviewIdAndPointType(
            reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
            pointType = PointType.PHOTO,
            pageable = Pageable.ofSize(1)
        ).first()


        // Then
        assertAll(
            { assertThat(givenHistoryId).isEqualTo(pointHistoryEntity.historyId) },
            { assertThat(givenReviewId).isEqualTo(pointHistoryEntity.reviewId) },
            { assertThat(givenUserId).isEqualTo(pointHistoryEntity.userId) },
            { assertThat(givenActionType).isEqualTo(pointHistoryEntity.actionType) },
            { assertThat(givenPointType).isEqualTo(pointHistoryEntity.pointType) },
            { assertThat(givenIncreaseType).isEqualTo(pointHistoryEntity.increaseType) },
            { assertThat(givenPoints).isEqualTo(pointHistoryEntity.points) },
            { assertThat(givenCreatedAt).isBefore(pointHistoryEntity.createdAt) },
            { assertThat(givenUpdatedAt).isBefore(pointHistoryEntity.updatedAt) },
        )
    }
}