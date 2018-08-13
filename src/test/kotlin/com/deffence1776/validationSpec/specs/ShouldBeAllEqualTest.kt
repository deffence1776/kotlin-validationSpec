package com.deffence1776.validationSpec.specs

import com.deffence1776.validationSpec.defineSpecs
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

internal class ShouldBeAllEqualTest : StringSpec({

    data class SpecTestModel(val value1:Int,val value2:Int,val value3:Int)

    "spec works "{
        val simpleSpec = defineSpecs<SpecTestModel> {
            fieldNames("value1","value2","value3") {
                spec(ShouldBeAllEqual(targetFun ={ setOf(value1,value2,value3)},fieldsNameInMessage = "values"))
            }
        }


        val result = simpleSpec.validateAll(SpecTestModel(1,2,1))
        result.hasErrors() shouldBe true
        result.errors.size shouldBe 1
        result.errors[0].also {
            it.specName shouldBe ShouldBeAllEqual::class.java.name
            it.errorMessage shouldBe "values should be all equal."
            it.fieldNames shouldBe listOf("value1","value2","value3")
        }
        val result2 = simpleSpec.validateAll(SpecTestModel(1,1,1))
        result2.hasErrors() shouldBe false
    }

})