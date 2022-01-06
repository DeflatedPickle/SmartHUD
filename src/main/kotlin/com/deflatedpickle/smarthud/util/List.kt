/* Copyright (c) 2022 ChloeDawn,DeflatedPickle under the APACHE-2.0 license */

package com.deflatedpickle.smarthud.util

import net.minecraft.item.ItemStack

fun List<String>.containsAny(vararg all: String) = this.any { it in all }

fun List<ItemStack>.toStackList() = this.toMutableList().map {
    it.toStack()
}