/* Copyright (c) 2022 ChloeDawn,DeflatedPickle under the APACHE-2.0 license */

package com.deflatedpickle.smarthud.impl.stub

import com.deflatedpickle.smarthud.api.stub.Inventory
import com.deflatedpickle.smarthud.api.stub.Slot
import java.util.UUID

data class Player(
    val id: UUID,
    val name: String,
    val inventories: Map<Inventory, List<Stack>>,
    val held: Map<Slot, Stack>,
    val slot: Int,
)