package com.example.mtcnn_demo

import android.content.Context
import android.util.Log
import java.io.*


object FileUtils {

    private const val TAG = "FileUtils"
    fun copyFileFromAssetsToOthers(context: Context?, assetName: String, targetName: String) {
        val targetFile: File
        var inputStream: InputStream? = null
        var outputStream: FileOutputStream? = null
        try {
            val assets = context?.assets
            targetFile = File(targetName)
            if (!targetFile.parentFile.exists()) {
                targetFile.parentFile.mkdirs()
            }
            if (!targetFile.exists()) {
                targetFile.createNewFile()
            }
            inputStream = assets?.open(assetName)
            outputStream = FileOutputStream(targetFile, false /* append */)
            val buf = ByteArray(2048)
            while (true) {
                val r = inputStream?.read(buf) ?: -1
                if (r == -1) {
                    break
                }
                outputStream.write(buf, 0, r)
            }
        } catch (e: Exception) {
            if (Constants.LOG_DEBUG)
                Log.e(TAG, e.message)
        } finally {
            closeSafely(outputStream)
            closeSafely(inputStream)
        }
    }

    private fun closeSafely(closeable: Closeable?) {
        try {
            closeable?.close()
        } catch (e: IOException) {
            if (Constants.LOG_DEBUG)
                Log.e(TAG, e.message)
        }
    }
}