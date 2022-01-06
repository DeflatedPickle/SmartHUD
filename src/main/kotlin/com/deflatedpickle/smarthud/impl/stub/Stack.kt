/* Copyright (c) 2022 ChloeDawn,DeflatedPickle under the APACHE-2.0 license */

package com.deflatedpickle.smarthud.impl.stub

import com.deflatedpickle.smarthud.api.stub.Slot

data class Stack(
    val name: String,
    val count: Int,
    val kinds: List<String>,
    val slot: Slot,
)