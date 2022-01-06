/* Copyright (c) 2022 ChloeDawn,DeflatedPickle under the APACHE-2.0 license */

package com.deflatedpickle.smarthud.util

import com.deflatedpickle.smarthud.api.stub.Slot
import com.deflatedpickle.smarthud.impl.stub.Stack
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ArmorItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.registry.Registry
import kotlin.reflect.full.allSuperclasses

fun ItemStack.toStack() = Stack(
    Registry.ITEM.getId(item).toString(),
    count,
    mutableListOf(item::class.simpleName!!).apply {
        addAll(item::class.allSuperclasses.map { it.simpleName!! })
    },
    when (val i = item) {
        is ArmorItem -> {
            when (i.slotType) {
                EquipmentSlot.FEET -> Slot.FEET
                EquipmentSlot.LEGS -> Slot.LEGS
                EquipmentSlot.CHEST -> Slot.CHEST
                EquipmentSlot.HEAD -> Slot.HEAD
                else -> Slot.MAIN_HAND
            }
        }
        else -> Slot.MAIN_HAND
    }
)
