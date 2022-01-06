/* Copyright (c) 2022 ChloeDawn,DeflatedPickle under the APACHE-2.0 license */

package com.deflatedpickle.smarthud.util

import com.deflatedpickle.smarthud.impl.Stack
import net.minecraft.item.ItemStack
import net.minecraft.util.registry.Registry

fun ItemStack.toStack() = Stack(
    Registry.ITEM.getId(item).toString(),
    count,
)
