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

/**
 * The ServiceProvider class acts as an Provider for singleton instances of
 * DeviceService, FunctionService, RoutineService.
 * It also can be used to handle error-save requests from the service layer.
 *
 * @author Fynn Debus
 */
object ServiceProvider {
    //var baseUrl: String = "http://192.168.2.44:8080/"

    var baseUrl: String = "http://raspberrypi:9000/"
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

    /**
     * Invokes the method given as a parameter while handling Exceptions.
     *
     * @param call Method implementation to invoke
     */
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

    /**
     * Starts the error Activity when connection loss or request errors are caught.
     */
    private fun startConnectionErrorActivity() {
        val context: Context = SmarthomeApplication.getContext()
        val connectionErrorActivity = Intent(context, ConnectionErrorActivity::class.java)
        connectionErrorActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        connectionErrorActivity.putExtra("url", baseUrl)
        context.startActivity(connectionErrorActivity)
    }
}

