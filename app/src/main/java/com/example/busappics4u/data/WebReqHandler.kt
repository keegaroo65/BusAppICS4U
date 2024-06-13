package com.example.busappics4u.data

import android.util.Log
import com.example.busappics4u.BuildConfig
import com.google.transit.realtime.GtfsRealtime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
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
        const val SERVER_URL = "https://busappserver-u74vjrgwzq-nn.a.run.app"
        const val GTFS_REALTIME_URL = "https://nextrip-public-api.azure-api.net/octranspo/gtfs-rt-vp/beta/v1/VehiclePositions"

//        const val GTFS_STATIC_URL = "https://oct-gtfs-emasagcnfmcgeham.z01.azurefd.net/public-access/GTFSExport.zip"

        suspend fun downloadFile(url: String, outputFile: File): Boolean {
            try {
                val u = URL(url)
                val conn = withContext(Dispatchers.IO) {
                    u.openConnection()
                }
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
        ): JSONObject {
            val url = URL("$SERVER_URL/trip/$tripId")

            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"  // optional default is GET

                println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")

                inputStream.bufferedReader().use {
                    it.lines().forEach { line ->
                        println(line)
                    }
                }
            }

            val tripInfo = url.readText()

            Log.d(TAG, "Feed: $tripInfo")

            if (!tripInfo.matches(Regex("""\{.+\}"""))) {
                Log.d(TAG, "getTripInfo server error:")
                Log.d(TAG, tripInfo)

                return JSONObject("{}")
            }

            return JSONObject(tripInfo)
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

                    val feed = GtfsRealtime.FeedMessage.parseFrom(conn.inputStream)
                    //Log.d(TAG, feed.toString())

                    var routeId: String = ""
                    var tripId: String = ""
                    var entity: GtfsRealtime.FeedEntity =
                        GtfsRealtime.FeedEntity.getDefaultInstance()

                    for (_entity in feed.entityList) {
                        if (_entity.hasVehicle()) {
                            if (_entity.vehicle.vehicle.id == id) {
                                routeId = _entity.vehicle.trip.routeId
                                tripId = _entity.vehicle.trip.tripId
                                entity = _entity
                            //"${entity.vehicle.trip.routeId} (${entity.vehicle.trip.tripId})"
                                //Log.d(TAG,"$id is currently running route $result")
                            }
                        }
                    }

                    if (tripId == "") {
                        Log.d(TAG, "Bus $id was not found :(")
                        callback("Bus was not found")

                        if (entity != GtfsRealtime.FeedEntity.getDefaultInstance()) {
                            Log.d(TAG, entity.toString())
                        }

                        return@async
                    }

                    Log.d(TAG, "routeId $routeId")
                    Log.d(TAG, "tripId $tripId")
                    //val tripInfo = getTripInfo(tripId)

                    callback("This bus is running route $routeId")

                    /*if (!tripInfo.has("route_id")) {
                        callback("Server error $tripInfo")
                        return@async
                    }

                    callback("${tripInfo.get("route_id")} ${tripInfo.get("trip_headsign")}")*/
//                    callback(tripId)
                }
            }
        }
    }
}