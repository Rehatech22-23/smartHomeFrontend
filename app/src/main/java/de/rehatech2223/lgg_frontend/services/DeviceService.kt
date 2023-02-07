package de.rehatech2223.lgg_frontend.services

import android.util.Log
import de.rehatech2223.datamodel.Device
import okhttp3.Request
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement


class DeviceService {

    fun getDeviceList(): ArrayList<String>{
        return ArrayList()
    }

    fun getDeviceInfo(deviceId: String): Device {
        val request = Request.Builder()
            .url(ServiceProvider.baseUrl + "device?deviceId=" + deviceId)
            .get()
            .build()

        ServiceProvider.client.newCall(request).execute().use { response ->
            if(!response.isSuccessful) { /* todo issue on github for popup warning */ }
            val jsonBody: String = response.body!!.string()
            return Json.decodeFromString(jsonBody)
        }
    }

    fun getUpdatedDevices(): ArrayList<String>{
        return ArrayList()
    }

}