package com.triple.member.common.handler

import com.triple.member.domain.review.exception.AlreadyRegisteredReviewException
import com.triple.member.domain.point.exception.PointUserNotFoundException
import com.triple.member.domain.review.exception.NotRegisteredReviewException
import com.triple.member.interfaces.point.param.PointOfErrorResponse
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.function.Consumer

private val log = KotlinLogging.logger {}

@RestControllerAdvice
class PointExceptionHandler {

    @ExceptionHandler(PointUserNotFoundException::class)
    fun handlePointUserNotFoundException(e: PointUserNotFoundException): ResponseEntity<PointOfErrorResponse> {
        log.error { "$e.message" }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(PointOfErrorResponse(e.message))
    }

    @ExceptionHandler(AlreadyRegisteredReviewException::class)
    fun handleAlreadyRegisteredReviewException(e: AlreadyRegisteredReviewException): ResponseEntity<PointOfErrorResponse> {
        log.error { "$e.message" }

        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(PointOfErrorResponse(e.message))
    }

    @ExceptionHandler(NotRegisteredReviewException::class)
    fun handleNotRegisteredReviewException(e: NotRegisteredReviewException): ResponseEntity<PointOfErrorResponse> {
        log.error { "$e.message" }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(PointOfErrorResponse(e.message))
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): Map<String, Any?> {
        val sb = StringBuilder()

        val errors: MutableMap<String, Any?> = HashMap()
        ex.bindingResult.allErrors.forEach(Consumer { error: ObjectError ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.getDefaultMessage()
            errors[fieldName] = "field: $fieldName, message: $errorMessage"
            sb.append(errors[fieldName]).append(" :: ")
        })
        errors["error"] = HttpStatus.BAD_REQUEST.reasonPhrase
        errors["status"] = HttpStatus.BAD_REQUEST.value()

        log.error { "$sb" }
        return errors
    }
}
