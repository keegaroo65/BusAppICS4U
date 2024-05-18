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
        const val SERVER_URL = "https://busappserver.azurewebsites.net"
        const val GTFS_REALTIME_URL = "https://nextrip-public-api.azure-api.net/octranspo/gtfs-rt-vp/beta/v1/VehiclePositions"

//        const val GTFS_STATIC_URL = "https://oct-gtfs-emasagcnfmcgeham.z01.azurefd.net/public-access/GTFSExport.zip"

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

        fun getTripInfo(
            tripId: String
        ): String {
            val url = URL("$SERVER_URL/trip/$tripId")

//            with(url.openConnection() as HttpURLConnection) {
//                requestMethod = "GET"  // optional default is GET
//
//                println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    inputStream.bufferedReader().use {
//                        it.lines().forEach { line ->
//                            println(line)
//                        }
//                    }
//                } else {
//                    val reader: BufferedReader = inputStream.bufferedReader()
//                    var line: String? = reader.readLine()
//                    while (line != null) {
//                        System.out.println(line)
//                        line = reader.readLine()
//                    }
//                    reader.close()
//                }
//            }

            val tripInfo = url.readText()

            Log.d(TAG, "Feed: $tripInfo")

            return tripInfo
        }

        fun getTripDetail(
            tripId: String,
            detail: String
        ) {

        }

        fun getTripHeadsign(
            tripId: String
        ) {

        }

        @Throws(Exception::class)
        fun test(
            id: String,
            callback: (String) -> Unit
        ) {
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

                    var found = false
                    var routeId: String = "0"
                    var tripId: String = "0"

                    for (entity in feed.entityList) {
                        if (entity.hasVehicle()) {
                            if (entity.vehicle.vehicle.id == id) {
                                found = true
                                routeId = entity.vehicle.trip.routeId
                                tripId = entity.vehicle.trip.tripId//"${entity.vehicle.trip.routeId} (${entity.vehicle.trip.tripId})"
                                //Log.d(TAG,"$id is currently running route $result")
                            }
                        }
                    }

                    if (!found) {
                        Log.d(TAG, "Bus $id was not found :(")
                    }

//                    val tripInfo = getTripInfo(tripId)
//
//                    callback(tripInfo)
                    callback(tripId)
                }
            }
        }
    }
}