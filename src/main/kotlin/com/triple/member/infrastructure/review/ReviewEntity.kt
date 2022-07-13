package com.triple.member.infrastructure.review

import com.triple.member.domain.review.Review
import com.triple.member.infrastructure.BaseTimeEntity
import java.util.stream.Collectors
import javax.persistence.*
import kotlin.streams.toList

@Entity
@Table(
    name = "review",
    indexes = [
        Index(name = "review_index_1", columnList = "place_id, created_at"),
    ]
)
class ReviewEntity(

    @Id
    @Column(name = "review_id")
    val reviewId: String,

    @Column(name = "user_id")
    val userId: String,

    @Column(name = "content")
    var content: String,

    @Column(name = "place_id")
    val placeId: String,

    @OneToMany(
        mappedBy = "review",
        fetch = FetchType.LAZY
    )
    var reviewPhotos: MutableSet<ReviewPhotoEntity> = mutableSetOf(),

    ) : BaseTimeEntity() {

    fun addPhoto(reviewPhotoEntity: ReviewPhotoEntity) {
        reviewPhotos.add(reviewPhotoEntity)
        reviewPhotoEntity.review = this
    }

    fun deletePhoto(deletePhotoIds: MutableSet<String>) {
        for (deletePhotoId in deletePhotoIds) {
            reviewPhotos.removeIf { i -> i.attachedPhotoId == deletePhotoId}
        }
    }

    fun updateReview(review: Review) {
        content = review.content
    }
}
