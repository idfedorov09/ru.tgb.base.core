package ru.idfedorov09.telegram.bot.base.domain.registry

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object TextCommandRegistry {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
    private val commandsMap: MutableMap<String, TextCommand> = mutableMapOf()

    fun register(
        command: String,
        allowedRoles: List<UserRole> = listOf(/* TODO: use role */),
        description: String? = "",
        showInHelp: Boolean = true,
    ): TextCommand {
        if (commandsMap.contains(command))
            throw IllegalArgumentException("Command $command already exists.")
        val registered = TextCommand(command, allowedRoles, description, showInHelp)
        commandsMap[command] = registered
        log.debug("Text command registered successfully: {}", registered)
        return registered
    }

    fun getCommand(command: String) = commandsMap[command]
}