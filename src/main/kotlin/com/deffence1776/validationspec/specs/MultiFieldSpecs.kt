package com.deffence1776.validationspec.specs

open class ShouldBeEqualsTwoValue<T>(targetFun:T.()-> Pair<Any,Any>, fieldNameInMessage1: String, fieldNameInMessage2: String) :
        FieldValidationSpec<T, Pair<Any, Any>>(
                "com.deffence1776.validationspec.specs.ShouldBeEqualsTwoValue"
                ,targetFun
                , { (field1,field2) -> field1 == field2 }
                , { "$fieldNameInMessage1 and $fieldNameInMessage2 should be equal." }
        )


open class ShouldNotBeEqualsTwoValue<T>(targetFun:T.()-> Pair<Any,Any>, fieldNameInMessage1: String, fieldNameInMessage2: String) :
        FieldValidationSpec<T, Pair<Any, Any>>(
                "com.deffence1776.validationspec.specs.ShouldNotBeEqualsTwoValue"
                ,targetFun
                , { (field1,field2) -> field1 != field2 }
                , { "$fieldNameInMessage1 and $fieldNameInMessage2 should not be equal." }
        )


open class ShouldBeAllEqual<T>(targetFun:T.()-> Set<Any>, fieldsNameInMessage: String) :
        FieldValidationSpec<T,Set<Any>>(
                "com.deffence1776.validationspec.specs.ShouldBeAllEqual"
                ,targetFun,
                { fieldsSet -> fieldsSet.size ==1  },
                { "$fieldsNameInMessage should be all equal." }
        )

open class ShouldBeAllUnique<T>(targetFun:T.()-> List<Any>, fieldsNameInMessage: String) :
        FieldValidationSpec<T,List<Any>>(
               "com.deffence1776.validationspec.specs.ShouldBeAllUnique"
                ,targetFun
                ,{ fieldsList -> fieldsList.size==fieldsList.distinct().size  },
                { "$fieldsNameInMessage should be all unique." }
        )


