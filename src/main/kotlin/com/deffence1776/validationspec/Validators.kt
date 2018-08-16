package com.deffence1776.validationspec

import com.deffence1776.validationspec.specs.FieldValidationSpec
import com.deffence1776.validationspec.specs.ValidationNestedResult
import com.deffence1776.validationspec.specs.ValidationProcessItem
import com.deffence1776.validationspec.specs.ValidationSpec


fun <T> defineSpecs(block: Validator<T>.() -> Unit): Validator<T> {
    val v = Validator<T>()
    block.invoke(v)
    return v
}


/**
 * Validator clas。
 * stocks validation validationProcessItems and validateAll
 */
class Validator<T> internal constructor(
        private val validationProcessItems: MutableList<ValidationProcessItem<T>> = mutableListOf()
        , private val fieldNames: List<String> = emptyList()) {


    fun shouldBe(specName: String = "", assertionFun: T.() -> Boolean): ValidationSpec<T> {
        val spec = ValidationSpec(specName = specName, assertionFun = assertionFun, fieldNames = fieldNames)
        validationProcessItems.add(spec)
        return spec
    }

    @Deprecated("use confirm for reuse",replaceWith = ReplaceWith("Validator#confirm"))
    fun <F> spec(specName: String, fieldSpec: FieldValidationSpec<T, F>) {
        val validationSpec = fieldSpec.toValidationSpec(newSpecName = specName, fieldNames = fieldNames)
        validationProcessItems.add(validationSpec)
    }

    @Deprecated("use confirm for reuse",replaceWith = ReplaceWith("Validator#confirm"))
    fun <F> spec(fieldSpec: FieldValidationSpec<T, F>) {
        spec("", fieldSpec)
    }

    fun <F> confirm(specName: String,targetFun: T.() -> F, spec:Validator<F>) {
        confirm(specName, targetFun) {spec}
    }

    fun <F> confirm(targetFun: T.() -> F, spec:Validator<F>) {
        confirm("", targetFun) {spec}
    }

    /**
     * nested spec
     * specName:[specName:nestedSpecName]
     * fieldName:[field1,field2,field1|field2.nestedField1]
     */
    fun <F> confirm(specName: String, targetFun: T.() -> F, specFun: T.() -> Validator<F>) {
        validationProcessItems.add(ValidationNestedResult(specName = specName,
                fieldNames = fieldNames,
                resultFun = { specFun.invoke(this).validateAll(targetFun.invoke(this)) }))
    }

    fun <F> confirm(targetFun: T.() -> F, specFun: T.() -> Validator<F>) {
        confirm("", targetFun, specFun)
    }

    /**
     * grouping validationProcessItems by field names
     */
    fun fieldNames(vararg fieldNames: String, fFlock: Validator<T>.() -> Unit) {
        //create new Validator using current validationProcessItems and specified fieldNames
        val fieldValidator = Validator(validationProcessItems, fieldNames.toList())
        fFlock.invoke(fieldValidator)
    }

    private fun execValidate(target: T, validateAll: Boolean): List<ValidationError> {

        val errors = mutableListOf<ValidationError>()

        validationProcessItems.forEach { item ->
            when (item) {
                is ValidationSpec<T> -> {
                    if (!item.isValid(target)) {

                        val error = ValidationError(specName = item.specName, fieldNames = item.fieldNames, errorMessage = item.showMessage(target))
                        errors.add(error)

                        if (!validateAll) return errors
                    }
                }

                is ValidationNestedResult<T> -> {

                    val nestedErrors = item.resultFun.invoke(target)
                    if (nestedErrors.hasErrors()) {

                        nestedErrors.errors.forEach { nestedError ->


                            //直感的にわかるようなフィールド名のセット。加えて、ValidationErrorsからフィルタできるように
                            //例:[field1,field2,field1|field2.nestedField1]
                            val fieldNamePrefix = item.fieldNames.joinToString(separator = "|")
                            val nestedFieldNames = nestedError.fieldNames.map { fieldName -> "$fieldNamePrefix.$fieldName".trim('.') }


                            //直感的にわかるようなフィールド名のセット。加えて、ValidationErrorsからフィルタできるように
                            //例:[specName:nestedSpecName]
                            val specName = "${item.specName}:${nestedError.specName}".trim(':')

                            val error = ValidationError(specName = specName
                                    , fieldNames = item.fieldNames + nestedFieldNames
                                    , errorMessage = nestedError.errorMessage)

                            errors.add(error)

                        }
                        if (!validateAll) return errors
                    }
                }
            }

        }
        return errors
    }

    /**
     * execute validation
     */
    fun validateAll(target: T) = ValidationErrors(execValidate(target = target, validateAll = true))

    fun validateUntilFirst(target: T) = ValidationErrors(execValidate(target = target, validateAll = false))

    fun isValid(target: T) = !validateUntilFirst(target).hasErrors()

    fun assert(target:T){
        assert(isValid(target)){"spec not satisfied:\n"+validateAll(target)}
    }

    fun registeredFields() = validationProcessItems.map { it.fieldNames }.flatten().distinct()

    fun registeredSpecNames() = validationProcessItems.map { it.specName }.filter { it != "" }.distinct()
}