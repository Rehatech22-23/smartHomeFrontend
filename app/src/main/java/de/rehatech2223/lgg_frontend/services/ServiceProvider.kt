package de.rehatech2223.lgg_frontend.services

import android.content.Context
import okhttp3.OkHttpClient

object ServiceProvider {
    const val baseUrl: String = "http://192.168.2.44:8080/"
    //const val baseUrl: String = "http://raspberrypi:9000/"
    val client: OkHttpClient = OkHttpClient()
    val devicesService: DeviceService = DeviceService()
    val functionService: FunctionService = FunctionService()
    val routineService: RoutineService = RoutineService()
}

