package com.deffence1776.validationSpec

private val shouldBeGreaterThan = ShouldBeGreaterThan::class.java.name

open class ShouldBeGreaterThan<T>(targetFun: T.()->Int, msgFieldName: String, baseLine: Int)
    : FieldValidationSpec<T, Int>(
        shouldBeGreaterThan
        ,targetFun
        , { field-> field > baseLine },
        { "$msgFieldName is greater than $baseLine." }
)

private val shouldBeLessThanSpecName = ShouldBeLessThan::class.java.name
open class ShouldBeLessThan<T>(targetFun:T.()-> Int, msgFieldName: String, baseLine: Int)
    : FieldValidationSpec<T, Int>(
        shouldBeLessThanSpecName
        ,targetFun
        , {  field-> field  < baseLine },
        { "$msgFieldName is less than $baseLine." }
)