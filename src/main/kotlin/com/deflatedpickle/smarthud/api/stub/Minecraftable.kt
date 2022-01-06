/* Copyright (c) 2022 ChloeDawn,DeflatedPickle under the APACHE-2.0 license */

package com.deflatedpickle.smarthud.api.stub

import net.minecraft.entity.player.PlayerEntity

interface Minecraftable<T> {
    fun toMinecraft(player: PlayerEntity): T
}