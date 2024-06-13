package com.example.busappics4u.utility

import androidx.compose.ui.graphics.Color
import com.example.busappics4u.ui.theme.ServiceTypeColors

class Utility {
    companion object {
        fun time(): Long {
            return System.currentTimeMillis()
        }

        val frequents = listOf(6, 7, 10, 11, 12, 14, 15, 25, 40, 44, 51, 53, 80, 85, 87, 88, 90, 111)
        val rapids = listOf(39, 45, 57, 61, 62, 63, 74, 75, 97, 98, 99)
        val limited = listOf(15, 21, 23, 27, 31, 32, 33, 47, 66, 67, 73, 82, 93, 96, 110, 153, 158, 161, 162, 164, 165, 173, 176, 186, 187, 190, 198, 199, 301, 302, 303, 304, 305)

        fun ServiceColor(routeId: Int): Color {
            if (frequents.contains(routeId)) {
                return ServiceTypeColors.Frequent
            }
            else if (rapids.contains(routeId)) {
                return ServiceTypeColors.Rapid
            }
            else if (routeId >= 200 && routeId <= 299) {
                return ServiceTypeColors.Connexion
            }
            return ServiceTypeColors.Local
        }
    }
}