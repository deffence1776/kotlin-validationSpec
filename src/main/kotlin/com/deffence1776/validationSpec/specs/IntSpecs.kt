package com.deffence1776.validationspec.specs

open class ShouldBeGreaterThan<T>(
        targetFun: T.()->Int, //function to get target field of target Type
        fieldNameInMessage: String,//parameters for validation logic and message
        greaterThan: Int
    )
    : FieldValidationSpec<T, Int>(
        "com.deffence1776.validationSpec.specs.ShouldBeGreaterThan" //specName
        ,targetFun
        , { field-> field > greaterThan },//validation logic
        { "$fieldNameInMessage should be greater than $greaterThan." }//message
)

open class ShouldBeLessThan<T>(targetFun:T.()-> Int, msgFieldName: String, lessThan: Int)
    : FieldValidationSpec<T, Int>(
        "com.deffence1776.validationSpec.specs.ShouldBeLessThan"
        ,targetFun
        , {  field-> field  < lessThan },
        { "$msgFieldName should be  less than $lessThan." }
)

open class ShouldBeInRange<T>(targetFun:T.()-> Int, fieldNameInMessage: String, range: IntRange)
    : FieldValidationSpec<T, Int>(
        "com.deffence1776.validationSpec.specs.ShouldBeInRange"
        ,targetFun
        , {  field -> field in range },
        { "$fieldNameInMessage should be in range $range." }
)