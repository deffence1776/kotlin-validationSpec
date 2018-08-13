package com.deffence1776.validationSpec.specs


open class ShouldBeGreaterThan<T>(targetFun: T.()->Int, fieldNameInMessage: String, greaterThan: Int)
    : FieldValidationSpec<T, Int>(
        ShouldBeGreaterThan::class.java.name
        ,targetFun
        , { field-> field > greaterThan },
        { "$fieldNameInMessage should be greater than $greaterThan." }
)

open class ShouldBeLessThan<T>(targetFun:T.()-> Int, msgFieldName: String, lessThan: Int)
    : FieldValidationSpec<T, Int>(
        ShouldBeLessThan::class.java.name
        ,targetFun
        , {  field-> field  < lessThan },
        { "$msgFieldName should be  less than $lessThan." }
)