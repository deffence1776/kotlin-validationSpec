package com.deffence1776.validationspec.specs

open class ValidationSpec<T>(val specName:String =""
                            ,val fieldNames: List<String>,
                             val assertionFun: T.() -> Boolean) {
    private var msgFun: ((T) -> String)? = null

    fun showMessage(target:T):String {
        val f = msgFun
        return f?.invoke(target) ?: "validation failed"
    }

    fun isValid(target:T):Boolean =assertionFun.invoke(target)


    fun errorMessage(msgFun:  T.() -> String) {
        this.msgFun = msgFun
    }
}

open class FieldValidationSpec<T, F>(val specName:String
                                     , private val targetFun:T.()->F
                                     , private val assertionFun: T.(f: F) -> Boolean
                                     , val msgFun: (target: T) -> String) {
    private fun assertionFun(): T.() -> Boolean = { assertionFun.invoke(this, targetFun.invoke(this)) }

    fun toValidationSpec(newSpecName:String,fieldNames: List<String>):ValidationSpec<T>{
        val specNameToRegister = if(""==newSpecName) specName else newSpecName
        val validationSpec = ValidationSpec(specName = specNameToRegister, assertionFun = assertionFun(), fieldNames = fieldNames)
        validationSpec.errorMessage(msgFun)
        return validationSpec
    }
}
