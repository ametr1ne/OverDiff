package com.ametr1ne.overdiff.utils

import android.os.AsyncTask
import com.ametr1ne.overdiff.models.Article
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.util.function.Consumer

class ArticlesActionTask {

    suspend fun getArticles(): Array<Article> {
        val articleList: MutableList<Article> = ArrayList()

        runCatching {
            val requestUrl = "http://" + GlobalProperties.KSITE_ADDRESS + "/api/articles"
            val charset = "UTF-8"
            val multipart = MultipartUtility(requestUrl, charset)
            val response = multipart.finish()
            val stringJson = StringBuilder()
            for (s in response) {
                stringJson.append(s)
            }

            val jsonObject = JSONObject(stringJson.toString())
            val jsonArray = jsonObject.getJSONArray("articles")
            var i = 0
            while (!jsonArray.isNull(i)) {
                val obj = jsonArray.getJSONObject(i)
                articleList.add(Article.deserialize(obj))
                i++
            }

        }
        return articleList.toTypedArray()
    }

}