package com.deffence1776.validationSpec.specs

import com.deffence1776.validationSpec.defineSpecs
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec


internal class ShouldBeEqualsTwoValueTest : StringSpec({

    data class SpecTestUser(val id: Int = 0, val name: String = "", val password: String = "", val confirmPassword: String = "")

    "spec works "{
        val simpleSpec = defineSpecs<SpecTestUser> {
            fieldNames("password","confirmPassword") {
                spec(ShouldBeEqualsTwoValue(targetFun = { Pair(password,confirmPassword) },fieldNameInMessage1 ="password",fieldNameInMessage2 = "confirmPassword" ))
            }
        }


        val result = simpleSpec.validateAll(SpecTestUser(1,"","pass","passX"))
        result.hasErrors() shouldBe true
        result.errors.size shouldBe 1
        result.errors[0].also {
            it.specName shouldBe ShouldBeEqualsTwoValue::class.java.name
            it.errorMessage shouldBe "password and confirmPassword should be equal."
            it.fieldNames shouldBe listOf("password","confirmPassword")
        }
    }

})