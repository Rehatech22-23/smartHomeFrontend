package de.rehatech2223.lgg_frontend.services

import de.rehatech2223.datamodel.Function
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class FunctionService {

    fun getFunctionInfo(functionId: Long): Function {
        val request = Request.Builder()
            .url(ServiceProvider.baseUrl + "function?functionId=$functionId")
            .get()
            .build()

        ServiceProvider.client.newCall(request).execute().use { response ->
            if(!response.isSuccessful) { /* todo issue on github for popup warning */ }
            val jsonBody: String = response.body!!.string()
            return Json.decodeFromString(jsonBody)
        }
    }

    fun triggerFunction(deviceId: String, functionId: Long, functionValue: Float){
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val request = Request.Builder()
            .url(ServiceProvider.baseUrl + "function/trigger?deviceId=$deviceId&functionId=$functionId")
            .put(functionValue.toString().toRequestBody(mediaType))
            .build()

        ServiceProvider.client.newCall(request).execute().use { response ->
            if(response.code == 500){ /* todo action on 500 Internal Server Error */ }
            if(!response.isSuccessful) { /* todo issue on github for popup warning */ }
        }
    }
}