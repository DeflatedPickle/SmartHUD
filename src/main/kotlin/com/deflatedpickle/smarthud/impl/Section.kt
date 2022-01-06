/* Copyright (c) 2022 ChloeDawn,DeflatedPickle under the APACHE-2.0 license */

package com.deflatedpickle.smarthud.impl

import com.deflatedpickle.smarthud.api.Inventory
import com.deflatedpickle.smarthud.api.Orientation
import com.deflatedpickle.smarthud.api.Towards

data class Section(
    /**
     * The horizontal and vertical alignment of this section
     */
    val position: Position = Position(),
    /**
     * The orientation these slots will be laid in
     */
    val orientation: Orientation = Orientation.HORIZONTAL,
    /**
     * An offset from the position
     */
    val offset: Pair<Int, Int> = Pair(0, 0),
    /**
     * A list of boolean checks for the items that will show in slots for this section
     */
    val items: List<(Stack) -> Boolean>,
    /**
     * An additional offset based on a lambda result
     */
    val dodge: Dodge = Dodge(),
    /**
     * Shows or hides this section
     */
    val show: (Player) -> Boolean = { true },
    /**
     * The direction item slots will go
     */
    val towards: Towards = Towards.RIGHT,
    /**
     * The amount of slots these items will get
     */
    val limit: Int = 1,
    /**
     * The inventories these items will be looked for in
     */
    val inventories: List<Inventory> = listOf(Inventory.MAIN),
)
