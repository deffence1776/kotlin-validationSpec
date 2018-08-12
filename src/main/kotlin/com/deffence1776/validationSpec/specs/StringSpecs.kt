package com.deffence1776.validationSpec.specs

open class ShouldNotBeBlank<T>(aTarget: String, msgFieldName: String) : BaseValidationSpec<String, T>(
        aTarget,
        { target -> target.isNotBlank() },
        { "$msgFieldName should not be blank." }
)

open class ShouldBeNumberFormat<T>(aTarget: String, msgFieldName: String) : BaseValidationSpec<String, T>(
        aTarget,
        { target -> target.toIntOrNull() != null },
        { "$msgFieldName should be Number." }
)

