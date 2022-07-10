package com.triple.member.domain.point.vo

import com.triple.member.interfaces.point.param.PointOfHttpResponse

data class PointUserVO(

    val userId: String,
    val points: Long,
) {
    fun convertToPointOfHttpResponse() = PointOfHttpResponse(
        userId = userId,
        points = points,
    )
}
