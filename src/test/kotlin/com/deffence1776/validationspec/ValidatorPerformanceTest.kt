package com.deffence1776.validationspec

import io.kotlintest.matchers.doubles.shouldBeLessThan
import io.kotlintest.specs.StringSpec
import org.hibernate.validator.constraints.Length
import javax.validation.Validation
import javax.validation.constraints.Min
import javax.validation.constraints.Size
import kotlin.system.measureTimeMillis


internal class ValidatorPerformanceTest : StringSpec({

    data class TestUser(@get:Min(1)val id: Int = 0,

                        @get:Length(10) val name: String = "", val password: String = "", val confirmPassword: String = "")

    "10,000 times validation average" {

        fun shouldBeGreaterThan(fieldNameInmessage: String, greaterThan: Int) = defineSpecs<Int> {
            shouldBe { this > greaterThan }.errorMessage { "$fieldNameInmessage should be greater than $greaterThan" }
        }

        fun shouldNotBeBlank(fieldNameInmessage: String) = defineSpecs<String> {
            shouldBe { this.isNotBlank() }.errorMessage { "$fieldNameInmessage should not be blank." }
        }

        //spec usually create once
        val fastSpec = defineSpecs<TestUser> {
            fieldNames("id") {
                shouldBe { id > 0 }.errorMessage { "ID should be greater than 0" }
            }
            fieldNames("name"){
                shouldBe { name.isNotBlank() }.errorMessage { "NAME should not be blank." }
            }
        }

        //confirm method is slow.but still much faster than hibernate validator
        val slowSpec = defineSpecs<TestUser> {
            fieldNames("id") {
                confirm({ id }, shouldBeGreaterThan("ID", 0) )
            }
            fieldNames("name"){
                confirm({ name },  shouldNotBeBlank("ID"))
            }
        }


        val validationTarget = TestUser()

        val fastResult = measureFunction(10,10000){
            fastSpec.validateAll(validationTarget)
        }

        println("fast:$fastResult") //example fast:[8, 2, 1, 2, 3, 3, 0, 1, 0, 1]
        println("fast average:${fastResult.average()}")//example fast average:2.1
        fastResult.average().shouldBeLessThan(5.0)


        val slowResult = measureFunction(10,10000){
            slowSpec.validateAll(validationTarget)
        }

        println("slow:$slowResult") //example slow:[51, 10, 7, 10, 6, 4, 9, 11, 9, 4]
        println("slow average:${slowResult.average()}")//example slow average:12.1
        slowResult.average().shouldBeLessThan(25.0)


        val validatorFactory = Validation.buildDefaultValidatorFactory()
        val validator = validatorFactory.validator

        val hibernateResult = measureFunction(10,10000){
            validator.validate(validationTarget)
        }

        println("hibernate:$hibernateResult")//example [354, 168, 81, 37, 35, 37, 61, 54, 25, 34]
        println("hibernate average:${hibernateResult.average()}")//example 88.6

        slowResult.average().shouldBeLessThan(hibernateResult.average())

    }
})

fun measureFunction(measureTimes: Int, baseTimes: Int, f: () -> Unit) = (1..measureTimes).map {
    measureTimeMillis {
        for (i in 1..baseTimes) {
            f.invoke()
        }
    }
}
