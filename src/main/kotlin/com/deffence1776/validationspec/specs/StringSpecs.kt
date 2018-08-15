package com.deffence1776.validationspec.specs

@Deprecated("Just use sepc function")
open class ShouldNotBeBlank<T>(targetFun:T.()->String, fieldNameInMessage: String)
    : FieldValidationSpec<T, String>(
        "com.deffence1776.validationspec.specs.ShouldNotBeBlank"
        ,targetFun,
        { field -> field.isNotBlank() },
        { "$fieldNameInMessage should not be blank." }
)

@Deprecated("Just use sepc function")
open class ShouldBeNumberFormat<T>(targetFun:T.()-> String, fieldNameInMessage: String)
    : FieldValidationSpec<T, String>(
        "com.deffence1776.validationspec.specs.ShouldBeNumberFormat"
        ,targetFun,
        { field -> field.toIntOrNull() != null },
        { "$fieldNameInMessage should be Number." }
)

