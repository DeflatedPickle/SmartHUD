/* Copyright (c) 2022 ChloeDawn,DeflatedPickle under the APACHE-2.0 license */

package com.deflatedpickle.smarthud.api.stub

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.collection.DefaultedList

enum class Inventory : Minecraftable<List<ItemStack>> {
    HOTBAR,
    MAIN,
    ARMOUR,
    MAIN_HAND,
    OFF_HAND;

    override fun toMinecraft(player: PlayerEntity): MutableList<ItemStack> = when (this) {
        HOTBAR -> player.inventory.main.subList(0, 8)
        MAIN -> player.inventory.main.subList(9, player.inventory.main.size)
        ARMOUR -> player.inventory.armor
        MAIN_HAND -> DefaultedList.ofSize(1, player.inventory.mainHandStack)
        OFF_HAND -> player.inventory.offHand
    }
}