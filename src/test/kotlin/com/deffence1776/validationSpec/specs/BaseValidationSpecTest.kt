//package com.deffence1776.validationSpec.specs
//
//
//import com.deffence1776.validationSpec.Validator
//import io.kotlintest.shouldBe
//import io.kotlintest.specs.StringSpec
//
//data class TestUser(val id: Int = 0, val name: String = "", val password: String = "", val confirmPassword: String = "")
//
//val defaultMessage = "validation failed"
//
//
//internal class BaseValidationSpecTest : StringSpec({
//
//
//    class TestShouldBeGreaterThan<T>(target:Int, idName:String, baseLine:Int)
//        :FieldValidationSpec<Int,T>(
//            target =target
//    ,assertionFun = {false}
//    ,msgFun = {"$idName should greater than $baseLine"})
//
//    "spec works "{
//        val simpleSpec = Validator<TestUser> {
//            field<Int>("id"){
//                spec { TestShouldBeGreaterThan(id,"ID",0) }
//                spec { TestShouldBeGreaterThan(id,"ID",0) }
//
//            }
//        }
//        val e = simpleSpec.validate(TestUser())
//        e.hasErrors() shouldBe true
//        e.errors[0].errorMessage shouldBe "ID should greater than 0"
//        e.errors[1].errorMessage shouldBe "ID should greater than 0"
//    }
//
//})