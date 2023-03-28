package de.rehatech2223.lgg_frontend.services

import android.content.Context
import android.content.Intent
import android.util.Log
import de.rehatech2223.lgg_frontend.ConnectionErrorActivity
import de.rehatech2223.lgg_frontend.SmarthomeApplication
import okhttp3.OkHttpClient
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import java.util.concurrent.TimeUnit

object ServiceProvider {
    var baseUrl: String = "http://192.168.2.44:8080/"

    //const val baseUrl: String = "http://raspberrypi:9000/"
    //const val baseUrl: String = "http://192.168.83.122:9000/"
    val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .callTimeout(10, TimeUnit.SECONDS)
        .build()
    val devicesService: DeviceService = DeviceService()
    val functionService: FunctionService = FunctionService()
    val routineService: RoutineService = RoutineService()

    fun connectionSaveCall(call: () -> Unit) {
        try {
            call.invoke()
        } catch (_: IOException) {
            Log.d("handler", "IOException")
            startConnectionErrorActivity()
        } catch (_: InvocationTargetException) {
            Log.d("handler", "InvocationTargetException")
            startConnectionErrorActivity()
        }
    }

    private fun startConnectionErrorActivity() {
        val context: Context = SmarthomeApplication.getContext()
        val connectionErrorActivity = Intent(context, ConnectionErrorActivity::class.java)
        connectionErrorActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        connectionErrorActivity.putExtra("url", baseUrl)
        context.startActivity(connectionErrorActivity)
    }
}

