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

/**
 * Implements api requests for "function" cf. API-doc.
 *
 * @author Fynn Debus
 */
class FunctionService {

    /**
     * Implementation of "/function".
     *
     * @param functionId functionId of the requested function
     * @return FunctionDTO? returns requested function or null
     */
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

    /**
     * Implementation of "/function/trigger".
     *
     * @param deviceId id of the device that possesses the function
     * @param functionId functionId of the function
     * @param functionValue value that should be used for triggering cf. API-doc
     */
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

    /**
     * Implementation of "/function/list".
     *
     * @return ArrayList<FunctionDTO> returns list of all functions
     */
    fun getFunctionList(): ArrayList<FunctionDTO> {
        Log.d("handler", "requested function list")
        var functionDTOList: ArrayList<FunctionDTO> = ArrayList()
        runBlocking {
            val request = Request.Builder()
                .url(ServiceProvider.baseUrl + "function/list")
                .get()
                .build()
            launch(Dispatchers.IO) {
                ServiceProvider.connectionSaveCall {
                    ServiceProvider.client.newCall(request).execute().use { response ->
                        if (!response.isSuccessful || response.code != 200) {
                            Log.d("handler", "canceled")
                            cancel()
                        } else {
                            val jsonBody: String = response.body!!.string()
                            functionDTOList = Json.decodeFromString(jsonBody)
                        }
                    }
                }
            }.join()
        }
        return functionDTOList
    }
}