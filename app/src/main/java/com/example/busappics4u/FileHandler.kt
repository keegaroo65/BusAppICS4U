package com.example.busappics4u

import android.content.Context
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.zip.ZipFile

private const val TAG = "FileHandler"

class FileHandler {
    companion object {
        private const val GTFS_STATIC_PATH = "GTFSExport"
        private const val GTFS_STATIC_FILE_ZIP = "GTFSExport.zip"
        private const val GTFS_DB_PATH = "gtfs.db"

        private var path: File? = null

        fun load(
            context: Context
        ) {
//            // Initialize the path variable for future use
//            path = context.filesDir
//
//            // Initialize the file if it doesn't exist
//            val gtfsFolder = File(path, GTFS_STATIC_PATH)
//            if (!gtfsFolder.exists()) {
//                attemptDownloadZip(gtfsFolder)
//            }
        }

//        private fun attemptDownloadZip(gtfsFolder: File) {
//            GlobalScope.launch {
//                async{
//                    // Download zip file from OC Transpo
//                    val gtfsZip = File(path, GTFS_STATIC_FILE_ZIP)
//
//                    if (!gtfsZip.exists()) {
//                        Log.i("FileHandler","Attempting to install zip")
//                        val result = WebReqHandler.downloadFile(
//                            WebReqHandler.GTFS_STATIC_URL,
//                            gtfsZip
//                        )
//                        Log.i("FileHandler", "Zip file finished: $result")
//                    }
//
//                    // Unzip the zip file into a normal usable directory
////                    Log.i("FileHandler","attempting UNzip")
////                    unzip(gtfsZip, gtfsFolder.path)
////                    Log.i("FileHandler","UNzip winning")
//
//                    // Use kgtfs to process the zip file into a GtfsDb
//                    val gtfsDbFile = File(path, GTFS_DB_PATH)
//
//                    val pathString = (path?.absolutePath ?: "")
//
////                    database =
////                        if (gtfsDbFile.exists())
////                            GtfsDb.open("$pathString/$GTFS_DB_PATH")
////                        else GtfsDb.fromReader(
////                            GtfsReader.newZipReader(
////                                File("$pathString/$GTFS_STATIC_FILE_ZIP").toPath()
////                            ),
////                            "$pathString/$GTFS_DB_PATH"
////                        )
////
////                    Log.d(TAG, database.toString())
//                }
//            }
//        }

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

