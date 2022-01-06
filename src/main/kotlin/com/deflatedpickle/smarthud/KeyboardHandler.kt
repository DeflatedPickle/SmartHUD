/* Copyright (c) 2021-2022 ChloeDawn,DeflatedPickle under the APACHE-2.0 license */

@file:Suppress("UNUSED_PARAMETER")

package com.deflatedpickle.smarthud

import com.deflatedpickle.smarthud.SmartHUDReheated.MOD_ID
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW.GLFW_KEY_GRAVE_ACCENT
import org.lwjgl.glfw.GLFW.GLFW_KEY_MINUS

object KeyboardHandler {
    private val toggleKeyBinding = KeyBinding(
        "key.$MOD_ID.toggle",
        InputUtil.Type.KEYSYM,
        GLFW_KEY_MINUS,
        "key.$MOD_ID"
    )

    private val reloadKeyBinding = KeyBinding(
        "key.$MOD_ID.reload",
        InputUtil.Type.KEYSYM,
        GLFW_KEY_GRAVE_ACCENT,
        "key.$MOD_ID"
    )

    fun initialize() {
        KeyBindingHelper.registerKeyBinding(toggleKeyBinding)
        KeyBindingHelper.registerKeyBinding(reloadKeyBinding)

        ClientTickEvents.END_CLIENT_TICK.register(::onTick)
    }

    private fun onTick(client: MinecraftClient) {
        when {
            toggleKeyBinding.wasPressed() -> SmartHUDReheated.enabled = !SmartHUDReheated.enabled
            reloadKeyBinding.wasPressed() -> Scripting.reloadConfig(true)
        }
    }
}
