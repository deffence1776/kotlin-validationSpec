package com.deffence1776.validationspec.samples

import com.deffence1776.validationspec.defineSpecs


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

