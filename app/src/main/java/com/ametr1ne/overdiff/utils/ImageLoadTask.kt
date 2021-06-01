package com.ametr1ne.overdiff.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.net.HttpURLConnection
import java.net.URL

class ImageLoadTask(private val url: String) {

    private var exceptionHandler: NConsumer<Throwable>? = null
    fun exceptionHandler(exceptionHandler: NConsumer<Throwable>?): ImageLoadTask {
        this.exceptionHandler = exceptionHandler
        return this
    }

    suspend fun upload() : Bitmap?{
        runCatching {
            val urlConnection = URL(url)
            val connection = urlConnection
                    .openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            if (input != null) return BitmapFactory.decodeStream(input)
        }.getOrElse {
            it.printStackTrace()
            if(exceptionHandler!=null)
                exceptionHandler?.accept(it)
        }
        return null
    }
}