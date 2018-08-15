package com.deffence1776.validationspec.samples

import com.deffence1776.validationspec.defineSpecs

//Validate Primitive

//define spec for primitive type
val strForNumberSpec = defineSpecs<String> {
    shouldBe { this.isNotBlank() }
    shouldBe { this.toIntOrNull() != null }
}

//if you want flexibility define 「spec function」
fun shouldBeGreaterThan(fieldName: String, greaterThan: Int) = defineSpecs<Int> {
    shouldBe { this > greaterThan }.errorMessage { "$fieldName should be greater than $greaterThan" }
}

//then use confirm
data class SampleModel(val id: Int = 0, val numStr: String = "")

val sampleModelSpec = defineSpecs<SampleModel> {
    fieldNames("id") {
        confirm({ id }, shouldBeGreaterThan("ID", 0))

        //if you want use target's propeties for parameter add block
       // confirm({ id }, {shouldBeGreaterThan("ID", 0)})
    }

    fieldNames("numStr") {
        confirm("strForNumberSpec rule",{ numStr }, strForNumberSpec)
    }
}

fun main(args: Array<String>) {
    val result = sampleModelSpec.validateAll(SampleModel())

    println(result)
//    ValidationErrors(errors=[ValidationError(specName=, errorMessage=ID should be greater than 0, fieldNames=[id])
//    , ValidationError(specName=strForNumberSpec rule, errorMessage=validation failed, fieldNames=[numStr])
//    , ValidationError(specName=strForNumberSpec rule, errorMessage=validation failed, fieldNames=[numStr])])
}