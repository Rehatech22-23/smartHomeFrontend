package de.rehatech2223.lgg_frontend.util

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@kotlinx.serialization.Serializable
data class DeviceNameDTO(
    val name: String,
    val icon: String="error",
    val description: String="",
    val rangeWithButtons: Boolean=false,
    val rangeMaxText: String="An",
    val rangeMinText: String="Aus"
){
    companion object{
        fun deserialize(deviceName: String): DeviceNameDTO{
            return try {
                Json.decodeFromString(deviceName)
            } catch (e: IllegalArgumentException) {
                DeviceNameDTO("Error Name")
            }
        }
    }
}