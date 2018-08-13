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
 * stocks validation specs and validate
 */
class Validator<T> internal constructor(private val specs: MutableList<ValidationSpec<T>> = mutableListOf()
                   , private val fieldNames: List<String> = emptyList()) {

    /**
     *
     */
    fun shouldBe(specName:String="",assertionFun: T.() -> Boolean): ValidationSpec<T> {
        val spec = ValidationSpec(specName = specName, assertionFun = assertionFun, fieldNames = fieldNames)
        specs.add(spec)
        return spec
    }

    /**
     *
     */
    fun <F> spec(specName:String, fieldSpec: FieldValidationSpec<T, F>) {
       val validationSpec = fieldSpec.toValidationSpec(newSpecName = specName,fieldNames = fieldNames)
        specs.add(validationSpec)
    }

    /**
     *
     */
    fun <F> spec( fieldSpec: FieldValidationSpec<T, F>) {
        spec("",fieldSpec)
    }

    /**
     * grouping specs by field names
     */
    fun fieldNames(vararg fieldNames: String, fFlock: Validator<T>.() -> Unit) {

        //create new Validator using current specs and specified fieldNames
        val fieldValidator = Validator<T>(specs, fieldNames.toList())
        fFlock.invoke(fieldValidator)
    }

    private fun execValidate(target: T) = specs.mapNotNull { spec ->
        if (!spec.isValid(target)) {
            val errorMessage = spec.showMessage(target)
            ValidationError(specName = spec.specName,fieldNames = spec.fieldNames, errorMessage = errorMessage)
        } else {
            null
        }
    }

    /**
     * execute validation
     */
    fun validate(target: T) = ValidationErrors(execValidate(target))

    /**
     * check registered field names for test
     */
    fun checkFieldNames(target: T) {
        val registeredFieldNamesSet = specs.map { it.fieldNames }.flatten().toSet()

        val t = target as Any
        val type = t::class.java
        val propetyNames = type.declaredFields.toSet().map { it.name }
        registeredFieldNamesSet.forEach { registeredName ->
            require(propetyNames.contains(registeredName)) { "target Type [${type.name} ] doesn't has property named [$registeredName]. correct property Names $propetyNames" }
        }
    }
}
