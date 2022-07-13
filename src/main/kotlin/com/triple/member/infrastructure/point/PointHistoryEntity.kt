package com.triple.member.infrastructure.point

import com.triple.member.domain.point.enums.PointActionType
import com.triple.member.domain.point.enums.PointHistoryIncreaseType
import com.triple.member.domain.point.enums.PointType
import com.triple.member.infrastructure.BaseTimeEntity
import javax.persistence.*

@Entity
@Table(
    name = "point_history",
    indexes = [
        Index(name = "point_history_index_1", columnList = "review_id, point_type"),
    ]
)
class PointHistoryEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val historyId: Long? = null,

    @Column(name = "review_id")
    val reviewId: String,

    @Column(name = "user_id")
    val userId: String,

    @Column(name = "action_type")
    @Enumerated(EnumType.STRING)
    val actionType: PointActionType = PointActionType.ADD,

    @Column(name = "point_type")
    @Enumerated(EnumType.STRING)
    var pointType: PointType = PointType.REVIEW,

    @Column(name = "increase_type")
    @Enumerated(EnumType.STRING)
    var increaseType: PointHistoryIncreaseType = PointHistoryIncreaseType.PLUS,

    @Column(name = "points")
    val points: Long = 0,
) : BaseTimeEntity()
