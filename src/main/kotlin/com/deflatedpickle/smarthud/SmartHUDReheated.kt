/* Copyright (c) 2021-2022 ChloeDawn,DeflatedPickle under the APACHE-2.0 license */

@file:Suppress("UNCHECKED_CAST", "MemberVisibilityCanBePrivate")

package com.deflatedpickle.smarthud

import com.deflatedpickle.smarthud.api.Horizontal
import com.deflatedpickle.smarthud.api.Orientation
import com.deflatedpickle.smarthud.api.Vertical
import com.deflatedpickle.smarthud.impl.Dodge
import com.deflatedpickle.smarthud.impl.Position
import com.deflatedpickle.smarthud.impl.Section
import com.deflatedpickle.smarthud.util.getSlotWithItem
import com.deflatedpickle.smarthud.util.hasItem
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.hud.InGameHud
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.util.Arm
import net.minecraft.util.Hand

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
        Section(
            position = Position(
                vertical = Vertical.BOTTOM
            ),
            orientation = Orientation.HORIZONTAL,
            offset = Pair(182 / 2 + SIZE / 2 + 7, 0),
            items = listOf(
                Items.CLOCK,
                Items.COMPASS,
            )
        ),
        Section(
            position = Position(
                vertical = Vertical.BOTTOM
            ),
            orientation = Orientation.HORIZONTAL,
            offset = Pair(-182 / 2 - SIZE / 2 - 7, 0),
            items = listOf(
                Items.ELYTRA,
            ),
            dodge = Dodge(
                upon = {
                    it.getStackInHand(Hand.OFF_HAND).item != Items.AIR
                },
                offset = Pair(-SIZE + 1, 0)
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
            MinecraftClient.getInstance().player?.let { player ->
                for (s in sections) {
                    val pos = s.position
                    val or = s.orientation
                    val o = s.offset
                    val items = s.items
                    val count = items.size
                    val dodge = s.dodge

                    var x = when (pos.horizontal) {
                        Horizontal.LEFT -> 0
                        Horizontal.CENTRE -> (inGameHud.scaledWidth / 2) - (SIZE / 2)
                        Horizontal.RIGHT -> when (or) {
                            Orientation.HORIZONTAL -> inGameHud.scaledWidth - SIZE
                            Orientation.VERTICAL -> inGameHud.scaledWidth - SIZE * count
                        }
                    }

                    var y = when (pos.vertical) {
                        Vertical.TOP -> 0
                        Vertical.CENTER -> (inGameHud.scaledHeight / 2) - (SIZE / 2)
                        Vertical.BOTTOM -> when (or) {
                            Orientation.HORIZONTAL -> inGameHud.scaledHeight - SIZE
                            Orientation.VERTICAL -> inGameHud.scaledHeight - SIZE * count
                        }
                    }

                    if (dodge.upon(player)) {
                        x += dodge.offset.first
                        y += dodge.offset.second
                    }

                    var collectedWidth = 0
                    var collectedHeight = 0

                    for ((i, item) in items.withIndex()) {
                        if (player.hasItem(item)) continue

                        val x2 = when (or) {
                            Orientation.HORIZONTAL -> x + o.first + collectedWidth.apply { collectedWidth += SIZE - 1 }
                            Orientation.VERTICAL -> x + o.first
                        }

                        val y2 = when (or) {
                            Orientation.HORIZONTAL -> y + o.second
                            Orientation.VERTICAL -> y + o.second + collectedHeight.apply { collectedHeight += SIZE - 1 }
                        }

                        drawFunc(
                            when (player.mainArm) {
                                Arm.LEFT -> inGameHud.scaledWidth - SIZE - 1 + x2 * -1
                                else -> x2
                            },
                            y2,
                            i, item
                        )
                    }
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

            val stack = if (slot != -1) {
                playerEntity.inventory.main[slot]
            } else if (playerEntity.armorItems.any { it.item == item }) {
                playerEntity.armorItems.first { it.item == item }
            } else null

            stack?.let {
                inGameHud.renderHotbarItem(
                    x + 3, y + 3,
                    partialTicks,
                    playerEntity,
                    stack,
                    seed,
                )
            }
        }
    }
}
