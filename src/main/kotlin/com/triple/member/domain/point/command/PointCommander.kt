package com.triple.member.domain.point.command

import com.triple.member.domain.point.exception.PointUserNotFoundException
import com.triple.member.domain.point.vo.PointUserVO
import com.triple.member.infrastructure.point.PointRepository
import com.triple.member.interfaces.point.param.PointOfHttpRequest
import org.springframework.stereotype.Component

@Component
class PointCommander(
    private val pointRepository: PointRepository
) {
    fun getUserPoint(userId: String): PointUserVO = pointRepository.findByUserId(userId)
        .orElseThrow { throw PointUserNotFoundException(userId) }
        .convertToPointUserVO()

    fun addPoint(pointOfHttpRequest: PointOfHttpRequest) {
        // TODO
    }

    fun updatePoint(pointOfHttpRequest: PointOfHttpRequest) {
        // TODO
    }

    fun deletePoint(pointOfHttpRequest: PointOfHttpRequest) {
        // TODO
    }
}
