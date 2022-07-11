package com.triple.member.domain.point.enums

enum class PointActionType {

    ADD,
    MOD,
    DELETE,
    ;

    companion object {
        fun getPointActionType(type: String) = try {
            valueOf(type)
        } catch (e: IllegalArgumentException) {
            null
        } catch (e: NullPointerException) {
            null
        }
    }
}
