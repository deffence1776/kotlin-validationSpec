package com.deffence1776.validationSpec.specs

open class ShouldNotBeBlank<T>(targetFun:T.()->String, fieldNameInMessage: String)
    : FieldValidationSpec<T, String>(
        "com.deffence1776.validationSpec.specs.ShouldNotBeBlank"
        ,targetFun,
        { field -> field.isNotBlank() },
        { "$fieldNameInMessage should not be blank." }
)

open class ShouldBeNumberFormat<T>(targetFun:T.()-> String, fieldNameInMessage: String)
    : FieldValidationSpec<T, String>(
        "com.deffence1776.validationSpec.specs.ShouldBeNumberFormat"
        ,targetFun,
        { field -> field.toIntOrNull() != null },
        { "$fieldNameInMessage should be Number." }
)

