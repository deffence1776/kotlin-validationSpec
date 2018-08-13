package com.deffence1776.validationSpec

open class ValidationSpec<T>(val specName:String =""
                            ,val fieldNames: List<String>,
                             val assertionFun: T.() -> Boolean) {
    private var msgFun: ((T) -> String)? = null

    fun showMessage(target:T):String {
        val f = msgFun
        if (null == f) {
            return  "validation failed"
        } else {
            return f.invoke(target)
        }
    }

    fun isValid(target:T):Boolean =assertionFun.invoke(target)


    fun errorMessage(msgFun: (target: T) -> String) {
        this.msgFun = msgFun
    }
}

open class FieldValidationSpec<T, F>(val specName:String =""
                                     , private val targetFun:T.()->F
                                     , private val assertionFun: T.(f: F) -> Boolean
                                     , val msgFun: (target: T) -> String) {
    fun assertionFun(): T.() -> Boolean = { assertionFun.invoke(this, targetFun.invoke(this)) }
}
