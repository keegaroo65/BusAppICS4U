package com.example.busappics4u.utility

import androidx.compose.ui.geometry.Size
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

        fun ServiceColor(routeId: Int): Color { // TODO: clean up the if statements
            if (frequents.contains(routeId)) {
                return ServiceTypeColors.Frequent
            }
            else if (rapids.contains(routeId)) {
                return ServiceTypeColors.Rapid
            }
            else if (routeId >= 200 && routeId <= 299) {
                return ServiceTypeColors.Connexion
            }
            else if (routeId == 1) {
                return ServiceTypeColors.R1
            }
            else if (routeId == 2) {
                return ServiceTypeColors.R2
            }
            else if (routeId == 3) {
                return ServiceTypeColors.R3
            }
            else if (routeId == 4) {
                return ServiceTypeColors.R4
            }
            return ServiceTypeColors.Local
        }

        // Ratio expressed as a Float value representing X / Y eg. 3:2 (landscape) is 3/2=1.5f
        // Will make it wider to accommodate min aspect ratio
        fun MinAspectRatio(size: Size, ratio: Float): Size {
            return (
                if ((size.width / size.height) < ratio)
                    size.copy(
                        size.height * ratio,
                        size.height
                    )
                else
                    size
            )
        }
    }
}