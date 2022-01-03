/* Copyright (c) 2022 ChloeDawn,DeflatedPickle under the APACHE-2.0 license */

package com.deflatedpickle.smarthud.impl

import com.deflatedpickle.smarthud.api.Orientation
import net.minecraft.item.Item

data class Section(
    val position: Position,
    val orientation: Orientation = Orientation.HORIZONTAL,
    val offset: Pair<Int, Int> = Pair(0, 0),
    val items: List<Item>,
    val dodge: Dodge = Dodge(),
)
