package com.ametr1ne.overdiff.utils

import android.os.AsyncTask
import org.json.JSONObject
import java.net.HttpURLConnection
import java.util.function.Consumer

class AddCommentTask(
    private val accessToken: String,
    private val articleId: Long,
    private val comment: String
)  {

    suspend fun addComment() : Int?{
        return runCatching {
            val httpUrlConnection: HttpURLConnection? = null
            val requestUrl =
                "http://" + GlobalProperties.KSITE_ADDRESS + "/api/commentarticle?access_token=" + accessToken +
                        "&article_id=" + articleId +
                        "&comment=" + comment
            val charset = "UTF-8"
            val multipart = MultipartUtility(requestUrl, charset)
            val response = multipart.finish()
            val stringJson = StringBuilder()
            for (s in response) {
                stringJson.append(s)
            }
            val jsonObject = JSONObject(stringJson.toString())
            return jsonObject.getString("status").toInt()
        }.getOrElse {
           null
        }
    }


}