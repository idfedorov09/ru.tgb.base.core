package ru.idfedorov09.telegram.bot.data.model

data class RedisServerData(
    val port: Int,
    val host: String,
    val password: String?, // если пароль не задан, то пробуем подключиться без него
)
