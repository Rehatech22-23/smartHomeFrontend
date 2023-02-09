package de.rehatech2223.lgg_frontend.services

import de.rehatech2223.datamodel.Routine
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class RoutineService {

    fun getRoutineList(): ArrayList<Long> {
        val request = Request.Builder()
            .url(ServiceProvider.baseUrl + "routine/list")
            .get()
            .build()

        ServiceProvider.client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) { /* todo issue on github for popup warning */ }
            val jsonBody: String = response.body!!.string()
            return Json.decodeFromString(jsonBody)
        }
    }

    fun getRoutineInfo(routineId: Long): Routine {
        val request = Request.Builder()
            .url(ServiceProvider.baseUrl + "routine?routineId=$routineId")
            .get()
            .build()

        ServiceProvider.client.newCall(request).execute().use { response ->
            if(!response.isSuccessful) { /* todo issue on github for popup warning */ }
            val jsonBody: String = response.body!!.string()
            return Json.decodeFromString(jsonBody)
        }
    }

    fun triggerRoutine(routineId: Long) {
        val request = Request.Builder()
            .url(ServiceProvider.baseUrl + "routine/trigger?routineId=$routineId")
            .get()
            .build()

        ServiceProvider.client.newCall(request).execute().use { response ->
            if(response.code == 404) { /* todo action on 404 Not Found */ }
            if(response.code == 500) { /* todo action on 500 Internal Server Error */ }
            if(!response.isSuccessful) { /* todo issue on github for popup warning */ }
        }
    }

    /*
     *  Returns a Routine on 201 Created, when Routine info had to be changed from the predefined routine
     *  can return null!
     */
    fun createRoutine(routine: Routine): Routine? {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBodyJson: String = Json.encodeToString(routine)
        val request = Request.Builder()
            .url(ServiceProvider.baseUrl + "routine/create")
            .post(requestBodyJson.toRequestBody(mediaType))
            .build()

        ServiceProvider.client.newCall(request).execute().use { response ->
            if(response.code == 201){
                val jsonBody: String = response.body!!.string()
                return Json.decodeFromString(jsonBody)
            }
            if(!response.isSuccessful) { /* todo issue on github for popup warning */ }
            return null
        }
    }

    fun deleteRoutine(routineId: Long): Boolean {
        val request = Request.Builder()
            .url(ServiceProvider.baseUrl + "routine/delete?routineId=$routineId")
            .delete()
            .build()

        ServiceProvider.client.newCall(request).execute().use { response ->
            if(response.code == 404) { return false /* todo action on 404 Not Found */ }
            if(!response.isSuccessful) { return false /* todo issue on github for popup warning */ }
            return true
        }
    }
}