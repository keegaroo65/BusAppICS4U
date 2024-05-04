package com.example.busappics4u

import android.util.Log
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL


class WebReqHandler {
    companion object{
        const val GTFS_STATIC_URL = "https://oct-gtfs-emasagcnfmcgeham.z01.azurefd.net/public-access/GTFSExport.zip"

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
                Log.w("WebReqHandler", "FileNotFoundException: $url")
                return false // swallow a 404
            } catch (e: IOException) {
                Log.w("WebReqHandler", "IOException: $url")
                return false // swallow a 404
            }
        }
    }
}