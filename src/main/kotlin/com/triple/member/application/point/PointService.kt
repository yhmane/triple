package com.triple.member.application.point

import com.triple.member.interfaces.point.param.PointOfHttpRequest
import com.triple.member.interfaces.point.param.PointOfHttpResponse
import org.springframework.stereotype.Component

@Component
class PointService {

    fun getUserPoints(userId: String): PointOfHttpResponse {
        // TODO 포인트 조회 business 개발 (현재 mocking)
        return PointOfHttpResponse(
            userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            points = 3L,
        )
    }

    fun executePointEvent(pointOfHttpRequest: PointOfHttpRequest) {
        // TODO 포인트 이벤트 처리 business 개발
    }
}
