/* Copyright (c) 2021-2022 ChloeDawn,DeflatedPickle under the APACHE-2.0 license */

@file:Suppress("MemberVisibilityCanBePrivate")

package com.deflatedpickle.smarthud

import com.deflatedpickle.smarthud.api.Alignment
import com.deflatedpickle.smarthud.api.Inventory
import com.deflatedpickle.smarthud.api.Orientation
import com.deflatedpickle.smarthud.api.Towards
import com.deflatedpickle.smarthud.impl.Dodge
import com.deflatedpickle.smarthud.impl.Position
import com.deflatedpickle.smarthud.impl.Section
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.hud.InGameHud
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArrowItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.Arm
import net.minecraft.util.Hand
import net.minecraft.util.collection.DefaultedList
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer

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
        // Navigation/Time
        Section(
            position = Position(
                vertical = Alignment.END,
            ),
            offset = Pair(182 / 2 + SIZE / 2 + 7, 0),
            items = listOf(
                { it.item == Items.CLOCK },
                { it.item == Items.COMPASS },
            )
        ),
        // Rockets
        Section(
            position = Position(
                vertical = Alignment.END,
            ),
            offset = Pair(-182 / 2 - SIZE / 2 - 7, 0),
            items = listOf { it.item == Items.FIREWORK_ROCKET },
            dodge = Dodge(
                upon = {
                    it.getStackInHand(Hand.OFF_HAND).item != Items.AIR
                },
                offset = Pair(-SIZE + 1, 0),
            ),
            towards = Towards.LEFT,
        ),
        // Arrows
        Section(
            position = Position(
                vertical = Alignment.END,
            ),
            offset = Pair(-182 / 2 - SIZE / 2 - 7, 0),
            items = listOf { it.item is ArrowItem },
            dodge = Dodge(
                upon = { player ->
                    player.getStackInHand(Hand.OFF_HAND).item != Items.AIR ||
                        player.inventory.main.any { it.item == Items.FIREWORK_ROCKET }
                },
                offset = Pair(0, -SIZE + 1),
            ),
            towards = Towards.LEFT,
            limit = -1,
        ),
        // Armour
        Section(
            position = Position(
                vertical = Alignment.END,
            ),
            offset = Pair(182 / 2 + SIZE / 2 + 7, 0),
            items = listOf(
                { it.item is ArmorItem && (it.item as ArmorItem).slotType == EquipmentSlot.HEAD },
                { it.item == Items.ELYTRA || it.item is ArmorItem && (it.item as ArmorItem).slotType == EquipmentSlot.CHEST },
                { it.item is ArmorItem && (it.item as ArmorItem).slotType == EquipmentSlot.LEGS },
                { it.item is ArmorItem && (it.item as ArmorItem).slotType == EquipmentSlot.FEET },
            ),
            dodge = Dodge(
                upon = { player ->
                    player.inventory.main.any { it.item == Items.CLOCK || it.item == Items.COMPASS }
                },
                offset = Pair(0, -SIZE + 1),
            ),
            inventories = listOf(
                Inventory.ARMOUR,
            ),
        ),
    )

    var enabled = true

    override fun onInitializeClient(mod: ModContainer) {
        println(listOf(MOD_ID, NAME, GROUP, AUTHOR, VERSION))

        KeyboardHandler.initialize()
    }

    fun drawSection(inGameHud: InGameHud, drawFunc: (Int, Int, Int, ItemStack) -> Unit) {
        if (enabled) {
            MinecraftClient.getInstance().player?.let { player ->
                for (s in sections) {
                    val pos = s.position
                    val or = s.orientation
                    val o = s.offset
                    val items = s.items
                    val count = items.size
                    val dodge = s.dodge
                    val towards = s.towards

                    if (!s.show(player)) continue

                    var x = when (pos.horizontal) {
                        Alignment.START -> 0
                        Alignment.CENTRE -> (inGameHud.scaledWidth / 2) - (SIZE / 2)
                        Alignment.END -> when (or) {
                            Orientation.HORIZONTAL -> inGameHud.scaledWidth - SIZE * count
                            Orientation.VERTICAL -> inGameHud.scaledWidth - SIZE
                        }
                    }

                    var y = when (pos.vertical) {
                        Alignment.START -> 0
                        Alignment.CENTRE -> (inGameHud.scaledHeight / 2) - (SIZE / 2)
                        Alignment.END -> when (or) {
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

                    val collectedKinds = mutableMapOf<Item, Int>()

                    val inventories = mutableListOf<DefaultedList<ItemStack>>().apply {
                        for (i in s.inventories) {
                            add(
                                when (i) {
                                    Inventory.MAIN -> player.inventory.main
                                    Inventory.ARMOUR -> player.inventory.armor
                                    Inventory.OFFHAND -> player.inventory.offHand
                                }
                            )
                        }
                    }

                    for (inventory in inventories) {
                        for ((i, stack) in inventory.withIndex()) {
                            for (item in items) {
                                if (!item(stack)) continue
                                collectedKinds.putIfAbsent(stack.item, 0)
                                if (s.limit != -1 && collectedKinds[stack.item]!! >= s.limit) continue
                                if (inventory.size == 36 && inventory.indexOf(stack) <= 9) continue

                                collectedKinds[stack.item] = collectedKinds[stack.item]!! + 1

                                val x2 = when (or) {
                                    Orientation.HORIZONTAL -> x + o.first + collectedWidth.apply { collectedWidth += SIZE - 1 } * towards.direction
                                    Orientation.VERTICAL -> x + o.first
                                }

                                val y2 = when (or) {
                                    Orientation.HORIZONTAL -> y + o.second
                                    Orientation.VERTICAL -> y + o.second + collectedHeight.apply { collectedHeight += SIZE - 1 } * towards.direction
                                }

                                drawFunc(
                                    when (player.mainArm) {
                                        Arm.LEFT -> inGameHud.scaledWidth - SIZE - 1 + x2 * -1
                                        else -> x2
                                    },
                                    y2,
                                    i, stack
                                )
                            }
                        }
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
        drawSection(inGameHud) { x, y, _, stack ->
            stack.let {
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
