package com.triple.member.interfaces.point.param

import com.fasterxml.jackson.annotation.JsonIgnore
import com.triple.member.common.validation.EnumValidatorOfHttpRequest
import com.triple.member.domain.point.enums.PointActionType
import com.triple.member.domain.point.enums.PointType
import javax.validation.constraints.NotBlank

data class PointOfHttpRequest(

    @field:EnumValidatorOfHttpRequest(
        enumClass = PointType::class,
        message = "review 만 올 수 있습니다",
    )
    val type: String,

    @field:EnumValidatorOfHttpRequest(
        enumClass = PointActionType::class,
        message = "add | mod | delete 만 올 수 있습니다",
    )
    val action: String,

    @field:NotBlank(message = "reviewId를 입력해주세요")
    val reviewId: String,

    @field:NotBlank(message = "1자 이상의 리뷰를 남겨주세요")
    val content: String,

    val attachedPhotoIds: List<String>? = null,

    @field:NotBlank(message = "userId를 입력해주세요")
    val userId: String,

    @field:NotBlank(message = "placeId를 입력해주세요")
    val placeId: String,
) {

    @get:JsonIgnore
    val pointActionType: PointActionType? by lazy {
        PointActionType.getPointActionType(action)
    }
}
