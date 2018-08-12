package com.deffence1776.validationSpec.specs
open class BaseValidationSpec<T>(val target: T, val aSpec: T.() -> Boolean){
    private var msgFun : (target: T) -> String ={""}

    fun errorMessage(msgFun: (target: T) -> String){
        this.msgFun=msgFun
    }
}

open class BaseFieldValidationSpec<F, T>(val target: F, val aSpec: (f: F) -> Boolean, val msgFun: (target: T) -> String)
