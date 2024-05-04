package com.example.busappics4u

import android.content.Context
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.zip.ZipFile

class FileHandler {
    companion object {
        private const val GTFS_STATIC_PATH = "GTFSExport"
        private const val GTFS_STATIC_FILE_ZIP = "GTFSExport.zip"

        private var path: File? = null

        fun load(
            context: Context
        ) {
            // Initialize the path variable for future use
            path = context.filesDir

            // Initialize the file if it doesn't exist
            val gtfsFolder = File(path, GTFS_STATIC_PATH)
            if (!gtfsFolder.exists()) {
                attemptDownloadZip(gtfsFolder)
            }
            else {
                Log.i("FileHandler","Folder already present.")
                // TODO: Extract zip file then use it, hopefully in an efficient way lol. kgtfs? :pray:
            }
        }

        private fun attemptDownloadZip(gtfsFolder: File) {
            GlobalScope.launch {
                async{
                    val gtfsZip = File(path, GTFS_STATIC_FILE_ZIP)

                    if (!gtfsZip.exists()) {
                        Log.i("FileHandler","Attempting to install zip")
                        val result = WebReqHandler.downloadFile(
                            WebReqHandler.GTFS_STATIC_URL,
                            gtfsZip
                        )
                        Log.i("FileHandler", "Zip file finished: $result")
                    }

                    Log.i("FileHandler","attempting UNzip")
                    unzip(gtfsZip, gtfsFolder.path)
                    Log.i("FileHandler","UNzip winning")
                }
            }
        }

        /**
         * @param zipFilePath
         * @param destDirectory
         * @throws IOException
         */
        @Throws(IOException::class)
        fun unzip(zipFilePath: File, destDirectory: String) {
            File(destDirectory).run {
                if (!exists()) {
                    mkdirs()
                }
            }

            ZipFile(zipFilePath).use { zip ->
                zip.entries().asSequence().forEach { entry ->
                    zip.getInputStream(entry).use { input ->
                        val filePath = destDirectory + File.separator + entry.name

                        if (!entry.isDirectory) {
                            // if the entry is a file, extracts it
                            extractFile(input, filePath)
                        } else {
                            // if the entry is a directory, make the directory
                            val dir = File(filePath)
                            dir.mkdir()
                        }
                    }
                }
            }
        }

        /**
         * Extracts a zip entry (file entry)
         * @param inputStream
         * @param destFilePath
         * @throws IOException
         */
        @Throws(IOException::class)
        private fun extractFile(inputStream: InputStream, destFilePath: String) {
            val bos = BufferedOutputStream(FileOutputStream(destFilePath))
            val bytesIn = ByteArray(BUFFER_SIZE)
            var read: Int
            while (inputStream.read(bytesIn).also { read = it } != -1) {
                bos.write(bytesIn, 0, read)
            }
            bos.close()
        }

        /**
         * Size of the buffer to read/write data
         */
        private const val BUFFER_SIZE = 4096
    }
}