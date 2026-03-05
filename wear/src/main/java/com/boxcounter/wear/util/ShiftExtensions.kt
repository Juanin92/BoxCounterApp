package com.boxcounter.wear.util

import com.boxcounter.core.entity.Shift

fun Shift.copy(
    quantity: Int = this.quantity,
    active: Boolean = this.isActive,
    startTime: Long? = this.startTime,
    endTime: Long? = this.endTime
): Shift{
    val newShift = Shift()
    newShift.id = this.id
    newShift.quantity = quantity
    newShift.startTime = startTime
    newShift.endTime = endTime
    newShift.isActive = active
    return newShift
}