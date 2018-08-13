package com.deffence1776.validationSpec.specs

private val shouldBeEqualsTwoValue = ShouldBeEqualsTwoValue::class.java.name
open class ShouldBeEqualsTwoValue<T>(targetFun:T.()-> Pair<Any,Any>, msgFieldName1: Any, msgFieldName2: String) :
        FieldValidationSpec<T, Pair<Any, Any>>(
                shouldBeEqualsTwoValue
                ,targetFun
                , { (field1,field2) -> field1 == field2 }
                , { "$msgFieldName1 and $msgFieldName2 should be same." }
        )


private val shouldNotBeEqualsTwoValue = ShouldNotBeEqualsTwoValue::class.java.name
open class ShouldNotBeEqualsTwoValue<T>(targetFun:T.()-> Pair<Any,Any>, msgFieldName1: Any, msgFieldName2: String) :
        FieldValidationSpec<T, Pair<Any, Any>>(
                shouldNotBeEqualsTwoValue
                ,targetFun
                , { (field1,field2) -> field1 != field2 }
                , { "$msgFieldName1 and $msgFieldName2 should be same." }
        )

//
//open class ShouldNotBeEqual<T>(aTargets:T.()-> List<Any>, msgFieldName1: String, msgFieldName2: String) :
//        FieldValidationSpec<T,List<Any>>(
//                ""
//                ,aTargets,
//                { targets -> targets[0] != targets[1] },
//                { "$msgFieldName1 and $msgFieldName2 should not  be same." }
//        )
//
//open class ShouldBeAllUnique<T>(vararg aTargets:T.()-> Any, msgName: String) :
//        FieldValidationSpec<T,Array<out Any>>(
//                ""
//                ,aTargets
//                , { targets -> targets.toSet().size == targets.size }
//                , { "$msgName should be all unique." }
//        )

