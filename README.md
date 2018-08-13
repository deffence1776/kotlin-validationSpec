Kotlin-validationSpec
==========================

kotlin Field validator.  
Inspired by  [Kotlin Validation](https://github.com/kamedon/Validation) 


## Getting Started

first create validation spec for type.

```kotlin

//target for validation
data class SampleUser(val id: Int = 0, val name: String = "", val password: String = "", val confirmPassword: String = "")

val sampleValidationSpec = validatorSpec<SampleUser> {
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

    //reusable Spec Object.
    fieldNames("name") {
        spec(ShouldNotBeBlank(targetFun = { name }, fieldNameInMessage = "NAME"))

        // specify specName
        spec("name length check", ShouldBeInRange(targetFun = { name.length }, range = 1..10, fieldNameInMessage = "NAME"))
    }
}

```

then, validate object.

```kotlin

fun main(args: Array<String>) {
    val sampleUser = SampleUser()

    //validate All spec
    val result: ValidationErrors = sampleValidationSpec.validateAll(sampleUser)
    println(result)
//    ValidationErrors(errors=[
//        ValidationError(specName=, errorMessage=validation failed, fieldNames=[])
//        , ValidationError(specName=password not blank, errorMessage=validation failed, fieldNames=[password])
//        , ValidationError(specName=password length range, errorMessage=validation failed, fieldNames=[password])
//        , ValidationError(specName=com.deffence1776.validationSpec.specs.ShouldNotBeBlank, errorMessage=NAME should not be blank., fieldNames=[name])
//        , ValidationError(specName=name length check, errorMessage=NAME should be in range 1..10., fieldNames=[name])])
//


    //validate until first error occurred
    val result2: ValidationErrors = sampleValidationSpec.validateUntilFirst(sampleUser)
    println(result2)
//    ValidationErrors(errors=[ValidationError(specName=, errorMessage=validation failed, fieldNames=[])])
}


```

* see [sample](https://github.com/deffence1776/kotlin-validationSpec/blob/master/src/test/kotlin/com/deffence1776/validationSpec/ValidatorSample.kt) 


## Spec Object

Reusable validation logic and message.   
You can create your own Spec for your Domain.

```kotlin

open class ShouldBeGreaterThan<T>(
        targetFun: T.()->Int, //function to get target field of target Type
        
        //parameters for validation logic and message
        fieldNameInMessage: String,
        greaterThan: Int
    )
    : FieldValidationSpec<T, Int>(
        ShouldBeGreaterThan::class.java.name //specName
        ,targetFun
        , { field-> field > greaterThan },//assertion function
        { "$fieldNameInMessage should be greater than $greaterThan." }//message
)

```

## Performance

not so bad.if validation logic is simple.

```kotlin
internal class ValidatorPerformanceTest : StringSpec({

    data class TestUser(val id: Int = 0, val name: String = "", val password: String = "", val confirmPassword: String = "")

    "10,000 times validation" {
        //spec usually create once
        val simpleSpec = validatorSpec<TestUser> {
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

        println(result) //example [13, 8, 8, 5, 4, 14, 2, 2, 2, 2]
        result.average().shouldBeLessThan(10.0)

    }
})

private fun measureDuration(f: () -> Unit): Long {
    val start = System.currentTimeMillis()
    f.invoke()
    val end = System.currentTimeMillis()
    return end - start
}
```