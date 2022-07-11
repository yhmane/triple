package com.triple.member.infrastructure.review

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.time.LocalDateTime

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReviewRepositoryTest {

    @Autowired
    private lateinit var reviewRepository: ReviewRepository

    @Test
    fun test() {
        // Given
        val givenReviewId = "240a0658-dc5f-4878-9381-ebb7b2667772"
        val givenUserId = "3ede0ef2-92b7-4817-a5f3-0c575361f745"
        val givenContent = "좋아요!!"
        val givenPlaceId = "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
        val givenCreatedAt = LocalDateTime.now().minusHours(1L)
        val givenUpdatedAt = LocalDateTime.now().minusHours(1L)


        // When
        val reviewEntity = reviewRepository.findByReviewId(givenReviewId)
            .get()

        // Then
        assertAll(
            { assertThat(givenReviewId).isEqualTo(reviewEntity.reviewId) },
            { assertThat(givenUserId).isEqualTo(reviewEntity.userId) },
            { assertThat(givenContent).isEqualTo(reviewEntity.content) },
            { assertThat(givenPlaceId).isEqualTo(reviewEntity.placeId) },
            { assertThat(givenCreatedAt).isBefore(reviewEntity.createdAt) },
            { assertThat(givenUpdatedAt).isBefore(reviewEntity.updatedAt) },
        )
    }
}
