package com.deffence1776.validationSpec.specs

private val shouldNotBeBlank = ShouldNotBeBlank::class.java.name

open class ShouldNotBeBlank<T>(targetFun:T.()->String, fieldNameInMessage: String)
    : FieldValidationSpec<T, String>(
        shouldNotBeBlank
        ,targetFun,
        { field -> field.isNotBlank() },
        { "$fieldNameInMessage should not be blank." }
)

private val shouldBeNumberFormat = ShouldBeNumberFormat::class.java.name
open class ShouldBeNumberFormat<T>(targetFun:T.()-> String, fieldNameInMessage: String)
    : FieldValidationSpec<T, String>(
        shouldBeNumberFormat
        ,targetFun,
        { field -> field.toIntOrNull() != null },
        { "$fieldNameInMessage should be Number." }
)

