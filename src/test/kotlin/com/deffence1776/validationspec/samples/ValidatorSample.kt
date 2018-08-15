package com.deffence1776.validationspec.samples

import com.deffence1776.validationspec.ValidationErrors
import com.deffence1776.validationspec.defineSpecs

//target for validation
data class SampleUser(val id: Int = 0, val name: String = "", val password: String = "", val confirmPassword: String = "")

//declare spec
val sampleValidationSpec = defineSpecs<SampleUser> {
    //most simple. functional style
    shouldBe { id > 0 }

    //specify specName and error message
    shouldBe("id max spec") { id < 100 }.errorMessage { "id $id is invalid. should be less than 100" }

    //add target field names
    fieldNames("password") {
        shouldBe("password not blank") { password.isNotBlank() }
        shouldBe("password length range") { password.length in 10..15 }
    }

    //multi field check
    fieldNames("password", "confirmPassword") {
        shouldBe("password confirmPassword same") { password == confirmPassword }
    }

}

fun main(args: Array<String>) {
    val sampleUser = SampleUser()

    //validate All spec
    val result: ValidationErrors = sampleValidationSpec.validateAll(sampleUser)
    println(result)
//    ValidationErrors(errors=[ValidationError(specName=, errorMessage=validation failed, fieldNames=[])
//        , ValidationError(specName=password not blank, errorMessage=validation failed, fieldNames=[password])
//        , ValidationError(specName=password length range, errorMessage=validation failed, fieldNames=[password])])


    //validate until error occurred
    val result2: ValidationErrors = sampleValidationSpec.validateUntilFirst(sampleUser)
    println(result2)
//    ValidationErrors(errors=[ValidationError(specName=, errorMessage=validation failed, fieldNames=[])])
}