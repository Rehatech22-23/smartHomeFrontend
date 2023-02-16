package de.rehatech2223.lgg_frontend.services

import okhttp3.OkHttpClient

object ServiceProvider {
    //const val baseUrl: String = "http://192.168.83.174:8080/"
    const val baseUrl: String = "http://raspberrypi:9000/"
    val client: OkHttpClient = OkHttpClient()
    val devicesService: DeviceService = DeviceService()
    val functionService: FunctionService = FunctionService()
    val routineService: RoutineService = RoutineService()
}

