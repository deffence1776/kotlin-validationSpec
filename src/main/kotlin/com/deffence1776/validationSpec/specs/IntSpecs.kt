package com.deffence1776.validationSpec.specs

private val shouldBeGreaterThan = ShouldBeGreaterThan::class.java.name

open class ShouldBeGreaterThan<T>(targetFun: T.()->Int, fieldNameInMessage: String, greaterThan: Int)
    : FieldValidationSpec<T, Int>(
        shouldBeGreaterThan
        ,targetFun
        , { field-> field > greaterThan },
        { "$fieldNameInMessage should be greater than $greaterThan." }
)

private val shouldBeLessThanSpecName = ShouldBeLessThan::class.java.name
open class ShouldBeLessThan<T>(targetFun:T.()-> Int, msgFieldName: String, lessThan: Int)
    : FieldValidationSpec<T, Int>(
        shouldBeLessThanSpecName
        ,targetFun
        , {  field-> field  < lessThan },
        { "$msgFieldName should be  less than $lessThan." }
)