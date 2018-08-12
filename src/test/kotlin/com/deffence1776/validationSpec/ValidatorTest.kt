package com.deffence1776.validationSpec

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

data class TestUser(val id: Int = 0, val name: String = "", val password: String = "", val confirmPassword: String = "")

val defaultMessage = "validation failed"

internal class ValidatorTest : StringSpec({


    "manual validation works and default message return" {
        val simpleSpec = Validator<TestUser> {
            shouldBe { id > 0 }
        }

        simpleSpec.validate(TestUser(1)).hasErrors() shouldBe false
        simpleSpec.validate(TestUser()).hasErrors() shouldBe true
        simpleSpec.validate(TestUser()).errors[0].errorMessage shouldBe defaultMessage


        simpleSpec.validate(TestUser()).fieldErrors("id").isEmpty() shouldBe true

    }

    "multi specs works" {
        val simpleSpec = Validator<TestUser> {
            shouldBe { id > 0 }
            shouldBe { name.isNotBlank() }

        }

        simpleSpec.validate(TestUser(1, "name")).hasErrors() shouldBe false
        simpleSpec.validate(TestUser()).hasErrors() shouldBe true
        simpleSpec.validate(TestUser()).errors.size shouldBe 2

        simpleSpec.validate(TestUser()).errors[0].errorMessage shouldBe defaultMessage
        simpleSpec.validate(TestUser()).errors[1].errorMessage shouldBe defaultMessage


        simpleSpec.validate(TestUser()).fieldErrors("id").isEmpty() shouldBe true
        simpleSpec.validate(TestUser()).fieldErrors("name").isEmpty() shouldBe true

    }

    "specified message returns.validate same as registered order" {
        val simpleSpec = Validator<TestUser> {
            shouldBe { id > 0 }.errorMessage { "id should greater than zero." }
            shouldBe { name.isNotBlank() }.errorMessage { "name should not blank." }
        }

        simpleSpec.validate(TestUser()).errors[0].errorMessage shouldBe "id should greater than zero."
        simpleSpec.validate(TestUser()).errors[1].errorMessage shouldBe "name should not blank."
    }

    "field spec works" {
        val simpleSpec = Validator<TestUser> {
            field<Int>("id") {
                shouldBe { id > 0 }.errorMessage { "id should greater than zero." }
                shouldBe { id < 5 }.errorMessage { "id should smaller than 5." }
            }

            field<Int>("name") {
                shouldBe { name.isNotBlank() }.errorMessage { "name should not blank." }
            }
        }

        simpleSpec.validate(TestUser(1, "name")).hasErrors() shouldBe false
        simpleSpec.validate(TestUser()).hasErrors() shouldBe true
        simpleSpec.validate(TestUser(10)).errors.size shouldBe 2

        simpleSpec.validate(TestUser(10)).errors[0].errorMessage shouldBe "id should smaller than 5."
        simpleSpec.validate(TestUser(10)).errors[1].errorMessage shouldBe "name should not blank."


        simpleSpec.validate(TestUser(10)).fieldErrors("id").size shouldBe 1
        simpleSpec.validate(TestUser()).fieldErrors("name").size shouldBe 1
    }

    "multi field spec works" {
        val simpleSpec = Validator<TestUser> {
            field<String>("password","confirmPassword") {
                shouldBe { password==confirmPassword }.errorMessage { "error" }
                shouldBe { password.isNotBlank() && confirmPassword.isNotBlank() }.errorMessage { "error2" }
            }
        }

        simpleSpec.validate(TestUser(1, "","pass","pass")).hasErrors() shouldBe false
        simpleSpec.validate(TestUser()).hasErrors() shouldBe true
        simpleSpec.validate(TestUser(10)).errors.size shouldBe 1

        simpleSpec.validate(TestUser(10,"","pass","pass2")).errors[0].errorMessage shouldBe "error"
        simpleSpec.validate(TestUser()).errors[0].errorMessage shouldBe "error2"


        simpleSpec.validate(TestUser(10)).fieldErrors("password").size shouldBe 1
    }
})