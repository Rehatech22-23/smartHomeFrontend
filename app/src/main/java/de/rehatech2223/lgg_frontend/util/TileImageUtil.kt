package de.rehatech2223.lgg_frontend.util

import de.rehatech2223.lgg_frontend.R

class TileImageUtil {
    companion object {
        fun getDeviceImageResource(iconName: String): Int {
            return when (iconName) {
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

        fun getRoutineImageResource(iconName: String): Int {
            return when (iconName) {
                "default" -> R.drawable.calendar_month_100px
                "error" -> R.drawable.error_100px
                else -> R.drawable.calendar_month_100px
            }
        }
    }
}