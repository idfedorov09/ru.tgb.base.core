package ru.idfedorov09.telegram.bot.base.domain.registry

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object LastUserActionTypeRegistry {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
    private val luatMap: MutableMap<String, LastUserActionType> = mutableMapOf()

    fun register(
        mark: String,
        description: String? = null
    ): LastUserActionType {
        if (luatMap.contains(mark))
            throw IllegalArgumentException("Command $mark already exists.")
        val registered = LastUserActionType(mark, description)
        luatMap[mark] = registered
        log.debug("Last user action type registered successfully: {}", registered)
        return registered
    }

    fun getCommand(command: String) = luatMap[command]
}