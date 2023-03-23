package de.rehatech2223.lgg_frontend.util

import android.content.Context
import de.rehatech2223.datamodel.DeviceDTO
import de.rehatech2223.datamodel.RoutineDTO
import de.rehatech2223.lgg_frontend.R
import java.io.BufferedReader

class TileImageUtil {
    companion object {
        fun getDeviceImageId(deviceDTO: DeviceDTO, context: Context): Int {
            val reader: BufferedReader = context.assets.open("device_to_icon_mappings.csv").bufferedReader()
            reader.readLine() //skip header
            val mappings: List<String> = reader.lineSequence()
                .filter {
                    it.isNotBlank() && (it.split(',', limit = 2).first() == deviceDTO.deviceName)
                }.map { it.split(',', limit = 2).last().trim() }
                .toList()

            return if (mappings.isEmpty()) R.drawable.error_100px else {
                when (mappings.first()) {
                    "detector_air" -> R.drawable.detector_co_100px
                    "detector_smoke" -> R.drawable.detector_smoke_100px
                    "motion" -> R.drawable.detection_and_zone_100px
                    "light" -> R.drawable.light_100px
                    "power" -> R.drawable.power_100px
                    "window" -> R.drawable.window_sensor_100px
                    "door" -> R.drawable.door_open_100px
                    "temperature" -> R.drawable.nest_true_radiant_100px
                    "cam" -> R.drawable.nest_cam_wall_mount_100px
                    "error" -> R.drawable.error_100px
                    else -> R.drawable.error_100px
                }
            }
        }

        fun getRoutineImageId(routineDTO: RoutineDTO, context: Context): Int{
            val reader: BufferedReader = context.assets.open("routine_to_icon_mappings.csv").bufferedReader()
            reader.readLine() //skip header
            val mappings: List<String> = reader.lineSequence()
                .filter {
                    it.isNotBlank() && (it.split(',', limit = 2).first() == routineDTO.routineName)
                }.map { it.split(',', limit = 2).last().trim() }
                .toList()

            return if (mappings.isEmpty()) R.drawable.calendar_month_100px else {
                when (mappings.first()) {
                    "default" -> R.drawable.calendar_month_100px
                    "error" -> R.drawable.error_100px
                    else -> R.drawable.calendar_month_100px
                }
            }
        }
    }
}