package de.rehatech2223.lgg_frontend.services

import okhttp3.OkHttpClient

object ServiceProvider {
    const val baseUrl: String = "http://localhost:8080/"
    val client: OkHttpClient = OkHttpClient()
    val devicesService: DeviceService = DeviceService()
    val functionService: FunctionService = FunctionService()
    val routineService: RoutineService = RoutineService()
}

