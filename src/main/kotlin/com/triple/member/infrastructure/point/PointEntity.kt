package com.triple.member.infrastructure.point

import com.triple.member.domain.point.vo.PointUserVO
import com.triple.member.infrastructure.BaseTimeEntity
import javax.persistence.*

@Table(name = "point")
@Entity
class PointEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val pointId: Long? = null,

    @Column(name = "user_id")
    val userId: String,

    @Column(name = "points")
    var points: Long = 0,
) : BaseTimeEntity() {

    fun convertToPointUserVO() = PointUserVO(
        userId = userId,
        points = points,
    )
}
