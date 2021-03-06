package com.triple.member.application.point

import com.triple.member.domain.point.command.PointCommander
import com.triple.member.interfaces.point.param.PointOfHttpResponse
import org.springframework.stereotype.Service

@Service
class PointService(
    private val pointCommander: PointCommander
) {

    fun getUserPoints(userId: String): PointOfHttpResponse {
        return pointCommander.getUserPoint(userId)
            .convertToPointOfHttpResponse()
    }
}
