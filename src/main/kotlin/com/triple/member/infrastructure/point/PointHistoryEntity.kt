package com.triple.member.infrastructure.point

import com.triple.member.domain.point.enums.PointActionType
import com.triple.member.domain.point.enums.PointHistoryIncreaseType
import com.triple.member.domain.point.enums.PointType
import com.triple.member.infrastructure.BaseTimeEntity
import javax.persistence.*

@Table(name = "point_history")
@Entity
class PointHistoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val historyId: Long? = null,

    val reviewId: String,

    val userId: String,

    @Enumerated(EnumType.STRING)
    val actionType: PointActionType = PointActionType.ADD,

    @Enumerated(EnumType.STRING)
    var pointType: PointType = PointType.REVIEW,

    @Enumerated(EnumType.STRING)
    var increaseType: PointHistoryIncreaseType = PointHistoryIncreaseType.PLUS,

    val points: Long = 0,
) : BaseTimeEntity()
