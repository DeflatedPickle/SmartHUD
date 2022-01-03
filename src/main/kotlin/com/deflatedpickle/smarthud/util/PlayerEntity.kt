/* Copyright (c) 2022 ChloeDawn,DeflatedPickle under the APACHE-2.0 license */

package com.deflatedpickle.smarthud.util

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.util.Hand

fun PlayerEntity.hasItem(item: Item) =
    inventory.getSlotWithItem(item) == -1 &&
        !armorItems.any { it.item == item } &&
        getStackInHand(Hand.OFF_HAND).item != item
