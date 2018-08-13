package com.deffence1776.validationSpec

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

data class TestUser(val id: Int = 0, val name: String = "", val password: String = "", val confirmPassword: String = "")

const val defaultMessage = "validation failed"

internal class ValidatorTest : StringSpec({


    "No error returns when object is valid" {
        val simpleSpec = validatorSpec<TestUser> {
            shouldBe { id > 0 }
        }

        val result = simpleSpec.validate(TestUser(1))
        result.hasErrors() shouldBe false
    }

    "manual validation works and default message return" {
        val simpleSpec = validatorSpec<TestUser> {
            shouldBe { id > 0 }
        }

        val result = simpleSpec.validate(TestUser())
        result.hasErrors() shouldBe true
        result.errors.size shouldBe 1
        result.errors[0].also {
            it.specName shouldBe ""
            it.errorMessage shouldBe defaultMessage
            it.fieldNames shouldBe emptyList()
        }
    }

    "multi specs works" {
        val simpleSpec = validatorSpec<TestUser> {
            shouldBe("id size") { id > 0 }
            shouldBe { name.isNotBlank() }

        }

        val result = simpleSpec.validate(TestUser())
        result.hasErrors() shouldBe true
        result.errors.size shouldBe 2
        result.errors[0].also {
            it.specName shouldBe "id size"
            it.errorMessage shouldBe defaultMessage
            it.fieldNames shouldBe emptyList()
        }

        result.errors[1].also {
            it.specName shouldBe ""
            it.errorMessage shouldBe defaultMessage
            it.fieldNames shouldBe emptyList()
        }
    }

    "specified message returns.validate same as registered order" {
        val testSpec = validatorSpec<TestUser> {
            shouldBe { id > 0 }.errorMessage { "id should greater than zero." }
            shouldBe { name.isNotBlank() }.errorMessage { "name should not blank." }
        }

        val result = testSpec.validate(TestUser())
        result.hasErrors() shouldBe true
        result.errors.size shouldBe 2

        result.errors[0].also {
            it.errorMessage shouldBe "id should greater than zero."
            it.fieldNames shouldBe emptyList()
        }

        result.errors[1].also {
            it.errorMessage shouldBe "name should not blank."
            it.fieldNames shouldBe emptyList()
        }
    }

    "field spec works" {
        val simpleSpec = validatorSpec<TestUser> {
            fieldNames("id") {
                shouldBe { id > 0 }
            }
            fieldNames("name") {
                shouldBe { name.isNotBlank()  }
            }
        }

        val result = simpleSpec.validate(TestUser())
        result.hasErrors() shouldBe true
        result.errors.size shouldBe 2
        result.errors[0].also {
            it.errorMessage shouldBe defaultMessage
            it.fieldNames shouldBe listOf("id")
        }

        result.errors[1].also {
            it.errorMessage shouldBe defaultMessage
            it.fieldNames shouldBe listOf("name")
        }
    }
})