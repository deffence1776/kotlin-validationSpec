package com.deffence1776.validationSpec.specs

open class ShouldBeEqualsTwoValue<T>(targetFun:T.()-> Pair<Any,Any>, fieldNameInMessage1: String, fieldNameInMessage2: String) :
        FieldValidationSpec<T, Pair<Any, Any>>(
                ShouldBeEqualsTwoValue::class.java.name
                ,targetFun
                , { (field1,field2) -> field1 == field2 }
                , { "$fieldNameInMessage1 and $fieldNameInMessage2 should be equal." }
        )


open class ShouldNotBeEqualsTwoValue<T>(targetFun:T.()-> Pair<Any,Any>, fieldNameInMessage1: String, fieldNameInMessage2: String) :
        FieldValidationSpec<T, Pair<Any, Any>>(
                ShouldNotBeEqualsTwoValue::class.java.name
                ,targetFun
                , { (field1,field2) -> field1 != field2 }
                , { "$fieldNameInMessage1 and $fieldNameInMessage2 should not be equal." }
        )


open class ShouldBeAllEqual<T>(targetFun:T.()-> Set<Any>, fieldsNameInMessage: String) :
        FieldValidationSpec<T,Set<Any>>(
                ShouldBeAllEqual::class.java.name
                ,targetFun,
                { fieldsSet -> fieldsSet.size ==1  },
                { "$fieldsNameInMessage should be all equal." }
        )

open class ShouldBeAllUnique<T>(targetFun:T.()-> List<Any>, fieldsNameInMessage: String) :
        FieldValidationSpec<T,List<Any>>(
                ShouldBeAllUnique::class.java.name
                ,targetFun
                ,{ fieldsList -> fieldsList.size==fieldsList.distinct().size  },
                { "$fieldsNameInMessage should be all unique." }
        )


