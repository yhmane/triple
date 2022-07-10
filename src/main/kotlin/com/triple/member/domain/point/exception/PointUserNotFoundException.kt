package com.triple.member.domain.point.exception

class PointUserNotFoundException(val userId: String) : RuntimeException() {

    override val message: String by lazy {
        "$userId 를 찾을 수 없습니다"
    }
}
