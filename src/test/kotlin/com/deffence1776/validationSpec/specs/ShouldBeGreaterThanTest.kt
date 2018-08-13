package com.deffence1776.validationSpec.specs


import com.deffence1776.validationSpec.defineSpecs
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec



internal class ShouldBeGreaterThanTest : StringSpec({

     data class SpecTestUser(val id: Int = 0, val name: String = "", val password: String = "", val confirmPassword: String = "")


    "spec works "{
        val testSpec = defineSpecs<SpecTestUser> {
            fieldNames("id") {
                spec(ShouldBeGreaterThan(targetFun = { id }, greaterThan = 0, fieldNameInMessage = "ID"))
                spec("special spec", ShouldBeGreaterThan(targetFun = { id }, greaterThan = 0, fieldNameInMessage = "ID"))
            }
        }

        val result = testSpec.validateAll(SpecTestUser())
        result.hasErrors() shouldBe true
        result.errors.size shouldBe 2
        result.errors[0].also {
            it.specName shouldBe ShouldBeGreaterThan::class.java.name
            it.errorMessage shouldBe "ID should be greater than 0."
            it.fieldNames shouldBe listOf("id")
        }

        result.errors[1].also {
            it.specName shouldBe "special spec"
            it.errorMessage shouldBe "ID should be greater than 0."
            it.fieldNames shouldBe listOf("id")
        }

    }

})