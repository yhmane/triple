package com.triple.member.application.point

import com.triple.member.domain.point.command.PointCommander
import com.triple.member.interfaces.point.param.PointOfHttpRequest
import com.triple.member.interfaces.point.param.PointOfHttpResponse
import org.springframework.stereotype.Component

@Component
class PointService(
    private val pointCommander: PointCommander
) {

    fun getUserPoints(userId: String): PointOfHttpResponse {
        return pointCommander.getUserPoint(userId)
            .convertToPointOfHttpResponse()
    }

    fun executePointEvent(pointOfHttpRequest: PointOfHttpRequest) {
        // TODO 포인트 이벤트 처리 business 개발
    }
}
