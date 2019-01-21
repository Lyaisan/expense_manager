package com.lalmeeva.expense.data.entity

class BillPosition {
    var name: String? = null
    var price: Double = 0.toDouble()
    var count: Double = 0.toDouble()

    override fun toString(): String {
        return "\t$name\n"
    }
}