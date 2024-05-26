package ru.idfedorov09.telegram.bot.base.domain.registry

data class CallbackCommand(
    val command: String,
    val description: String? = null,
)