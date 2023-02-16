package de.rehatech2223.lgg_frontend.services

import de.rehatech2223.datamodel.RoutineDTO
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

    fun getRoutineList(callback: (routineList: ArrayList<Long>) -> Unit) {
        val request = Request.Builder()
            .url(ServiceProvider.baseUrl + "routine/list")
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

    fun getRoutineInfo(routineId: Long, callback: (routine: RoutineDTO) -> Unit) {
        val request = Request.Builder()
            .url(ServiceProvider.baseUrl + "routine?routineId=$routineId")
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

    fun triggerRoutine(routineId: Long) {
        val request = Request.Builder()
            .url(ServiceProvider.baseUrl + "routine/trigger?routineId=$routineId")
            .get()
            .build()

        ServiceProvider.client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (response.code == 404) { /* todo action on 404 Not Found */
                    }
                    if (response.code == 500) { /* todo action on 500 Internal Server Error */
                    }
                    if (!response.isSuccessful) { /* todo issue on github for popup warning */
                    }
                }
            }
        })
    }

    /*
     *  Returns a Routine on 201 Created, when Routine info had to be changed from the predefined routine
     *  can return null!
     */
    fun createRoutine(routine: RoutineDTO, callback: (routine: RoutineDTO?) -> Unit) {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBodyJson: String = Json.encodeToString(routine)
        val request = Request.Builder()
            .url(ServiceProvider.baseUrl + "routine/create")
            .post(requestBodyJson.toRequestBody(mediaType))
            .build()

        ServiceProvider.client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (response.code == 201) {
                        val jsonBody: String = response.body!!.string()
                        callback.invoke(Json.decodeFromString(jsonBody))
                    }
                    if (!response.isSuccessful) { /* todo issue on github for popup warning */
                    }
                    callback.invoke(null)
                }
            }
        })
    }

    fun deleteRoutine(routineId: Long, callback: (deleted: Boolean) -> Unit) {
        val request = Request.Builder()
            .url(ServiceProvider.baseUrl + "routine/delete?routineId=$routineId")
            .delete()
            .build()

        ServiceProvider.client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (response.code == 404) {
                        callback.invoke(false) /* todo action on 404 Not Found */
                    }
                    if (!response.isSuccessful) {
                        callback.invoke(false) /* todo issue on github for popup warning */
                    }
                    callback.invoke(true)
                }
            }
        })
    }
}