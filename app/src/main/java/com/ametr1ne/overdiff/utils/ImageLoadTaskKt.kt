package com.ametr1ne.overdiff.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView
import java.net.HttpURLConnection
import java.net.URL
import java.util.function.Consumer

class ImageLoadTaskKt(private val url: String, private val imageView: ImageView) {

    private var exceptionHandler: Consumer<Exception>? = null
    fun exceptionHandler(exceptionHandler: Consumer<Exception>?): ImageLoadTaskKt {
        this.exceptionHandler = exceptionHandler
        return this
    }

    suspend fun upload() : Bitmap?{
        try {
            val urlConnection = URL(url)
            val connection = urlConnection
                    .openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            if (input != null) return BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            if (exceptionHandler != null) exceptionHandler!!.accept(e)
        }
        return null
    }
}