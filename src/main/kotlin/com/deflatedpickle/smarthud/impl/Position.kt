/* Copyright (c) 2021-2022 ChloeDawn,DeflatedPickle under the APACHE-2.0 license */

package com.deflatedpickle.smarthud.impl

import com.deflatedpickle.smarthud.api.Horizontal
import com.deflatedpickle.smarthud.api.Vertical

data class Position(
    val horizontal: Horizontal = Horizontal.CENTRE,
    val vertical: Vertical = Vertical.CENTER,
)
