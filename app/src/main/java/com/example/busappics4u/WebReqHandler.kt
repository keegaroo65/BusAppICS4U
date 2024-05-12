package com.example.busappics4u

import android.util.Log
import com.google.transit.realtime.GtfsRealtime.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


private const val TAG = "WebReqHandler"

class WebReqHandler {
    companion object{
        const val GTFS_STATIC_URL = "https://oct-gtfs-emasagcnfmcgeham.z01.azurefd.net/public-access/GTFSExport.zip"
        const val GTFS_REALTIME_URL = "https://nextrip-public-api.azure-api.net/octranspo/gtfs-rt-vp/beta/v1/VehiclePositions"

        suspend fun downloadFile(url: String, outputFile: File): Boolean {
            try {
                val u = URL(url)
                val conn = u.openConnection()
                val contentLength = conn.contentLength
                val stream = DataInputStream(u.openStream())
                val buffer = ByteArray(contentLength)
                stream.readFully(buffer)
                stream.close()

                val fos = DataOutputStream(FileOutputStream(outputFile))
                fos.write(buffer)
                fos.flush()
                fos.close()

                return true
            } catch (e: FileNotFoundException) {
                Log.w(TAG, "FileNotFoundException: $url")
                return false // swallow a 404
            } catch (e: IOException) {
                Log.w(TAG, "IOException: $url")
                return false // swallow a 404
            }
        }

        @Throws(Exception::class)
        fun test() {
            GlobalScope.launch {
                async {
                    val url = URL(GTFS_REALTIME_URL)

                    val conn = url.openConnection() as HttpURLConnection
                    conn.setRequestMethod("GET")
                    conn.setRequestProperty(
                        "Cache-Control",
                        "no-cache"
                    )
                    conn.setRequestProperty(
                        "Ocp-Apim-Subscription-Key",
                        BuildConfig.ocKey
                    )

                    val feed = FeedMessage.parseFrom(conn.inputStream)
                    //Log.d(TAG, feed.toString())

                    for (entity in feed.entityList) {
                        if (entity.hasVehicle()) {
                            if (entity.vehicle.vehicle.id == "6508") {
                                Log.d(TAG,"6508 is currently running route ${entity.vehicle.trip.routeId} ${entity.vehicle.trip.tripId}")
                            }
                        }
                    }
                }
            }
        }
    }
}