package de.rehatech2223.lgg_frontend.services

import android.util.Log
import de.rehatech2223.datamodel.DeviceDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.Request
import okio.use

class DeviceService {
    
    fun getDeviceList(): ArrayList<String> {
        Log.d("handler", "requested device list")
        var deviceList: ArrayList<String> = ArrayList()
        runBlocking {
            val request = Request.Builder()
                .url(ServiceProvider.baseUrl + "device/list")
                .get()
                .build()
            launch(Dispatchers.IO) {
                Log.d("handler", "launchen at: ${request.url}")
                ServiceProvider.connectionSaveCall {
                    ServiceProvider.client.newCall(request).execute().use { response ->
                        Log.d("handler", "request angekommen")
                        if (!response.isSuccessful || response.code != 200) {
                            Log.d("handler", "request rip")
                            cancel()
                        } else {
                            val jsonBody: String = response.body!!.string()
                            Log.d("handler", "devicelist: $jsonBody")
                            deviceList = Json.decodeFromString(jsonBody)
                        }
                    }
                }
                Log.d("handler", "finished")
            }.join()
        }
        Log.d("handler", "returning")
        return deviceList
    }

    fun getDeviceInfo(deviceId: String): DeviceDTO? {
        Log.d("handkler", "requested device")
        var deviceDTO: DeviceDTO? = null
        runBlocking {
            val request = Request.Builder()
                .url(ServiceProvider.baseUrl + "device?deviceId=$deviceId")
                .get()
                .build()
            launch(Dispatchers.IO) {
                ServiceProvider.connectionSaveCall {
                    ServiceProvider.client.newCall(request).execute().use { response ->
                        if (!response.isSuccessful || response.code != 200) {
                            cancel()
                        } else {
                            val jsonBody: String = response.body!!.string()
                            Log.d("handkler", "device with: $jsonBody")
                            deviceDTO = Json.decodeFromString(jsonBody)
                        }
                    }
                }
            }.join()
        }
        return deviceDTO
    }

    fun updateDeviceDatabase() : Boolean {
        var success = true
        val request = Request.Builder()
            .url(ServiceProvider.baseUrl + "device/updated")
            .get()
            .build()
        runBlocking {
            launch(Dispatchers.IO) {
                ServiceProvider.connectionSaveCall {
                    ServiceProvider.client.newCall(request).execute().use { response ->
                        if (!response.isSuccessful || response.code != 200) {
                            success = false
                            cancel()
                        }
                    }
                }
            }.join()
        }
        return success
    }
}