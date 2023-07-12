package com.typ.muslim.core

annotation class Buggy(val reason: String = "")

annotation class NeedsTesting(val reason: String = "")

annotation class BackwardCompatible(val reason: String = "")

annotation class NotBackwardCompatible(val reason: String = "")

@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER
)
annotation class HelperMethod
