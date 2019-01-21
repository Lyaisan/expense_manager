package com.lalmeeva.expense.data.source

import android.net.Uri
import com.lalmeeva.expense.data.entity.BillParams
import com.lalmeeva.expense.data.entity.BillResponse
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class BillRepository @Inject constructor(private val restServiceFactory: RestServiceFactory) {

    private fun getBills(billParamsList: List<BillParams>): Single<BillResponse> {
        return restServiceFactory.getService().getBill(billParamsList)
    }

    fun parseBills(barcodesDetected: Set<String>): Single<BillResponse> {
        val billEntities = ArrayList<BillParams>()
        val iterator = barcodesDetected.iterator()
        while (iterator.hasNext()) {
            val displayValue = iterator.next()
            val uri = Uri.parse("http://127.0.0.1?$displayValue")
            billEntities.add(
                BillParams(
                    uri.getQueryParameter("fn"),
                    uri.getQueryParameter("fp"),
                    uri.getQueryParameter("i"),
                    uri.getQueryParameter("n"),
                    uri.getQueryParameter("s"),
                    uri.getQueryParameter("t")
                )
            )
        }
        return getBills(billEntities)
    }
}