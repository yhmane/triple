package com.triple.member.interfaces.rest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class PointController {

    @GetMapping("/points/users/{userId}")
    fun getUserPoint(@PathVariable userId: String): String = "yunho"
}
