/* Copyright (c) 2022 ChloeDawn,DeflatedPickle under the APACHE-2.0 license */

package com.deflatedpickle.smarthud.impl

import net.minecraft.entity.player.PlayerEntity

data class Dodge(
    val upon: (PlayerEntity) -> Boolean = { false },
    val offset: Pair<Int, Int> = Pair(0, 0),
)
