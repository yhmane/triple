package com.triple.member.interfaces.point.rest

import com.triple.member.application.point.PointService
import com.triple.member.interfaces.point.param.PointOfHttpRequest
import com.triple.member.interfaces.point.param.PointOfHttpResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class PointController(
    private val pointService: PointService
) {

    @GetMapping("/points/users/{userId}")
    fun getUserPoint(@PathVariable userId: String): ResponseEntity<PointOfHttpResponse> {
        return ResponseEntity(pointService.getUserPoints(userId), HttpStatus.OK)
    }

    @PostMapping("/events")
    fun executePointEvent(@RequestBody pointOfHttpRequest: PointOfHttpRequest): ResponseEntity<Any> {
        return ResponseEntity(pointService.executePointEvent(pointOfHttpRequest), HttpStatus.OK)
    }
}
