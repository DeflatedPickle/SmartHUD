/* Copyright (c) 2022 ChloeDawn,DeflatedPickle under the APACHE-2.0 license */

package com.deflatedpickle.smarthud.util

import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.Item

fun PlayerInventory.getSlotWithItem(item: Item): Int {
    for ((i, s) in this.main.withIndex()) {
        if (s.isEmpty || s.item != item) continue
        return i
    }
    return -1
}
