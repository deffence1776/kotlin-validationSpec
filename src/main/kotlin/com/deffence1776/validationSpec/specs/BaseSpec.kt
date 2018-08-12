package com.deffence1776.validationSpec.specs

open class BaseValidationSpec<F, T>(val target: F, val aSpec: (f: F) -> Boolean, val msgFun: (target: T) -> String)
