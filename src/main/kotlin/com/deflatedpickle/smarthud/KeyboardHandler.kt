/* Copyright (c) 2021-2022 ChloeDawn,DeflatedPickle under the APACHE-2.0 license */

@file:Suppress("UNUSED_PARAMETER")

package com.deflatedpickle.smarthud

import com.deflatedpickle.smarthud.SmartHUDReheated.MOD_ID
import com.mojang.blaze3d.platform.InputUtil
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBind
import org.lwjgl.glfw.GLFW.GLFW_KEY_MINUS

object KeyboardHandler {
    private val toggleKeyBinding = KeyBind(
        "key.$MOD_ID.toggle",
        InputUtil.Type.KEYSYM,
        GLFW_KEY_MINUS,
        "key.$MOD_ID"
    )

    fun initialize() {
        KeyBindingHelper.registerKeyBinding(toggleKeyBinding)

        ClientTickEvents.END_CLIENT_TICK.register(::onTick)
    }

    private fun onTick(client: MinecraftClient) {
        if (toggleKeyBinding.wasPressed()) {
            SmartHUDReheated.enabled = !SmartHUDReheated.enabled
        }
    }
}
