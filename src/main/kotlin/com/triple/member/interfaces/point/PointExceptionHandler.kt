package com.triple.member.interfaces.point

import com.triple.member.domain.point.exception.PointUserNotFoundException
import com.triple.member.interfaces.point.param.PointOfErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class PointExceptionHandler {

    @ExceptionHandler(PointUserNotFoundException::class)
    fun handlePointUserNotFoundException(e: PointUserNotFoundException): ResponseEntity<PointOfErrorResponse> =
        ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(PointOfErrorResponse(e.message))
}
