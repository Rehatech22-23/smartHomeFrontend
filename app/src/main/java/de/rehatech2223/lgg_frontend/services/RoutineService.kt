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

/**
 * Implements api requests for "routine" cf. API-doc.
 *
 * @author Fynn Debus
 */
class RoutineService {

    /**
     * Implementation of "/routine/list".
     *
     * @return ArrayList<RoutineDTO> returns routine list
     */
    fun getRoutineList(): ArrayList<RoutineDTO> {
        Log.d("handler", "requesting routine List")
        var routineList: ArrayList<RoutineDTO> = ArrayList()
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

    /**
     * Implementation of "/routine".
     *
     * @param routineId routineId of the requested routine
     * @return RoutineDTO? returns requested routine or null
     */
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
                            Log.d("handler", "routine request of routine $routineId failed with body: ${response.body!!.string()}")
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

    /**
     * Implementation of "/function/trigger".
     *
     * @param routineId routineId of routine that should be triggered
     */
    fun triggerRoutine(routineId: Long) {
        Log.d("handler", "triggering routine: $routineId")
        val request = Request.Builder()
            .url(ServiceProvider.baseUrl + "routine/trigger?routineId=$routineId")
            .get()
            .build()
        ServiceProvider.connectionSaveCall {
            ServiceProvider.client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("handler", "request failed")
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

    /**
     * Implementation of "/routine/create"
     *
     *  @param routine routineDTO that should be created
     *  @return RoutineDTO? returns a RoutineDTO if the routine has been changed by the backend
     */
    fun createRoutine(routine: RoutineDTO): RoutineDTO? {
        Log.d("handler", "created routine")
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
                        if ((response.isSuccessful || response.code == 200) || (response.code == 201 && response.body != null)) {
                            Log.d("handler", "create routine successful")
                            val jsonBody: String = response.body!!.string()
                            routineDTO = Json.decodeFromString(jsonBody)
                        } else {
                            Log.d("handler", "create routine failed")
                            cancel()
                        }
                    }
                }
            }.join()
        }
        return routineDTO
    }

    /**
     * Implementation of "/routine/delete".
     *
     * @param routineId routineId of the routine to delete
     * @return Boolean returns success or not
     */
    fun deleteRoutine(routineId: Long): Boolean {
        Log.d("handler", "deleting routine with id: $routineId")
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
                            Log.d("handler", "deleting failed 404")
                            deleted = false
                            cancel()
                        }
                        if (!response.isSuccessful || response.code != 200) {
                            Log.d("handler", "deleting failed != 200")
                            deleted = false
                            cancel()
                        }
                    }
                }
            }.join()
        }
        Log.d("handler", "delete successful")
        return deleted
    }
}