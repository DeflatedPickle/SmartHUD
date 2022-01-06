/* Copyright (c) 2022 ChloeDawn,DeflatedPickle under the APACHE-2.0 license */

@file:Suppress("PropertyName")

package assets.smarthud.config

import com.deflatedpickle.smarthud.MinecraftAssets.HOTBAR_WIDTH
import com.deflatedpickle.smarthud.SmartHUDReheated.DISTANCE
import com.deflatedpickle.smarthud.SmartHUDReheated.SIZE
import com.deflatedpickle.smarthud.api.Alignment
import com.deflatedpickle.smarthud.api.Towards
import com.deflatedpickle.smarthud.api.stub.Inventory
import com.deflatedpickle.smarthud.api.stub.Slot
import com.deflatedpickle.smarthud.impl.Dodge
import com.deflatedpickle.smarthud.impl.Position
import com.deflatedpickle.smarthud.impl.Section
import com.deflatedpickle.smarthud.util.containsAny

val CompassItem = "class_1759"
val FireworkRocketItem = "class_1781"
val ArrowItem = "class_1744"
val ArmorItem = "class_1738"

listOf(
    // Navigation/Time
    Section(
        position = Position(
            vertical = Alignment.END,
        ),
        offset = Pair(HOTBAR_WIDTH / 2 + SIZE / 2 + DISTANCE, 0),
        items = listOf(
        	{ it.name == "minecraft:clock" },
        	{ it.kinds.containsAny("CompassItem", CompassItem) },
        )
    ),
    // Rockets
    Section(
        position = Position(
            vertical = Alignment.END,
        ),
        offset = Pair(-HOTBAR_WIDTH / 2 - SIZE / 2 - DISTANCE, 0),
        items = listOf { it.kinds.containsAny("FireworkRocketItem", FireworkRocketItem) },
        dodge = Dodge(
            upon = {
                it.held[Slot.OFF_HAND]!!.name != "minecraft:air"
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
        offset = Pair(-HOTBAR_WIDTH / 2 - SIZE / 2 - DISTANCE, 0),
        items = listOf { it.kinds.containsAny("ArrowItem", ArrowItem) },
        dodge = Dodge(
            upon = { player ->
                player.held[Slot.OFF_HAND]!!.name != "minecraft:air" ||
                        player.inventories[Inventory.MAIN]!!.any { it.kinds.containsAny("FireworkRocketItem", FireworkRocketItem) }
            },
            offset = Pair(0, -SIZE + 1),
        ),
        towards = Towards.LEFT,
        limit = 1,
    ),
    // Armour
    Section(
        position = Position(
            vertical = Alignment.END,
        ),
        offset = Pair(HOTBAR_WIDTH / 2 + SIZE / 2 + DISTANCE, 0),
        items = listOf(
            { it.kinds.containsAny("ArmorItem", ArmorItem) && it.slot == Slot.HEAD },
            { it.name == "minecraft:elytra" || it.kinds.containsAny("ArmorItem", ArmorItem) && it.slot == Slot.CHEST },
            { it.kinds.containsAny("ArmorItem", ArmorItem) && it.slot == Slot.LEGS },
            { it.kinds.containsAny("ArmorItem", ArmorItem) && it.slot == Slot.FEET },
        ),
        dodge = Dodge(
            upon = { player ->
                player.inventories[Inventory.MAIN]!!.any { it.name == "minecraft:clock" || it.kinds.containsAny("CompassItem", CompassItem) }
            },
            offset = Pair(0, -SIZE + 1),
        ),
        inventories = listOf(
            Inventory.ARMOUR,
        ),
    ),
)
