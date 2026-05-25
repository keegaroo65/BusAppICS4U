package com.example.busappics4u.utility

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.example.busappics4u.ui.theme.ServiceTypeColors

class Utility {
    companion object {
        fun time(): Long {
            return System.currentTimeMillis()
        }

        val frequents = listOf(6, 7, 10, 11, 12, 14, 25, 39, 40, 41, 44, 45, 57, 61, 62, 63, 68,
            74, 75, 80, 85, 87, 88, 90, 97, 98, 99, 111)
        val limited = listOf(13, 15, 21, 23, 26, 32, 33, 43, 47, 60, 66, 67, 73, 82, 84, 92, 117,
            187, 198)

        fun ServiceColor(routeId: Int): Color { // TODO: clean up the if statements
            if (frequents.contains(routeId)) {
                return ServiceTypeColors.Frequent
            }
            else if (routeId in 200..299) {
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

        fun IsRouteLimited(routeId: Int): Boolean {
            return limited.contains(routeId)        // For local routes that are limited
                    || routeId in 600..699    // For school routes (always limited)
                    || routeId in 300..499    // For shopper & event routes (always limited)
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