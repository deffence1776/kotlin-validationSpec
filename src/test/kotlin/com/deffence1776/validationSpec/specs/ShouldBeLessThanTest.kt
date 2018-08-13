package com.deffence1776.validationSpec.specs

import com.deffence1776.validationSpec.validatorSpec
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

internal class ShouldBeLessThanTest : StringSpec({

    data class SpecTestUser(val id: Int = 0, val name: String = "", val password: String = "", val confirmPassword: String = "")

    "spec works "{
        val simpleSpec = validatorSpec<SpecTestUser> {
            fieldNames("id") {
                spec(ShouldBeLessThan(targetFun = { id }, lessThan = 10, msgFieldName = "ID"))
                spec("special spec", ShouldBeLessThan(targetFun = { id }, lessThan = 10, msgFieldName = "ID"))
            }
        }

        val result = simpleSpec.validateAll(SpecTestUser(20))
        result.hasErrors() shouldBe true
        result.errors.size shouldBe 2
        result.errors[0].also {
            it.specName shouldBe ShouldBeLessThan::class.java.name
            it.errorMessage shouldBe "ID should be  less than 10."
            it.fieldNames shouldBe listOf("id")
        }

        result.errors[1].also {
            it.specName shouldBe "special spec"
            it.errorMessage shouldBe "ID should be  less than 10."
            it.fieldNames shouldBe listOf("id")
        }
    }
})