package com.deffence1776.validationSpec

import com.deffence1776.validationSpec.specs.BaseFieldValidationSpec


/**
 * Validator clas。
 * stocks validation specs and validate
 */
class Validator<T>(block: Validator<T>.() -> Unit) {

    private val specs: MutableList<Pair<List<String>, T.() -> Boolean>> = mutableListOf()
    private val specsMags: MutableMap<Int, T.() -> String> = mutableMapOf()

    init {
        block.invoke(this)
    }

    fun shouldBe(aSpec: T.() -> Boolean): Validator<T> {
        specs.add(Pair(emptyList(), { target: T -> aSpec.invoke(target) }))
        return this
    }

    fun shouldBe2(aSpec: T.() -> Boolean): Validator<T> {
        specs.add(Pair(emptyList(), { target: T -> aSpec.invoke(target) }))
        return this
    }



    fun errorMessage(msgFun: T.() -> String) {
        specsMags[specs.lastIndex] = msgFun
    }

    fun <F> field(vararg fieldNames: String, fBlock: FieldValidator<T, F>.() -> Unit): FieldValidator<T, F> {
        return FieldValidator(fieldNames.toList(), specs, specsMags, fBlock)
    }

    private fun execValidate(target: T) = specs.mapIndexedNotNull { index, (fieldNames, aSpec) ->
        if (!aSpec.invoke(target)) {
            val errorMessage = specsMags.getOrDefault(index) { "validation failed" }
            ValidationError(fieldNames = fieldNames, errorMessage = errorMessage.invoke(target))
        } else {
            null
        }
    }

    fun validate(target: T) = ValidationErrors(execValidate(target))

    /**
     * check registered field names for test
     */
    fun checkFieldNames(target: T) {
        val registeredFieldNamesSet=specs.map{ (fieldNames,_) -> fieldNames }.flatten().toSet()

        val t = target as Any
        val type = t::class.java
        val propetyNames=type.declaredFields.toSet().map {it.name }
        registeredFieldNamesSet.forEach { registeredName->
            require(propetyNames.contains(registeredName)) { "target Type [${type.name} ] doesn't has property named [$registeredName]. correct property Names $propetyNames" }
        }
    }
}

class FieldValidator<T, F>(private val fieldNames: List<String>
                           , private val specs: MutableList<Pair<List<String>, T.() -> Boolean>>
                           , private val specsMags: MutableMap<Int, (t: T) -> String>
                           , block: FieldValidator<T, F>.() -> Unit) {
    init {
        block.invoke(this)
    }

    fun shouldBe(aSpec: T.() -> Boolean): FieldValidator<T, F> {
        specs.add(Pair(fieldNames, aSpec))
        return this
    }

    fun errorMessage(msgFun: T.() -> String) {
        specsMags[specs.lastIndex] = msgFun
    }

    fun spec(spec: T.() -> BaseFieldValidationSpec<F, T>) {

        specs.add(Pair(fieldNames, { target: T ->
            val specValue = spec.invoke(target)
            specValue.aSpec.invoke(specValue.target)
        }))

        specsMags[specs.lastIndex] = { t: T ->
            spec.invoke(t).msgFun.invoke(t)
        }
    }
}


