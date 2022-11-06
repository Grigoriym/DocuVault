package com.grappim.domain.utils

fun <T : Any> uninitializedStateFlow(): StateFlowWithoutInitialValue<T> =
    StateFlowWithoutInitialValueImpl()