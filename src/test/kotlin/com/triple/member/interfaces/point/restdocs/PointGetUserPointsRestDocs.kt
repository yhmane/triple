package com.triple.member.interfaces.point.restdocs

import com.triple.member.interfaces.AbstractRestDocs
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.JsonFieldType.NUMBER
import org.springframework.restdocs.payload.JsonFieldType.STRING
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName

object PointGetUserPointsRestDocs : AbstractRestDocs("getUserPoints") {

    override fun pathParameters() = arrayOf(
        parameterWithName("userId").description("userId")
    )

    override fun responseFieldsSnippet(): ResponseFieldsSnippet =
        responseFields(*userPointsResponseFields())

    private fun userPointsResponseFields(): Array<FieldDescriptor> =
        arrayOf(
            fieldWithPath("userId").type(STRING).description("사용자 id를 말한다"),
            fieldWithPath("points").type(NUMBER).description("사용자의 합계 포인트를 말한다")
        )
}
