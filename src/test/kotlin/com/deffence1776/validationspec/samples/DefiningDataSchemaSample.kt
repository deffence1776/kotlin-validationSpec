package com.deffence1776.validationspec.samples

import com.deffence1776.validationspec.defineSpecs


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
        assert(spec.isValid(this)){"doesn't satisfy the spec:\n"+ spec.validateAll(this)}
    }
}

//Form
class RegisterForm(){
    var itemId=""
        set(value){
            //assert
            assert(spec.isValid(this)){"doesn't satisfy the spec:\n"+ spec.validateAll(this)}
            field = value
        }

    companion object {
        val spec= defineSpecs<RegisterForm> {
            //developer can easily understand itemId of Item and RegisterForm are same.
            confirm({itemId}, applicationItemIdSpec)
        }
    }
}

