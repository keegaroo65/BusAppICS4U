package com.example.busappics4u.data

import android.util.Log
import com.example.busappics4u.BuildConfig
import com.example.busappics4u.utility.Utility
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

        var lastRTPing = 0L
        var lastRTFeed: GtfsRealtime.FeedMessage? = null

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
        fun UpdateRTFeed(callback: () -> Unit) {
            val now = Utility.time();
            if (now < lastRTPing + 30L) {
                return // Don't ping more often than every 30 seconds
            }

            GlobalScope.launch {
                async {
                    lastRTPing = Utility.time()

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

                    lastRTFeed = GtfsRealtime.FeedMessage.parseFrom(conn.inputStream)

                    callback() // Tells the callback this has finished loading the lastRTFeed variable
                }
            }
        }

        @Throws(Exception::class)
        fun SearchSingleBus(
            id: String,
            callback: (String, Int) -> Unit
        ) {
            UpdateRTFeed({ // Ping the realtime feed if it's been 30 or more seconds since the last ping
                if (lastRTFeed == null) return@UpdateRTFeed; // If there is no feed data present, return empty

                var routeId = ""
                var tripId = ""
                var entity: GtfsRealtime.FeedEntity = GtfsRealtime.FeedEntity.getDefaultInstance()

                for (_entity in lastRTFeed!!.entityList) {
                    if (_entity.hasVehicle()) {
                        if (_entity.vehicle.vehicle.id == id) {
                            routeId = _entity.vehicle.trip.routeId
                            tripId = _entity.vehicle.trip.tripId
                            entity = _entity
                        }
                    }
                }

                if (tripId == "") {
                    Log.d(TAG, "Bus $id was not found :(")
                    callback("Bus was not found", 0)

                    if (entity != GtfsRealtime.FeedEntity.getDefaultInstance()) {
                        Log.d(TAG, entity.toString())
                    }

                    return@UpdateRTFeed
                }

                callback("This bus is running route", routeId.toInt())
            })
        }

        @Throws(Exception::class)
        fun SearchBusRange(
            minText: String,
            maxText: String,
            callback: (String, List<GtfsRealtime.FeedEntity>) -> Unit
        ) {
            val min = minText.toIntOrNull() ?: -1
            val max = maxText.toIntOrNull() ?: -1

            if (min == -1) {
                callback("Invalid minimum id", listOf())
                return
            }
            if (max == -1) {
                callback("Invalid maximum id", listOf())
                return
            }

            UpdateRTFeed({ // Ping the realtime feed if it's been 30 or more seconds since the last ping
                if (lastRTFeed == null) return@UpdateRTFeed // If there is no feed data present, return empty

                var busList: List<GtfsRealtime.FeedEntity> = mutableListOf()

                for (entity in lastRTFeed!!.entityList) {
                    if (entity.hasVehicle()) {
                        val id = entity.vehicle.vehicle.id.toIntOrNull() ?: -1

                        if (id in min..max) {
                            busList = busList.plus(entity)
                        }
                    }
                }

                callback("", busList)
            })
        }

        @Throws(Exception::class)
        fun SearchZEBs(
            callback: (List<GtfsRealtime.FeedEntity>) -> Unit
        ) {
            UpdateRTFeed({ // Ping the realtime feed if it's been 30 or more seconds since the last ping
                if (lastRTFeed == null) return@UpdateRTFeed // If there is no feed data present, return empty

                var zebList: List<GtfsRealtime.FeedEntity> = mutableListOf()

                for (entity in lastRTFeed!!.entityList) {
                    if (entity.hasVehicle()) {
                        if (isZebId(entity.vehicle.vehicle.id)) {
                            zebList = zebList.plus(entity)
                        }
                    }
                }

                callback(zebList)
            })
        }

        fun isZebId(idText: String): Boolean {
            val id = idText.toIntOrNull() ?: 0

            Log.d(TAG, "bus $id is ${id >= 8110}")

//            return id >= 8110
            return id in 2100..2599
        }
    }
}