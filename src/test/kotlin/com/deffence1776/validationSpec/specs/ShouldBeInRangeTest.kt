package com.deffence1776.validationspec.specs

import com.deffence1776.validationspec.defineSpecs
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec


internal class ShouldBeInRangeTest : StringSpec({

    data class SpecTestUser(val id: Int = 0, val name: String = "", val password: String = "", val confirmPassword: String = "")

    "spec works "{
        val simpleSpec = defineSpecs<SpecTestUser> {
            fieldNames("id") {
                spec(ShouldBeInRange(targetFun = { id }, range = 1..100, fieldNameInMessage = "ID"))
            }
        }

        val result = simpleSpec.validateAll(SpecTestUser())
        result.hasErrors() shouldBe true
        result.errors.size shouldBe 1
        result.errors[0].also {
            it.specName shouldBe ShouldBeInRange::class.java.name
            it.errorMessage shouldBe "ID should be in range 1..100."
            it.fieldNames shouldBe listOf("id")
        }

    }
})