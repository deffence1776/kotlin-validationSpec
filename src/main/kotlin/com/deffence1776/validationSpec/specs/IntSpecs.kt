package com.deffence1776.validationSpec.specs

open class ShouldBeGreaterThan<T>(aTarget: Int, msgFieldName: String, baseLine: Int)
    : BaseValidationSpec<Int, T>(aTarget
        , { target -> target > baseLine },
        { "$msgFieldName is greater than ${baseLine}." }
)

open class ShouldBeLessThan<T>(aTarget: Int, msgFieldName: String, baseLine: Int)
    : BaseValidationSpec<Int, T>(
        aTarget
        , { target -> target < baseLine },
        { "$msgFieldName is less than ${baseLine}." }
)