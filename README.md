Kotlin-validationSpec
==========================

kotlin functional field validator.  
depends on only Kotlin stdlib.

  
Inspired by  [Clojure Spec](https://clojure.org/guides/spec)   
Inspired by  [Kotlin Validation](https://github.com/kamedon/Validation) 


## Getting Started

First, create validation spec for type.

```kotlin

//target for validation
data class SampleUser(val id: Int = 0, val name: String = "", val password: String = "", val confirmPassword: String = "")

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

```

Then, validate object.

```kotlin

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

```

* see [sample](https://github.com/deffence1776/kotlin-validationSpec/blob/master/src/test/kotlin/com/deffence1776/validationspec/samples/ValidatorSample.kt) 


## Reuse specs
Defied specs are reusable.  
Use 「confirm」method.   

For example, define for primitive types.
```kotlin

//define spec for primitive type
val strForNumberSpec = defineSpecs<String> {
    shouldBe { this.isNotBlank() }
    shouldBe { this.toIntOrNull() != null }
}

//if you want flexibility define 「spec function」
fun shouldBeGreaterThan(fieldName: String, greaterThan: Int) = defineSpecs<Int> {
    shouldBe { this > greaterThan }.errorMessage { "$fieldName should be greater than $greaterThan" }
}
```

Then, use them in other spec definitions.

```kotlin
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
```

Then, execute validation.

```kotlin
fun main(args: Array<String>) {
    val result = sampleModelSpec.validateAll(SampleModel())

    println(result)
//    ValidationErrors(errors=[ValidationError(specName=, errorMessage=ID should be greater than 0, fieldNames=[id])
//    , ValidationError(specName=strForNumberSpec rule, errorMessage=validation failed, fieldNames=[numStr])
//    , ValidationError(specName=strForNumberSpec rule, errorMessage=validation failed, fieldNames=[numStr])])
}

```


* see [sample](https://github.com/deffence1776/kotlin-validationSpec/blob/master/src/test/kotlin/com/deffence1776/validationspec/samples/PrimitiveTypeSpecSample.kt) 

## Complex Object

Here is a sample code for validating a complex object for your domain.  
The object has nested and private property.

 ```kotlin
//Value Object With private field
class UserId(private val value: String) {
    companion object {
        //public val. define spec at companion object to access private properties
        val spec = defineSpecs<UserId> {
            shouldBe("user id length rule") { value.length == 5 }.errorMessage { "user id's length should be 5" }
        }
    }
}


//Value Object With private field
class UserName(private val value: String) {
    companion object {
        val spec = defineSpecs<UserName> {
            shouldBe("user name length rule") { value.length in 1..10 }.errorMessage { "username's length should be in 1..10" }
        }
    }
}

//Immutable Entity
class User(private val userId: UserId, private val userName: UserName) {
    init{
        //you can define assertion.by assert method validate if -ea option added 
        spec.assert(this)
    }
    
    companion object {
        //define spec for innerValue
        val spec = defineSpecs<User> {
            fieldNames("userId") {
                confirm({ userId }, UserId.spec )
            }

            fieldNames("userName") {
                confirm({ userName }, UserName.spec)
            }
        }
    }
    
    //you can define validate method 
    fun validate()=spec.validateAll(this)
}

fun main(args: Array<String>) {

    val user = User(UserId("abc"), UserName("12345678901"))
    val result = User.spec.validateAll(user)
    //or 
    //val result = user.validate()
    
    println(result)

}

 ```


* see [sample](https://github.com/deffence1776/kotlin-validationSpec/blob/master/src/test/kotlin/com/deffence1776/validationspec/samples/ComplexObjectValidationSample.kt) 

## Definining Data Spec

Value Object is elegant approach. But in real world, data can't be confined in one system boundary.  
When interact with database, view, framework, or other systems, complex types may make some trouble.  

Spec can define data schema using primitive types. It makes code more human readable.

 ```kotlin

//define spec for primitive type.and name it.
val applicationItemIdSpec = defineSpecs<String> {
    shouldBe { this.isNotBlank() }
    shouldBe { this.toIntOrNull() != null }
}

//use spec to various Model.such as Entities or Forms

//Entity
class Item(val itemId:String){
    companion object {
        val spec= defineSpecs<Item> {
            //developer can make sense what [itemId] means
            confirm({itemId}, applicationItemIdSpec)
        }
    }
    init{
        //assert is useful
        spec.assert(this)
    }
}

//Form
class RegisterForm(){
    var itemId=""
        set(value){
            //assert
            spec.assert(this)
            field = value
        }

    companion object {
        val spec= defineSpecs<RegisterForm> {
            //developer can easily understand itemId of Item and RegisterForm are same.
            confirm({itemId}, applicationItemIdSpec)
        }
    }
}

 ```


## Performance

Confirm method is slow.  
But, still much faster than hibernate validator 

```kotlin

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

```

