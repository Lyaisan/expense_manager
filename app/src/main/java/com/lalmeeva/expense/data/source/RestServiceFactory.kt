package com.lalmeeva.expense.data.source

import com.google.gson.Gson
import com.lalmeeva.expense.utils.CommunicationUtils
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RestServiceFactory(private val httpClient: OkHttpClient,
                         private val gson: Gson/*, SharedPreferences userSharedPreferences*/) {

    @Throws(InstantiationException::class)
    fun getService(): RestService {
        val serverUrl = CommunicationUtils.SERVER_URL + ":" + CommunicationUtils.SERVER_PORT
        if (serverUrl.isEmpty()) {
            throw InstantiationException()
        }
        val newRequestBuilder = Retrofit.Builder()
            .baseUrl(serverUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build()
        return newRequestBuilder.create(RestService::class.java)
    }
}