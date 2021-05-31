package com.ametr1ne.overdiff

import android.util.Base64
import com.ametr1ne.overdiff.encryption.SimpleCipher
import com.ametr1ne.overdiff.models.User
import com.ametr1ne.overdiff.utils.AuthUserTaskKt
import com.ametr1ne.overdiff.utils.RefreshTokenTaskKt
import kotlinx.coroutines.*
import java.io.IOException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.*
import java.util.function.Consumer
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter

class UserFactoryKt2 {

    private var currentUser: User = User.getInstance()


    fun authCurrentUser(userLogin: String, password: String, savePassword: Boolean, action: Consumer<User?>) {


        try {
            val aesKey = SecretKeySpec(Base64.decode(SimpleCipher.PASSWORD_CIPHER_KEY.toByteArray(), Base64.DEFAULT), "AES")
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.ENCRYPT_MODE, aesKey)
            val encrypted = cipher.doFinal(password.toByteArray())
            val s = DatatypeConverter.printBase64Binary(encrypted)

            CoroutineScope(Dispatchers.IO).launch {
                val authUser = AuthUserTaskKt(userLogin, password).auth()
                setCurrentUser(authUser)
                action.accept(currentUser)
                if (authUser.isAuthorization) {
                    try {
                        val properties = MainActivity.getProperties()
                        properties.setProperties("refresh_token", if (!savePassword) null else String(Base64.encode(SimpleCipher.encodePassword(u.refreshToken.toByteArray()), Base64.DEFAULT)))
                        properties.setProperties("user_id", if (!savePassword) null else u.id.toString())
                        properties.setProperties("save", java.lang.Boolean.toString(savePassword))
                        properties.save()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: IllegalBlockSizeException) {
                        e.printStackTrace()
                    } catch (e: BadPaddingException) {
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        }
    }

    fun refreshCurrentUser(action: Consumer<User?>) {
        if (currentUser.refreshToken != null) {

            CoroutineScope(Dispatchers.IO).launch {
                val u = RefreshTokenTaskKt(currentUser.refreshToken, currentUser.id).refreshToken()
                setCurrentUser(u)
                action.accept(u)
                if (u.isAuthorization) {
                    try {
                        val properties = MainActivity.getProperties()
                        val save = java.lang.Boolean.parseBoolean(properties.getValue("save").orElse("false"))
                        if (save) {
                            properties.setProperties("refresh_token", String(Base64.encode(SimpleCipher.encodePassword(u.refreshToken.toByteArray()), Base64.DEFAULT)))
                            properties.setProperties("user_id", u.id.toString())
                            properties.setProperties("save", java.lang.Boolean.toString(save))
                            properties.save()
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: BadPaddingException) {
                        e.printStackTrace()
                    } catch (e: IllegalBlockSizeException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun refreshSavedUser(action: Consumer<User?>) {
        val properties = MainActivity.getProperties()
        val refreshTokenOptional = properties.getValue("refresh_token")
        val userIdOptional = properties.getValue("user_id")
        if (refreshTokenOptional.isPresent && userIdOptional.isPresent) {
            try {
                var refreshToken = refreshTokenOptional.get()
                val userId = userIdOptional.get().toLong()
                refreshToken = String(SimpleCipher.decodePassword(Base64.decode(refreshToken.toByteArray(), Base64.DEFAULT)))
                CoroutineScope(Dispatchers.IO).launch {
                    val u = RefreshTokenTaskKt(refreshToken, userId).refreshToken()
                    setCurrentUser(u)
                    action.accept(u)
                    if (u.isAuthorization) {
                        try {
                            properties.setProperties("refresh_token", String(Base64.encode(SimpleCipher.encodePassword(u.refreshToken.toByteArray()), Base64.DEFAULT)))
                            properties.setProperties("user_id", u.id.toString())
                            properties.setProperties("save", properties.getValue("save").orElse("false"))
                            properties.save()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } catch (e: IllegalBlockSizeException) {
                            e.printStackTrace()
                        } catch (e: BadPaddingException) {
                            e.printStackTrace()
                        }
                    }
                }
            } catch (e: BadPaddingException) {
                e.printStackTrace()
            } catch (e: IllegalBlockSizeException) {
                e.printStackTrace()
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
        }
    }

    fun getCurrentUser(): User? {
        return currentUser
    }

    fun exit() {
        setCurrentUser(User.getInstance())
        val properties = MainActivity.getProperties()
        try {
            properties.remove("refresh_token")
            properties.remove("user_id")
            properties.remove("save")
            properties.save()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun setCurrentUser(user: User) {
        this.currentUser.authStatus = user.authStatus
        this.currentUser.isAuthorization = user.isAuthorization
        this.currentUser.email = user.email
        this.currentUser.refreshToken = user.refreshToken
        this.currentUser.accessToken = user.accessToken
        this.currentUser.isBan = user.isBan
        this.currentUser.isEnabled = user.isEnabled
        this.currentUser.username = user.username
        this.currentUser.id = user.id
        this.currentUser.article = user.article
        this.currentUser.comment = user.comment
        this.currentUser.likeDislikes = user.likeDislikes
        this.currentUser.password = user.password
        this.currentUser.roles = user.roles
        this.currentUser.token = user.token
    }


    companion object {

        private var userFactory: UserFactoryKt2? = null

        @JvmStatic
        fun getInstance(): UserFactoryKt2 {
            if (userFactory == null) userFactory - UserFactoryKt2()
            return userFactory!!
        }


    }

}