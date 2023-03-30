package de.rehatech2223.lgg_frontend.services

import android.util.Log
import de.rehatech2223.datamodel.FunctionDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class FunctionService {

    fun getFunctionInfo(functionId: Long): FunctionDTO? {
        Log.d("handler", "requested function id: $functionId")
        var functionDTO: FunctionDTO? = null
        runBlocking {
            val request = Request.Builder()
                .url(ServiceProvider.baseUrl + "function?functionId=$functionId")
                .get()
                .build()
            launch(Dispatchers.IO) {
                ServiceProvider.connectionSaveCall {
                    ServiceProvider.client.newCall(request).execute().use { response ->
                        if (!response.isSuccessful || response.code != 200) {
                            Log.d("handler", "canceled")
                            Log.d("handler", "body: ${if(response.body != null) response.body!!.string() else null}")
                            cancel()
                        } else {
                            val jsonBody: String = response.body!!.string()
                            functionDTO = Json.decodeFromString(jsonBody)
                        }
                    }
                }
            }.join()
        }
        return functionDTO
    }

    fun triggerFunction(deviceId: String, functionId: Long, functionValue: Float) {
        Log.d("handler", "triggering function: $functionId from device: $deviceId with value: $functionValue")
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val request = Request.Builder()
            .url(ServiceProvider.baseUrl + "function/trigger?deviceId=$deviceId&functionId=$functionId")
            .put(functionValue.toString().toRequestBody(mediaType))
            .build()
        Log.d("handler", "request url: ${request.url}")
        ServiceProvider.connectionSaveCall {
            ServiceProvider.client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("handler", "trigger function on failure")
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (response.code == 500) {
                            Log.d("handler", "trigger function response 500")
                        }
                        if (!response.isSuccessful) {
                            Log.d("handler", "trigger !successful")
                        }
                    }
                }
            })
        }
    }
}