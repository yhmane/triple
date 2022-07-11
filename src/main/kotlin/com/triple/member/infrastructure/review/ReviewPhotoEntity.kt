package com.triple.member.infrastructure.review

import com.triple.member.infrastructure.BaseTimeEntity
import javax.persistence.*

@Table(name = "review_photo")
@Entity
class ReviewPhotoEntity(

    @Id
    val attachedPhotoId: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    var review: ReviewEntity? = null,
) : BaseTimeEntity()
