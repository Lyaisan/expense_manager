package com.lalmeeva.expense.data.entity

data class BillParams(val fn: String?, val fp: String?, val i: String?, val n: String?, val s: String?, val t: String?) {

    override fun toString(): String {
        return "s: $s, fp: $fp\n"
    }
}