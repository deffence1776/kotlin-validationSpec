package com.deffence1776.validationSpec.specs

open class ShouldBeEqual<T>(aTargets: List<Any>, msgFieldName1: Any, msgFieldName2: String) :
        BaseFieldValidationSpec<List<Any>, T>(aTargets,
                { targets -> targets[0] == targets[1] }, { "$msgFieldName1 and $msgFieldName2 should be same." }
        )

open class ShouldNotBeEqual<T>(aTargets: List<Any>, msgFieldName1: String, msgFieldName2: String) :
        BaseFieldValidationSpec<List<Any>, T>(aTargets,
                { targets -> targets[0] != targets[1] },
                { "$msgFieldName1 and $msgFieldName2 should not  be same." }
        )

open class ShouldBeAllUnique<T>(vararg  aTargets: Any, msgName: String) :
        BaseFieldValidationSpec<Array<out Any>, T>(aTargets,
                { targets -> targets.toSet().size==targets.size },
                { "$msgName should be all unique." }
        )

open class ShouldBeAllSame<T>(vararg  aTargets: Any, msgName: String) :
        BaseFieldValidationSpec<Array<out Any>, T>(aTargets,
                { targets -> targets.toSet().size==1 },
                { "$msgName は全て一致しなければいけません" }
        )
