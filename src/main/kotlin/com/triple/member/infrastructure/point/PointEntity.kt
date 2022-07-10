package com.triple.member.infrastructure.point

import com.triple.member.domain.point.vo.PointUserVO
import com.triple.member.infrastructure.BaseTimeEntity
import org.hibernate.annotations.ColumnDefault
import javax.persistence.*

@Table(name = "point")
@Entity
class PointEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val pointId: Long,

    val userId: String,

    @ColumnDefault("0")
    var points: Long,
) : BaseTimeEntity() {

    fun convertToPointUserVO() = PointUserVO(
        userId = userId,
        points = points,
    )
}
