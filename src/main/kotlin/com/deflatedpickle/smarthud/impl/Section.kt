/* Copyright (c) 2022 ChloeDawn,DeflatedPickle under the APACHE-2.0 license */

package com.deflatedpickle.smarthud.impl

import com.deflatedpickle.smarthud.api.Inventory
import com.deflatedpickle.smarthud.api.Orientation
import com.deflatedpickle.smarthud.api.Towards
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack

data class Section(
    val position: Position = Position(),
    val orientation: Orientation = Orientation.HORIZONTAL,
    val offset: Pair<Int, Int> = Pair(0, 0),
    val items: List<(ItemStack) -> Boolean>,
    val dodge: Dodge = Dodge(),
    val show: (PlayerEntity) -> Boolean = { true },
    val towards: Towards = Towards.RIGHT,
    val limit: Int = 1,
    val inventories: List<Inventory> = listOf(Inventory.MAIN),
)
