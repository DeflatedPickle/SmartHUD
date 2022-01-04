/* Copyright (c) 2021-2022 ChloeDawn,DeflatedPickle under the APACHE-2.0 license */

@file:Suppress("UNCHECKED_CAST", "MemberVisibilityCanBePrivate")

package com.deflatedpickle.smarthud

import com.deflatedpickle.smarthud.api.Alignment
import com.deflatedpickle.smarthud.api.Inventory
import com.deflatedpickle.smarthud.api.Orientation
import com.deflatedpickle.smarthud.impl.Section
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.hud.InGameHud
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Arm
import net.minecraft.util.collection.DefaultedList
import java.io.File
import kotlin.script.experimental.api.ResultValue
import kotlin.script.experimental.api.ResultWithDiagnostics
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.ScriptEvaluationConfiguration
import kotlin.script.experimental.api.defaultImports
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost

@Suppress("UNUSED")
object SmartHUDReheated : ClientModInitializer {
    const val MOD_ID = "$[id]"
    private const val NAME = "$[name]"
    private const val GROUP = "$[group]"
    private const val AUTHOR = "$[author]"
    private const val VERSION = "$[version]"

    const val DISTANCE = 7
    const val SIZE = 22

    private lateinit var sections: List<Section>

    var enabled = true

    override fun onInitializeClient() {
        println(listOf(MOD_ID, NAME, GROUP, AUTHOR, VERSION))

        KeyboardHandler.initialize()

        val file = File("${System.getProperty("user.dir")}/config/smarthud.kts")

        val cc = ScriptCompilationConfiguration {
            jvm {
                dependenciesFromCurrentContext(
                    wholeClasspath = true
                )
            }
            defaultImports(
                "com.deflatedpickle.smarthud.SmartHUDReheated.SIZE",
                "com.deflatedpickle.smarthud.api.*",
                "com.deflatedpickle.smarthud.impl.*",
            )
        }

        val ec = ScriptEvaluationConfiguration {
        }

        val eval = BasicJvmScriptingHost().eval(file.toScriptSource(), cc, ec)

        when (eval) {
            is ResultWithDiagnostics.Success -> {
                when (val result = eval.value.returnValue) {
                    is ResultValue.Value -> {
                        sections = result.value as List<Section>
                    }
                    is ResultValue.Unit -> TODO()
                    is ResultValue.Error -> TODO()
                    ResultValue.NotEvaluated -> TODO()
                }
            }
            is ResultWithDiagnostics.Failure -> {}
        }
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
