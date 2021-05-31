package com.ametr1ne.overdiff.utils

import java.io.IOException
import java.util.*

interface Properties {
    fun setProperties(key: String, value: String?)
    fun getValue(key: String): String?

    @Throws(IOException::class)
    suspend fun save()
}