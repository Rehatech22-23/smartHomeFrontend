package de.rehatech2223.lgg_frontend.services

import de.rehatech2223.datamodel.Device
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.Request

class DeviceService {

    fun getDeviceList(): ArrayList<String>{
        val request = Request.Builder()
            .url(ServiceProvider.baseUrl + "device/list")
            .get()
            .build()

        ServiceProvider.client.newCall(request).execute().use { response ->
            if(!response.isSuccessful) { /* todo issue on github for popup warning */ }
            val jsonBody: String = response.body!!.string()
            return Json.decodeFromString(jsonBody)
        }
    }

    fun getDeviceInfo(deviceId: String): Device {
        val request = Request.Builder()
            .url(ServiceProvider.baseUrl + "device?deviceId=$deviceId")
            .get()
            .build()

        ServiceProvider.client.newCall(request).execute().use { response ->
            if(!response.isSuccessful) { /* todo issue on github for popup warning */ }
            val jsonBody: String = response.body!!.string()
            return Json.decodeFromString(jsonBody)
        }
    }

    fun getUpdatedDevices(): ArrayList<String>{
        val request = Request.Builder()
            .url(ServiceProvider.baseUrl + "device/updatedDevices")
            .get()
            .build()

        ServiceProvider.client.newCall(request).execute().use { response ->
            if(!response.isSuccessful) { /* todo issue on github for popup warning */ }
            val jsonBody: String = response.body!!.string()
            return Json.decodeFromString(jsonBody)
        }
    }
}