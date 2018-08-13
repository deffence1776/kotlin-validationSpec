package com.deffence1776.validationSpec.specs

import com.deffence1776.validationSpec.validatorSpec
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

internal class ShouldBeAllUniqueTest : StringSpec({

    data class SpecTestModel(val value1:Int,val value2:Int,val value3:Int)

    "spec works "{
        val simpleSpec = validatorSpec<SpecTestModel> {
            fieldNames("value1","value2","value3") {
                spec(ShouldBeAllUnique(targetFun ={listOf(value1,value2,value3)},fieldsNameInMessage = "values"))
            }
        }

        val result = simpleSpec.validateAll(SpecTestModel(1,2,1))

        result.hasErrors() shouldBe true
        result.errors.size shouldBe 1
        result.errors[0].also {
            it.specName shouldBe ShouldBeAllUnique::class.java.name
            it.errorMessage shouldBe "values should be all unique."
            it.fieldNames shouldBe listOf("value1","value2","value3")
        }

        val result2 = simpleSpec.validateAll(SpecTestModel(1,2,3))
        result2.hasErrors() shouldBe false
    }

})