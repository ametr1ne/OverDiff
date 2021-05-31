package com.ametr1ne.overdiff.utils

import android.os.AsyncTask
import android.view.View
import android.widget.TextView
import com.ametr1ne.overdiff.MainActivity
import com.ametr1ne.overdiff.R
import com.ametr1ne.overdiff.UserFactory.Companion.getInstance
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.util.function.Consumer

class CreateArticleTask(
    private val file: File?,
    private val description: String,
    private val text: String
)  {
    companion object {
        private var emptyFile: File? = null

        init {
            try {
                emptyFile = File.createTempFile("temptile", ".tmp")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    suspend fun createArticle():Int{
        return runCatching {
            val user = getInstance().getCurrentUser()
            if (user.isAuthorization) {
                val requestUrl =
                    "http://" + GlobalProperties.KSITE_ADDRESS + "/api/createarticle?access_token=" + user.accessToken +
                            "&description=" + description +
                            "&text=" + text
                val multipart = MultipartUtility(requestUrl, Charset.defaultCharset().name())
                if (file != null) multipart.addFilePart("icon", file)
                val response = multipart.finish()
                val stringJson = StringBuilder()
                for (s in response) {
                    stringJson.append(s)
                }
                val jsonObject = JSONObject(stringJson.toString())
                return jsonObject.getInt("status")
            }else{
                return ArticleStatus.TOKEN_DAMAGED
            }
        }.getOrElse { ArticleStatus.ERROR }
    }


}