/* Copyright (c) 2021-2022 ChloeDawn,DeflatedPickle under the APACHE-2.0 license */

package com.deflatedpickle.smarthud.impl

import com.deflatedpickle.smarthud.api.Alignment

data class Position(
    val horizontal: Alignment = Alignment.CENTRE,
    val vertical: Alignment = Alignment.CENTRE,
)
