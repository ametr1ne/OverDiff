package com.ametr1ne.overdiff.utils

import com.ametr1ne.overdiff.models.User
import org.json.JSONObject

class RefreshTokenTaskKt(private val refreshToken: String, private val userId: Long) {

    suspend fun refreshToken() : User{
        try {
            val requestUrl = "http://" + GlobalProperties.KSITE_ADDRESS + "/api/refresh?id=" + userId +
                    "&device_id=" + DeviceId.getDeviceId() +
                    "&refresh_token=" + refreshToken +
                    "&service_id=" + GlobalProperties.SERVICE_NAME
            val charset = "UTF-8"
            val multipart = MultipartUtility(requestUrl, charset)
            val response = multipart.finish()
            val stringJson = StringBuilder()
            for (s in response) {
                stringJson.append(s)
            }
            val jsonObject = JSONObject(stringJson.toString())
            return User.deserialize(jsonObject)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return User.getInstance()
    }

}