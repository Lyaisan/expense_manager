package com.lalmeeva.expense.data.entity

class BillResponsePayload {
    var date: String? = null
    var company: String? = null
    var positions: List<BillPosition>? = null

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("$date - $company\n", date, company)
        for (position in positions!!) {
            stringBuilder.append(position.toString())
        }
        stringBuilder.append("\n")
        return stringBuilder.toString()
    }
}