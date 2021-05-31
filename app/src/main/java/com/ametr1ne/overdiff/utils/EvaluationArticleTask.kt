package com.ametr1ne.overdiff.utils

import com.ametr1ne.overdiff.models.Article
import org.json.JSONObject

class EvaluationArticleTask(
    private val accessToken: String,
    private val article: Article,
    private val like: Boolean
) {

    suspend fun evaluation(): JSONObject? {
        runCatching {
            val requestUrl =
                "http://" + GlobalProperties.KSITE_ADDRESS + "/api/evaluation?access_token=" + accessToken +
                        "&article_id=" + article.id +
                        "&is_like=" + like
            val charset = "UTF-8"
            val multipart = MultipartUtility(requestUrl, charset)
            val response = multipart.finish()
            val stringJson = StringBuilder()
            for (s in response) {
                stringJson.append(s)
            }
            val result = JSONObject(stringJson.toString())
            val status = result.getInt("status")
            if (status == ArticleStatus.SUCCESSFULLY) {
                val likes = result.getInt("likes")
                val dislikes = result.getInt("dislikes")
                article.likes = likes
                article.dislikes = dislikes
            }
            return result
        }.getOrElse { it.printStackTrace() }
        return null
    }
}