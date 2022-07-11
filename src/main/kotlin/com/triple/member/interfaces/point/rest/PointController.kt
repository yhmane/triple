package com.triple.member.interfaces.point.rest

import com.triple.member.application.aggregate.PointReviewAggregate
import com.triple.member.application.point.PointService
import com.triple.member.interfaces.point.param.PointOfHttpRequest
import com.triple.member.interfaces.point.param.PointOfHttpResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class PointController(
    private val pointService: PointService,
    private val pointReviewAggregate: PointReviewAggregate,
) {

    @GetMapping("/points/users/{userId}")
    fun getUserPoint(@PathVariable userId: String): ResponseEntity<PointOfHttpResponse> {
        return ResponseEntity(pointService.getUserPoints(userId), HttpStatus.OK)
    }

    @PostMapping("/events")
    fun executePointEvent(@RequestBody @Valid pointOfHttpRequest: PointOfHttpRequest): ResponseEntity<Any> {
        return ResponseEntity(pointReviewAggregate.executePointEvent(pointOfHttpRequest), HttpStatus.OK)
    }
}
