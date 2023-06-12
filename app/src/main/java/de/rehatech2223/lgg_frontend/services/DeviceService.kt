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

/**
 * Implements api requests for "device" cf. API-doc.
 * All requests are made asynchronous but blocking, because android does not support
 * synchronous http requests.
 * Calls are also made error-save and start the error activity on failure.
 *
 * @author Fynn Debus
 */
class DeviceService {

    /**
     * Implementation of "/device/list".
     *
     * @return ArrayList<DeviceDTO> returns list of devices
     */
    fun getDeviceList(): ArrayList<DeviceDTO> {
        Log.d("handler", "requested device list")
        var deviceList: ArrayList<DeviceDTO> = ArrayList()
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

    /**
     * Implementation of "/device".
     *
     * @param deviceId deviceId of the requested device
     * @return DeviceDTO? returns requested device or null
     */
    fun getDeviceInfo(deviceId: String): DeviceDTO? {
        Log.d("handler", "requested device")
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
                            Log.d("handler", "device with: $jsonBody")
                            deviceDTO = Json.decodeFromString(jsonBody)
                        }
                    }
                }
            }.join()
        }
        return deviceDTO
    }

    /**
     * Updates backend-database for debug or testing purposes ("/device/updated").
     *
     * @return Boolean returns success or not
     */
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