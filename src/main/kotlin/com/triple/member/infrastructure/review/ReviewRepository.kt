package com.triple.member.infrastructure.review

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ReviewRepository : JpaRepository<ReviewEntity, String> {

    fun findByReviewId(placeId: String): Optional<ReviewEntity>

    fun findTop1ByPlaceIdOrderByCreatedAtAsc(placeId: String): List<ReviewEntity>
}