package de.rehatech2223.lgg_frontend.services

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
        var deviceList: ArrayList<String> = ArrayList()
        runBlocking {
            val request = Request.Builder()
                .url(ServiceProvider.baseUrl + "device/list")
                .get()
                .build()
            launch(Dispatchers.IO) {
                ServiceProvider.client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        cancel()/* todo issue on github for popup warning */
                    }
                    val jsonBody: String = response.body!!.string()
                    deviceList = Json.decodeFromString(jsonBody)
                }
            }.join()
        }
        return deviceList
    }

    fun getDeviceInfo(deviceId: String): DeviceDTO? {
        var deviceDTO: DeviceDTO? = null
        runBlocking {
            val request = Request.Builder()
                .url(ServiceProvider.baseUrl + "device?deviceId=$deviceId")
                .get()
                .build()
            launch(Dispatchers.IO) {
                ServiceProvider.client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        cancel() /* todo issue on github for popup warning */
                    }
                    val jsonBody: String = response.body!!.string()
                    deviceDTO = Json.decodeFromString(jsonBody)
                }
            }.join()
        }
        return deviceDTO
    }

    fun getUpdatedDevices(): ArrayList<String> {
        var updatedDevices: ArrayList<String> = ArrayList()
        val request = Request.Builder()
            .url(ServiceProvider.baseUrl + "device/updatedDevices")
            .get()
            .build()
        runBlocking {
            launch(Dispatchers.IO) {
                ServiceProvider.client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        cancel() /* todo issue on github for popup warning */
                    }
                    val jsonBody: String = response.body!!.string()
                    updatedDevices = Json.decodeFromString(jsonBody)
                }
            }.join()
        }
        return updatedDevices
    }
}