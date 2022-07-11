package com.triple.member.common.validation

import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD,)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [EnumValidatorOfHttpRequest.ValueOfEnumValidator::class])
annotation class EnumValidatorOfHttpRequest(

    val enumClass: KClass<out Enum<*>>,
    val message: String = "message를 overriding해서 사용 해주세요",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
) {
    class ValueOfEnumValidator : ConstraintValidator<EnumValidatorOfHttpRequest, String> {
        private val enumValues: MutableList<String> = mutableListOf()

        override fun initialize(constraintAnnotation: EnumValidatorOfHttpRequest) {
            super.initialize(constraintAnnotation)

            enumValues.addAll(constraintAnnotation.enumClass.java
                .enumConstants
                .map { it.name }
            )
        }

        override fun isValid(value: String, context: ConstraintValidatorContext): Boolean =
            if (value == null)
                true
            else
                enumValues.contains(value.uppercase())
    }
}
