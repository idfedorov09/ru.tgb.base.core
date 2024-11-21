package ru.idfedorov09.telegram.bot.base.util

import org.springframework.core.io.ClassPathResource
import java.io.InputStream

object ResourcesUtil {
    fun getContent(path: String): String {
        val resource = ClassPathResource(path)
        val inputStream: InputStream = resource.inputStream
        return inputStream.bufferedReader().use { it.readText() }
    }
}