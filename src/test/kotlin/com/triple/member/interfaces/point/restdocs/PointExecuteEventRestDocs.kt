package com.triple.member.interfaces.point.restdocs

import com.triple.member.interfaces.AbstractRestDocs
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.ResponseFieldsSnippet

object PointExecuteEventRestDocs : AbstractRestDocs("executePointEvent") {

    override fun requestParameterDescriptors() = arrayOf(
        fieldWithPath("type").description("type값으로 현재는 REVIEW만 존재한다"),
        fieldWithPath("action").description("action값으로 ADD (추가)| MOD (수정)| DEL (삭제) 3가지가 존재한다"),
        fieldWithPath("reviewId").description("reviewId 이다"),
        fieldWithPath("content").description("리뷰 내용을 가리킨다"),
        fieldWithPath("attachedPhotoIds").description("첨부된 사진들의 id들이다"),
        fieldWithPath("userId").description("userId"),
        fieldWithPath("placeId").description("placeId 이다"),
    )

    override fun responseFieldsSnippet(): ResponseFieldsSnippet =
        PayloadDocumentation.responseFields()
}