package com.triple.member.infrastructure.review

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ReviewPhotoRepository : JpaRepository<ReviewPhotoEntity, String> {

    @Modifying
    @Query(
        """
            delete from ReviewPhotoEntity rp
            where rp.attachedPhotoId in :attachedPhotoId
        """
    )
    fun deleteAllByPhotoId(@Param("attachedPhotoId") attachedPhotoId: Set<String>)

    @Modifying
    @Query(
        """
            delete from ReviewPhotoEntity rp
            where rp.review.reviewId = :reviewId
        """
    )
    fun deleteAllByReviewId(@Param("reviewId") reviewId: String)
}
