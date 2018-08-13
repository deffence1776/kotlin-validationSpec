package com.deffence1776.validationSpec

import com.deffence1776.validationSpec.specs.ShouldBeGreaterThan
import com.deffence1776.validationSpec.specs.ShouldNotBeBlank
import io.kotlintest.matchers.doubles.shouldBeLessThan
import io.kotlintest.specs.StringSpec

internal class ValidatorPerformanceTest : StringSpec({

    data class TestUser(val id: Int = 0, val name: String = "", val password: String = "", val confirmPassword: String = "")

    "10,000 times validation average within 10 milliseconds" {
        //spec usually create once
        val simpleSpec = defineSpecs<TestUser> {
            fieldNames("id") {
                shouldBe("bc") { id > 0 }
                spec(ShouldBeGreaterThan(targetFun = {id},greaterThan = 0,fieldNameInMessage = "ID"))
                spec(ShouldNotBeBlank(targetFun = {name},fieldNameInMessage = "NAME"))

            }
        }

        val validationTarget = TestUser()
        val f = {
            for (i in 1..10000) {
                simpleSpec.validateAll(validationTarget)
            }
        }

        val result = (1..10).map { measureDuration(f) }

        println(result) //[13, 8, 8, 5, 4, 14, 2, 2, 2, 2]
        result.average().shouldBeLessThan(10.0)

    }
})

private fun measureDuration(f: () -> Unit): Long {
    val start = System.currentTimeMillis()
    f.invoke()
    val end = System.currentTimeMillis()
    return end - start
}