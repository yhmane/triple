package com.triple.member.infrastructure.point

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PointRepository : CrudRepository<PointEntity, Long> {

    fun findByUserId(userId: String): Optional<PointEntity>
}
