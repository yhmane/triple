package com.triple.member.interfaces.point.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.triple.member.application.aggregate.PointReviewAggregate
import com.triple.member.application.point.PointService
import com.triple.member.domain.point.exception.PointUserNotFoundException
import com.triple.member.domain.review.exception.AlreadyRegisteredReviewException
import com.triple.member.domain.review.exception.NotRegisteredReviewException
import com.triple.member.interfaces.point.param.PointOfHttpRequest
import com.triple.member.interfaces.point.param.PointOfHttpResponse
import com.triple.member.interfaces.point.restdocs.PointExecuteEventRestDocs
import com.triple.member.interfaces.point.restdocs.PointGetUserPointsRestDocs
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

@ExtendWith(RestDocumentationExtension::class)
@WebMvcTest(PointController::class)
@AutoConfigureRestDocs
class PointControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var pointService: PointService

    @MockkBean
    private lateinit var pointReviewAggregate: PointReviewAggregate

    @Test
    fun `GET user-point 200 OK`() {
        // Given
        val givenUserId = "3ede0ef2-92b7-4817-a5f3-0c575361f745"
        val expectedResponse = PointOfHttpResponse(
            userId = givenUserId,
            points = 3L,
        )
        every { pointService.getUserPoints(givenUserId) } returns expectedResponse

        // When
        val endPoint = "/points/users/{userId}"
        val resultActions: ResultActions = mockMvc.perform(RestDocumentationRequestBuilders.get(endPoint, givenUserId))
            .andDo(MockMvcResultHandlers.print())

        // Then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.userId").value(expectedResponse.userId))
            .andExpect(jsonPath("$.points").value(expectedResponse.points))
            .andDo(PointGetUserPointsRestDocs.restDocument())
    }

    @Test
    fun `GET user-point 404 not-found`() {
        // Given
        val givenUserId = "3ede0ef2-92b7-4817-a5f3-0c575322"
        every { pointService.getUserPoints(givenUserId) } throws PointUserNotFoundException(givenUserId)

        // When
        val endPoint = "/points/users/{userId}"
        val resultActions: ResultActions = mockMvc.perform(MockMvcRequestBuilders.get(endPoint, givenUserId))
            .andDo(MockMvcResultHandlers.print())

        // Then
        resultActions.andExpect(MockMvcResultMatchers.status().is4xxClientError)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `POST events 200 OK`() {
        // Given
        val givenPointOfHttpRequest = PointOfHttpRequest(
            type = "REVIEW",
            action = "ADD",
            reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
            content = "!",
            attachedPhotoIds = mutableListOf("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"),
            userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            placeId = "2e4baf1c-5acb-4efb-a1af-eddada31b00f",
        )

        every { pointReviewAggregate.executePointEvent(givenPointOfHttpRequest) } returns mockk()

        // When
        val endPoint = "/events"
        val resultActions: ResultActions = mockMvc.perform(RestDocumentationRequestBuilders.post(endPoint)
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(givenPointOfHttpRequest))
        )
            .andDo(PointExecuteEventRestDocs.restDocument())
            .andDo(MockMvcResultHandlers.print())

        // Then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `POST events 400 BadRequest`() {
        // Given
        val givenPointOfHttpRequest = PointOfHttpRequest(
            type = "REVIEW1",
            action = "ADD22",
            reviewId = "",
            content = "",
            attachedPhotoIds = null,
            userId = "",
            placeId = "",
        )

        // When
        val endPoint = "/events"
        val resultActions: ResultActions = mockMvc.perform(MockMvcRequestBuilders.post(endPoint)
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(givenPointOfHttpRequest))
        )
            .andDo(MockMvcResultHandlers.print())

        // Then
        resultActions.andExpect(MockMvcResultMatchers.status().is4xxClientError)
            .andExpect(jsonPath("$.userId").value("field: userId, message: userId를 입력해주세요"))
            .andExpect(jsonPath("$.placeId").value("field: placeId, message: placeId를 입력해주세요"))
            .andExpect(jsonPath("$.action").value("field: action, message: add | mod | delete 만 올 수 있습니다"))
            .andExpect(jsonPath("$.type").value("field: type, message: review 만 올 수 있습니다"))
            .andExpect(jsonPath("$.reviewId").value("field: reviewId, message: reviewId를 입력해주세요"))
            .andExpect(jsonPath("$.content").value("field: content, message: 1자 이상의 리뷰를 남겨주세요"))
    }

    @Test
    fun `POST events add 409 NotRegisteredReviewException`() {
        // Given
        val givenPointOfHttpRequest = PointOfHttpRequest(
            type = "REVIEW",
            action = "ADD",
            reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
            content = "!",
            attachedPhotoIds = mutableListOf("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"),
            userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            placeId = "2e4baf1c-5acb-4efb-a1af-eddada31b00f",
        )
        val givenReviewId = givenPointOfHttpRequest.reviewId
        every { pointReviewAggregate.executePointEvent(givenPointOfHttpRequest) } throws NotRegisteredReviewException(givenReviewId)

        // When
        val endPoint = "/events"
        val resultActions: ResultActions = mockMvc.perform(MockMvcRequestBuilders.post(endPoint)
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(givenPointOfHttpRequest))
        )
            .andDo(MockMvcResultHandlers.print())


        // Then
        resultActions.andExpect(MockMvcResultMatchers.status().is4xxClientError)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `POST events update 409 AlreadyRegisteredReviewException`() {
        // Given
        val givenPointOfHttpRequest = PointOfHttpRequest(
            type = "REVIEW",
            action = "ADD",
            reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772",
            content = "!",
            attachedPhotoIds = mutableListOf("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"),
            userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            placeId = "2e4baf1c-5acb-4efb-a1af-eddada31b00f",
        )
        val givenReviewId = givenPointOfHttpRequest.reviewId
        every { pointReviewAggregate.executePointEvent(givenPointOfHttpRequest) } throws AlreadyRegisteredReviewException(givenReviewId)

        // When
        val endPoint = "/events"
        val resultActions: ResultActions = mockMvc.perform(MockMvcRequestBuilders.post(endPoint)
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(givenPointOfHttpRequest))
        )
            .andDo(MockMvcResultHandlers.print())


        // Then
        resultActions.andExpect(MockMvcResultMatchers.status().is4xxClientError)
            .andDo(MockMvcResultHandlers.print())
    }
}
