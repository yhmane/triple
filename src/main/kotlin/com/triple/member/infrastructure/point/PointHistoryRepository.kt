package com.triple.member.infrastructure.point

import com.triple.member.domain.point.enums.PointType
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PointHistoryRepository : JpaRepository<PointHistoryEntity, Long> {

    @Query("""
        select pr
        from PointHistoryEntity pr
        where pr.reviewId = :reviewId
        and pr.pointType = :pointType
        ORDER BY pr.historyId
    """)
    fun findByReviewIdAndPointType(
        @Param("reviewId") reviewId: String,
        @Param("pointType") pointType: PointType,
        pageable: Pageable,
    ): List<PointHistoryEntity>
}
