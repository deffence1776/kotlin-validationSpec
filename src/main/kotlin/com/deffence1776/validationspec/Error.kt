package com.deffence1776.validationspec

data class ValidationError(val specName:String,val errorMessage: String, val fieldNames: List<String>)

data class ValidationErrors(val errors: List<ValidationError>) {
    fun fieldErrors(vararg fieldNames: String) =
            errors.filter { it.fieldNames.containsAll(fieldNames.toList()) }
    fun singleFieldErrors(fieldName:String)=
            errors.filter { it.fieldNames.size ==1 && it.fieldNames.contains(fieldName)}

    fun hasErrors() = !errors.isEmpty()
}
