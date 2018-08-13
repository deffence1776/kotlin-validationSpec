package com.deffence1776.validationSpec

import com.deffence1776.validationSpec.specs.FieldValidationSpec
import com.deffence1776.validationSpec.specs.ValidationSpec


fun <T> validatorSpec(block: Validator<T>.() -> Unit): Validator<T> {
    val v = Validator<T>()
    block.invoke(v)
    return v
}

/**
 * Validator clas。
 * stocks validation specs and validateAll
 */
class Validator<T> internal constructor(
         private  val specs: MutableList<ValidationSpec<T>> = mutableListOf()
                   ,private  val  fieldNames: List<String> = emptyList()) {

    fun shouldBe(specName:String="",assertionFun: T.() -> Boolean): ValidationSpec<T> {
        val spec = ValidationSpec(specName = specName, assertionFun = assertionFun, fieldNames = fieldNames)
        specs.add(spec)
        return spec
    }

    fun <F> spec(specName:String, fieldSpec: FieldValidationSpec<T, F>) {
       val validationSpec = fieldSpec.toValidationSpec(newSpecName = specName,fieldNames = fieldNames)
        specs.add(validationSpec)
    }

    fun <F> spec( fieldSpec: FieldValidationSpec<T, F>) {
        spec("",fieldSpec)
    }

    /**
     * grouping specs by field names
     */
    fun fieldNames(vararg fieldNames: String, fFlock: Validator<T>.() -> Unit) {

        //create new Validator using current specs and specified fieldNames
        val fieldValidator = Validator(specs, fieldNames.toList())
        fFlock.invoke(fieldValidator)
    }

    private fun execValidate(target: T,validateAll:Boolean) = specs.mapNotNull { spec ->
        if (!spec.isValid(target)) {
            val errorMessage = spec.showMessage(target)
            val error =ValidationError(specName = spec.specName,fieldNames = spec.fieldNames, errorMessage = errorMessage)
            if(!validateAll){
                return  listOf(error)
            }else{
                error
            }
        } else {
            null
        }
    }

    /**
     * execute validation
     */
    fun validateAll(target: T) = ValidationErrors(execValidate(target=target,validateAll=true))

    fun validateUntilFirst(target:T)= ValidationErrors(execValidate(target=target,validateAll=false))

    fun registeredFields()=specs.map { it.fieldNames }.flatten().distinct()

    fun registeredSpecNames()=specs.map { it.specName }.filter { it != "" }.distinct()
}