/* Copyright (c) 2022 ChloeDawn,DeflatedPickle under the APACHE-2.0 license */

package com.deflatedpickle.smarthud.util

import com.deflatedpickle.smarthud.api.stub.Inventory
import com.deflatedpickle.smarthud.api.stub.Slot
import com.deflatedpickle.smarthud.impl.stub.Player
import net.minecraft.entity.player.PlayerEntity

fun PlayerEntity.toPlayer() = Player(
    this.uuid,
    this.entityName,
    Inventory.values().zip(Inventory.values().map { it.toMinecraft(this).toStackList() }).toMap(),
    Slot.values().zip(Slot.values().map { it.toMinecraft(this) }).toMap(),
    inventory.selectedSlot,
)
