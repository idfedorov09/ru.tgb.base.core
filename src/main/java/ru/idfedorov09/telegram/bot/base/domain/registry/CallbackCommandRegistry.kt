package ru.idfedorov09.telegram.bot.base.domain.registry

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * 'Центр' управления коллбэками (кнопками)
 */
object CallbackCommandRegistry {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
    private val commandsMap: MutableMap<String, CallbackCommand> = mutableMapOf()

    fun register(
        command: String,
        description: String? = null
    ): CallbackCommand {
        if (commandsMap.contains(command))
            throw IllegalArgumentException("Command $command already exists.")
        val registered = CallbackCommand(command, description)
        commandsMap[command] = registered
        log.debug("CallBack command registered successfully: {}", registered)
        return registered
    }

    fun getCommand(command: String) = commandsMap[command]
}