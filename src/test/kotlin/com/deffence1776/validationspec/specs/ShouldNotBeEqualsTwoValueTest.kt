package com.deffence1776.validationspec.specs

import com.deffence1776.validationspec.defineSpecs
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec


internal class ShouldNotBeEqualsTwoValueTest : StringSpec({

    data class SpecTestUser(val id: Int = 0, val name: String = "", val password: String = "", val confirmPassword: String = "")

    "spec works "{
        val simpleSpec = defineSpecs<SpecTestUser> {
            fieldNames("name","password") {
                spec(ShouldNotBeEqualsTwoValue(targetFun = { Pair(name,password) },fieldNameInMessage1 ="name",fieldNameInMessage2 = "password" ))
            }
        }

        val result = simpleSpec.validateAll(SpecTestUser(1,"admin","admin","passX"))
        result.hasErrors() shouldBe true
        result.errors.size shouldBe 1
        result.errors[0].also {
            it.specName shouldBe ShouldNotBeEqualsTwoValue::class.java.name
            it.errorMessage shouldBe "name and password should not be equal."
            it.fieldNames shouldBe listOf("name","password")
        }
    }
})