package com.ametr1ne.overdiff.utils

import com.ametr1ne.overdiff.models.Article
import org.json.JSONObject

class ArticleActionTask(private val articleHash: String) {

    suspend fun getArticle(): Article? {

        return runCatching {
            val requestUrl = "http://${GlobalProperties.KSITE_ADDRESS}/api/article?id=${articleHash}"
            val charset = "UTF-8"
            val multipart = MultipartUtility(requestUrl, charset)
            val response = multipart.finish()
            val stringJson = StringBuilder()
            for (s in response) {
                stringJson.append(s)
            }
            val jsonObject = JSONObject(stringJson.toString())
            return Article.deserialize(jsonObject)
        }.getOrElse { null }
    }
}