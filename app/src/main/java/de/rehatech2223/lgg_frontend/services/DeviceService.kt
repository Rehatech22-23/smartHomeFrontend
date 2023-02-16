package de.rehatech2223.lgg_frontend.services

import android.util.Log
import de.rehatech2223.datamodel.DeviceDTO
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class DeviceService {

    fun getDeviceList(callback: (deviceList: ArrayList<String>) -> Unit) {
        val request = Request.Builder()
            .url(ServiceProvider.baseUrl + "device/list")
            .get()
            .build()

        ServiceProvider.client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) { /* todo issue on github for popup warning */
                    }
                    val jsonBody: String = response.body!!.string()
                    callback.invoke(Json.decodeFromString(jsonBody))
                }
            }
        })
    }

    fun getDeviceInfo(deviceId: String, callback: (device: DeviceDTO) -> Unit) {
        val request = Request.Builder()
            .url(ServiceProvider.baseUrl + "device?deviceId=$deviceId")
            .get()
            .build()

        ServiceProvider.client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) { /* todo issue on github for popup warning */
                        Log.d("asd", "not successfull")
                        return
                    }
                    val jsonBody: String = response.body!!.string()
                    Log.d("handler", "jsonBody: $jsonBody")
                    callback.invoke(Json.decodeFromString(jsonBody))
                }
            }
        })
    }

    fun getUpdatedDevices(callback: (updatedDevicesList: ArrayList<String>) -> Unit) {
        val request = Request.Builder()
            .url(ServiceProvider.baseUrl + "device/updatedDevices")
            .get()
            .build()

        ServiceProvider.client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) { /* todo issue on github for popup warning */
                    }
                    val jsonBody: String = response.body!!.string()
                    callback.invoke(Json.decodeFromString(jsonBody))
                }
            }
        })
    }
}