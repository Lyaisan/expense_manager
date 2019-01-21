package com.lalmeeva.expense.data.entity

class BillResponse {
    private var bills: List<BillResponsePayload> = ArrayList()
    private var failed: List<BillParams> = ArrayList()

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("failed:\n")
        stringBuilder.append(failed.toString())
        stringBuilder.append("\n\nbills:\n")
        stringBuilder.append(bills.toString())
        return stringBuilder.toString()
    }
}