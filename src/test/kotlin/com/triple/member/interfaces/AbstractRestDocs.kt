package com.triple.member.interfaces

import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import org.springframework.restdocs.request.ParameterDescriptor
import org.springframework.restdocs.request.RequestDocumentation.pathParameters

abstract class AbstractRestDocs(
    private val id: String
) {

    fun restDocument(): RestDocumentationResultHandler =
        if (requestParameterDescriptors().isNotEmpty()) {
            MockMvcRestDocumentation.document(
                id,
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(*requestParameterDescriptors()),
                responseFieldsSnippet(),
            )
        } else {
            MockMvcRestDocumentation.document(
                id,
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(*pathParameters()),
                responseFieldsSnippet(),
            )
        }

    private fun getDocumentRequest(): OperationRequestPreprocessor = Preprocessors.preprocessRequest(
        Preprocessors.modifyUris()
            .scheme("http")
            .host("localhost")
            .port(8080),
        Preprocessors.prettyPrint(),
    )

    private fun getDocumentResponse(): OperationResponsePreprocessor = Preprocessors.preprocessResponse(
        Preprocessors.prettyPrint()
    )

    open fun pathParameters(): Array<ParameterDescriptor> = emptyArray()

    open fun requestParameterDescriptors(): Array<FieldDescriptor> = emptyArray()

    abstract fun responseFieldsSnippet(): ResponseFieldsSnippet
}