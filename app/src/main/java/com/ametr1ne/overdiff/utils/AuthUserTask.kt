package com.ametr1ne.overdiff.utils

import com.ametr1ne.overdiff.models.User
import org.json.JSONObject

class AuthUserTask(
    private val username: String,
    private val password: String,
) {

    suspend fun auth(): User {

        return runCatching {
            val requestUrl =
                "http://" + GlobalProperties.KSITE_ADDRESS + "/api/auth?login=" + username +
                        "&password=" + password +
                        "&device_id=" + DeviceId.getDeviceId() +
                        "&service_id=" + GlobalProperties.SERVICE_NAME
            val charset = "UTF-8"
            val multipart = MultipartUtility(requestUrl, charset)
            val response = multipart.finish()
            val stringJson = StringBuilder()
            for (s in response) {
                stringJson.append(s)
            }

            println("SDSD: "+stringJson.toString())

            val jsonObject = JSONObject(stringJson.toString())
            return User.deserialize(jsonObject)
        }.getOrElse {
            it.printStackTrace()
            User.getInstance() }

    }


}