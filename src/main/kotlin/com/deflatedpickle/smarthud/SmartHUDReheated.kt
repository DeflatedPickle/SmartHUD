/* Copyright (c) 2021-2022 ChloeDawn,DeflatedPickle under the APACHE-2.0 license */

@file:Suppress("UNCHECKED_CAST", "MemberVisibilityCanBePrivate")

package com.deflatedpickle.smarthud

import com.deflatedpickle.smarthud.api.Horizontal
import com.deflatedpickle.smarthud.api.Orientation
import com.deflatedpickle.smarthud.api.Vertical
import com.deflatedpickle.smarthud.impl.Position
import com.deflatedpickle.smarthud.util.getSlotWithItem
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.gui.hud.InGameHud
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.Items

@Suppress("UNUSED")
object SmartHUDReheated : ClientModInitializer {
    const val MOD_ID = "$[id]"
    private const val NAME = "$[name]"
    private const val GROUP = "$[group]"
    private const val AUTHOR = "$[author]"
    private const val VERSION = "$[version]"

    private const val DISTANCE = 7

    private const val POSITION = "pos"
    private const val ORIENTATION = "or"
    private const val OFFSET = "off"
    private const val ITEMS = "i"

    const val SIZE = 22

    private val sections = listOf(
        mapOf(
            POSITION to Position(
                vertical = Vertical.BOTTOM
            ),
            ORIENTATION to Orientation.HORIZONTAL,
            OFFSET to Pair(182 / 2 + 10, 0),
            ITEMS to mutableListOf(
                Items.CLOCK,
                Items.COMPASS,
            )
        )
    )

    var enabled = true

    override fun onInitializeClient() {
        println(listOf(MOD_ID, NAME, GROUP, AUTHOR, VERSION))

        KeyboardHandler.initialize()
    }

    fun drawSection(inGameHud: InGameHud, drawFunc: (Int, Int, Int, Item) -> Unit) {
        if (enabled) {
            for (s in sections) {
                val p = s[POSITION] as Position
                val or = s[ORIENTATION] as Orientation
                val o = s[OFFSET] as Pair<Int, Int>
                val l = (s[ITEMS] as List<Item>).size

                val x = when (p.horizontal) {
                    Horizontal.LEFT -> 0
                    Horizontal.CENTRE -> (inGameHud.scaledWidth / 2)
                    Horizontal.RIGHT -> when (or) {
                        Orientation.HORIZONTAL -> inGameHud.scaledWidth - SIZE
                        Orientation.VERTICAL -> inGameHud.scaledWidth - SIZE * l
                    }
                }

                val y = when (p.vertical) {
                    Vertical.TOP -> 0
                    Vertical.CENTER -> (inGameHud.scaledHeight / 2)
                    Vertical.BOTTOM -> when (or) {
                        Orientation.HORIZONTAL -> inGameHud.scaledHeight - SIZE
                        Orientation.VERTICAL -> inGameHud.scaledHeight - SIZE * l
                    }
                }

                for ((i, stack) in (s[ITEMS] as List<Item>).withIndex()) {
                    val x2 = when (or) {
                        Orientation.HORIZONTAL -> x + o.first + (SIZE - 1) * i
                        Orientation.VERTICAL -> x + o.first
                    }

                    val y2 = when (or) {
                        Orientation.HORIZONTAL -> y + o.second
                        Orientation.VERTICAL -> y + o.second + (SIZE - 1) * i
                    }

                    drawFunc(x2, y2, i, stack)
                }
            }
        }
    }

    fun drawSmartHUDBackground(inGameHud: InGameHud, matrixStack: MatrixStack) {
        drawSection(inGameHud) { x, y, _, _ ->
            inGameHud.drawTexture(
                matrixStack,
                x,
                y,
                60, 23,
                SIZE, SIZE,
            )
        }
    }

    fun drawSmartHUDItems(inGameHud: InGameHud, playerEntity: PlayerEntity, partialTicks: Float, seed: Int) {
        drawSection(inGameHud) { x, y, _, item ->
            val slot = playerEntity.inventory.getSlotWithItem(item)

            if (slot != -1) {
                inGameHud.renderHotbarItem(
                    x + 3, y + 3,
                    partialTicks,
                    playerEntity,
                    playerEntity.inventory.main[slot],
                    seed,
                )
            }
        }
    }
}
