/* Copyright (c) 2021 ChloeDawn,DeflatedPickle under the APACHE-2.0 license */

package com.deflatedpickle.smarthud

import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.gui.hud.InGameHud
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items

@Suppress("UNUSED")
object SmartHUDReheated : ClientModInitializer {
    const val MOD_ID = "$[id]"
    private const val NAME = "$[name]"
    private const val GROUP = "$[group]"
    private const val AUTHOR = "$[author]"
    private const val VERSION = "$[version]"

    const val DISTANCE = 7

    val options = mutableListOf(
        Items.CLOCK,
        Items.COMPASS,
    ).map { ItemStack(it) }

    var enabled = true

    override fun onInitializeClient() {
        println(listOf(MOD_ID, NAME, GROUP, AUTHOR, VERSION))

        KeyboardHandler.initialize()
    }

    fun drawSmartHUDBackground(inGameHud: InGameHud, matrixStack: MatrixStack) {
        if (enabled) {
            for (i in options.indices) {
                inGameHud.drawTexture(
                    matrixStack,
                    inGameHud.scaledWidth / 2 + 182 / 2 + DISTANCE + (21 * i),
                    inGameHud.scaledHeight - 22 - 1,
                    24, 22,
                    22, 24,
                )
            }
        }
    }

    fun drawSmartHUDItems(inGameHud: InGameHud, y: Int, playerEntity: PlayerEntity, partialTicks: Float, seed: Int) {
        if (enabled) {
            for ((ind, s) in options.withIndex()) {
                val slot = playerEntity.inventory.getSlotWithStack(s)

                if (slot != -1) {
                    inGameHud.renderHotbarItem(
                        inGameHud.scaledWidth / 2 + 182 / 2 + DISTANCE + 3 + (21 * ind), y,
                        partialTicks,
                        playerEntity,
                        playerEntity.inventory.main[slot],
                        seed,
                    )
                }
            }
        }
    }
}
