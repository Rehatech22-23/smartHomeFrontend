package de.rehatech2223.lgg_frontend.services

import android.util.Log
import de.rehatech2223.datamodel.RoutineDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

class RoutineService {

    fun getRoutineList(): ArrayList<Long> {
        Log.d("handler", "requesting routine List")
        var routineList: ArrayList<Long> = ArrayList()
        val request = Request.Builder()
            .url(ServiceProvider.baseUrl + "routine/list")
            .get()
            .build()
        runBlocking {
            launch(Dispatchers.IO) {
                ServiceProvider.connectionSaveCall {
                    ServiceProvider.client.newCall(request).execute().use { response ->
                        if (!response.isSuccessful || response.code != 200) {
                            cancel()
                        } else {
                            val jsonBody: String = response.body!!.string()
                            routineList = Json.decodeFromString(jsonBody)
                        }
                    }
                }
            }.join()
        }
        return routineList
    }

    fun getRoutineInfo(routineId: Long): RoutineDTO? {
        Log.d("handler", "requesting routineinfo of: $routineId")
        var routineDTO: RoutineDTO? = null
        val request = Request.Builder()
            .url(ServiceProvider.baseUrl + "routine?routineId=$routineId")
            .get()
            .build()
        runBlocking {
            launch(Dispatchers.IO) {
                ServiceProvider.connectionSaveCall {
                    ServiceProvider.client.newCall(request).execute().use { response ->
                        if (!response.isSuccessful || response.code != 200) {
                            cancel()
                        } else {
                            val jsonBody: String = response.body!!.string()
                            Log.d("handler", "response routine: $jsonBody")
                            routineDTO = Json.decodeFromString(jsonBody)
                        }
                    }
                }
            }.join()
        }
        return routineDTO
    }

    fun triggerRoutine(routineId: Long) {
        Log.d("handler", "triggering routine: $routineId")
        val request = Request.Builder()
            .url(ServiceProvider.baseUrl + "routine/trigger?routineId=$routineId")
            .get()
            .build()
        ServiceProvider.connectionSaveCall {
            ServiceProvider.client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (response.code == 404) {
                            Log.d("handler", "trigger routine failed")
                        }
                        if (response.code == 500) {
                            Log.d("handler", "trigger routine failed")
                        }
                        if (!response.isSuccessful) {
                            Log.d("handler", "trigger routine failed")
                        }
                    }
                }
            })
        }
    }

    /*
     *  Returns a Routine on 201 Created, when Routine info had to be changed from the predefined routine
     *  can return null!
     */
    fun createRoutine(routine: RoutineDTO): RoutineDTO? {
        var routineDTO: RoutineDTO? = null
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBodyJson: String = Json.encodeToString(routine)
        val request = Request.Builder()
            .url(ServiceProvider.baseUrl + "routine/create")
            .post(requestBodyJson.toRequestBody(mediaType))
            .build()
        runBlocking {
            launch(Dispatchers.IO) {
                ServiceProvider.connectionSaveCall {
                    ServiceProvider.client.newCall(request).execute().use { response ->
                        if ((response.code == 201 || response.isSuccessful || response.code == 200) && response.body != null) {
                            val jsonBody: String = response.body!!.string()
                            routineDTO = Json.decodeFromString(jsonBody)
                        } else {
                            cancel()
                        }
                    }
                }
            }.join()
        }
        return routineDTO
    }

    fun deleteRoutine(routineId: Long): Boolean {
        var deleted = true
        val request = Request.Builder()
            .url(ServiceProvider.baseUrl + "routine/delete?routineId=$routineId")
            .delete()
            .build()
        runBlocking {
            launch(Dispatchers.IO) {
                ServiceProvider.connectionSaveCall {
                    ServiceProvider.client.newCall(request).execute().use { response ->
                        if (response.code == 404) {
                            deleted = false
                            cancel()
                        }
                        if (!response.isSuccessful || response.code != 200) {
                            deleted = false
                            cancel()
                        }
                    }
                }
            }.join()
        }
        return deleted
    }
}