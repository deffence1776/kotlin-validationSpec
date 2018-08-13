package com.deffence1776.validationSpec

private val shouldNotBeBlank = ShouldNotBeBlank::class.java.name

open class ShouldNotBeBlank<T>(targetFun:T.()->String, msgFieldName: String)
    : FieldValidationSpec<T,String>(
        shouldNotBeBlank
        ,targetFun,
        { field -> field.isNotBlank() },
        { "$msgFieldName should not be blank." }
)

private val shouldBeNumberFormat = ShouldBeNumberFormat::class.java.name

open class ShouldBeNumberFormat<T>(targetFun:T.()-> String, msgFieldName: String)
    : FieldValidationSpec<T,String>(
        shouldBeNumberFormat
        ,targetFun,
        { field -> field.toIntOrNull() != null },
        { "$msgFieldName should be Number." }
)

