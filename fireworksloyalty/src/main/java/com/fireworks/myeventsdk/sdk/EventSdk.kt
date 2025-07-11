package com.fireworks.myeventsdk.sdk

import android.content.Context
import com.fireworks.myeventsdk.NetworkService.Service
import com.fireworks.myeventsdk.Utils.CommonInterface.Callback
import com.fireworks.myeventsdk.Utils.CommonInterface.CategoryCallback
import com.fireworks.myeventsdk.Utils.CommonInterface.EventDetailCallback
import com.fireworks.myeventsdk.Utils.NetworkUtils
import com.fireworks.myeventsdk.model.Event
import com.fireworks.myeventsdk.model.rewards_search.SearchRewardItem
import digital.fireworks.kpdrm.data.dto.events_detail.Detail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//test
object EventSdk {

    private lateinit var retrofitService: Service

    fun init(baseUrl: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofitService = retrofit.create(Service::class.java)
    }

    fun fetchEvents(
        context: Context,
        sectoken: String,
        custId: String,
        vcKey: String,
        category: String? = "0",
        searchTerm: String? = "",
        mall: String = "12",
        isLatest: String = "1",
        extraParams: Map<String, String> = emptyMap(),
        callback: Callback
    ) {
        val fields = mutableMapOf(
            "cat_filter" to (category ?: ""),
            "search_item" to (searchTerm ?: ""),
            "mall" to mall,
            "latest" to isLatest,
            "custcode" to custId,
            "date" to System.currentTimeMillis().toString(),
            "vc" to vcKey
        )

        fields.putAll(extraParams)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = retrofitService.getEvents(
                    sectoken = sectoken,
                    fields = fields
                )

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        callback.onSuccess(response.body()!!.events)
                    } else {
                        callback.onFailure("API failed with status: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback.onFailure("Error: ${e.localizedMessage}")
                }
            }
        }
    }



    fun fetchEventDetail(
        context: Context,
        eventId: String,
        token: String,
        custId: String?,
        extraParams: Map<String, String> = emptyMap(),
        callback: EventDetailCallback
    ) {
        val fieldMap = mutableMapOf(
            "eventid" to eventId,
            "date" to System.currentTimeMillis().toString(),
            "mall" to "12",
            "vc" to NetworkUtils.getVCKey()
        )

        custId?.let { fieldMap["custcode"] = it }

        // Add any extra fields from host app
        fieldMap.putAll(extraParams)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = retrofitService.getEventsDetails(
                    secToken = token,
                    fields = fieldMap
                )

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val body = response.body()!!
                        if (body.status.equals("failed", ignoreCase = true)) {
                            callback.onFailure(body.message ?: "Unknown error")
                        } else {
                            callback.onSuccess(body.details.firstOrNull())
                        }
                    } else {
                        callback.onFailure("API failed: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback.onFailure(e.localizedMessage ?: "Network error")
                }
            }
        }
    }




}