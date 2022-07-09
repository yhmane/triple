package com.triple.member.interfaces.point.param

data class PointOfHttpRequest(

    val type: String,
    val action: String,
    val reviewId: String,
    val content: String,
    val attachedPhotoIds: List<String>? = null,
    val userId: String,
    val placeId: String,
)
