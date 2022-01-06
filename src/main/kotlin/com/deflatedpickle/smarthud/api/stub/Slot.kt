/* Copyright (c) 2022 ChloeDawn,DeflatedPickle under the APACHE-2.0 license */

package com.deflatedpickle.smarthud.api.stub

import com.deflatedpickle.smarthud.impl.stub.Stack
import com.deflatedpickle.smarthud.util.toStack
import com.deflatedpickle.smarthud.util.toStackList
import net.minecraft.entity.player.PlayerEntity

enum class Slot : Minecraftable<Stack> {
    MAIN_HAND,
    OFF_HAND,
    FEET,
    LEGS,
    CHEST,
    HEAD;

    override fun toMinecraft(player: PlayerEntity): Stack = when (this) {
        MAIN_HAND -> player.inventory.mainHandStack.toStack()
        OFF_HAND -> player.inventory.offHand.toStackList()[0]
        HEAD -> player.inventory.armor[3].toStack()
        CHEST -> player.inventory.armor[2].toStack()
        LEGS -> player.inventory.armor[1].toStack()
        FEET -> player.inventory.armor[0].toStack()
    }
}