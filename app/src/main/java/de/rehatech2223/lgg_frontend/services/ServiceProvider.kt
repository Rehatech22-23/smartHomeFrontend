package de.rehatech2223.lgg_frontend.services

import android.content.Context
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object ServiceProvider {
    //const val baseUrl: String = "http://192.168.2.44:8080/"
    //const val baseUrl: String = "http://raspberrypi:9000/"
    const val baseUrl: String = "http://192.168.83.122:9000/"
    val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .callTimeout(30, TimeUnit.SECONDS)
        .build()
    val devicesService: DeviceService = DeviceService()
    val functionService: FunctionService = FunctionService()
    val routineService: RoutineService = RoutineService()
}

