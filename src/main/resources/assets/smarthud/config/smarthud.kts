/* Copyright (c) 2022 ChloeDawn,DeflatedPickle under the APACHE-2.0 license */

package assets.smarthud.config

import com.deflatedpickle.smarthud.SmartHUDReheated.DISTANCE
import com.deflatedpickle.smarthud.SmartHUDReheated.SIZE
import com.deflatedpickle.smarthud.api.Alignment
import com.deflatedpickle.smarthud.impl.Position
import com.deflatedpickle.smarthud.impl.Section

listOf(
    Section(
        position = Position(
            vertical = Alignment.END,
        ),
        offset = Pair(182 / 2 + SIZE / 2 + DISTANCE, 0),
        items = listOf(
        	{ it.name == "minecraft:clock" },
        	{ it.name == "minecraft:compass" },
        )
    ),
)
