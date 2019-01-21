package com.lalmeeva.expense.data.source

import com.lalmeeva.expense.data.entity.BillParams
import com.lalmeeva.expense.data.entity.BillResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface RestService {

    @POST("/api/bill/")
    fun getBill(@Body params: List<BillParams>): Single<BillResponse>
}