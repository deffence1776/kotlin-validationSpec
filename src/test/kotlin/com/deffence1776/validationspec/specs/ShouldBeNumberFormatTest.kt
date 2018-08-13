package com.deffence1776.validationspec.specs

import com.deffence1776.validationspec.defineSpecs
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec



internal class ShouldBeNumberFormatTest : StringSpec({

    data class SpecTestUser(val id: Int = 0, val name: String = "", val password: String = "", val confirmPassword: String = "")

    "spec works "{
        val simpleSpec = defineSpecs<SpecTestUser> {
            fieldNames("name") {
                spec(ShouldBeNumberFormat(targetFun = { name },fieldNameInMessage = "NAME"))
                spec("special spec",ShouldBeNumberFormat(targetFun = { name },fieldNameInMessage = "NAME"))
            }
        }

        val result = simpleSpec.validateAll(SpecTestUser())
        result.hasErrors() shouldBe true
        result.errors.size shouldBe 2
        result.errors[0].also {
            it.specName shouldBe ShouldBeNumberFormat::class.java.name
            it.errorMessage shouldBe "NAME should be Number."
            it.fieldNames shouldBe listOf("name")
        }

        result.errors[1].also {
            it.specName shouldBe "special spec"
            it.errorMessage shouldBe "NAME should be Number."
            it.fieldNames shouldBe listOf("name")
        }

    }

})