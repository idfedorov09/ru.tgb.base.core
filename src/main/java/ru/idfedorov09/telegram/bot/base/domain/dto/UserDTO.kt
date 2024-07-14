package ru.idfedorov09.telegram.bot.base.domain.dto

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import ru.idfedorov09.telegram.bot.base.config.registry.LastUserActionType
import ru.idfedorov09.telegram.bot.base.config.registry.ReplyKeyboardType
import ru.idfedorov09.telegram.bot.base.config.registry.UserRole
import ru.idfedorov09.telegram.bot.base.domain.entity.UserEntity
import ru.mephi.sno.libs.flow.belly.Mutable

@Mutable
data class UserDTO(
    val id: Long? = null,
    val tui: String? = null,
    var lastTgNick: String? = null,
    var roles: Set<UserRole> = mutableSetOf(),
    var lastUserActionType: LastUserActionType? = null,
    var data: String? = null,
    var isDeleted: Boolean = false,
    var currentKeyboardType: ReplyKeyboardType? = null,
    var isKeyboardSwitched: Boolean = false,
): BaseDTO<UserEntity>() {
    private val log = LoggerFactory.getLogger(this::class.java)
    private val objectMapper = ObjectMapper()

    fun <T> userData(clazz: Class<T>): T? {
        return data?.let {
            runCatching {
                objectMapper.readValue(it, clazz)
            }.onFailure { e ->
                log.error("Error while reading user_data: {}\n{}", e, e.stackTraceToString())
            }.getOrNull()
        }
    }

    inline fun <reified T> userData() = userData(T::class.java)

    override fun toEntity() = UserEntity(
        id = id,
        tui = tui,
        lastTgNick = lastTgNick,
        roles = roles,
        lastUserActionType = lastUserActionType,
        data = data,
        isDeleted = isDeleted,
        currentKeyboardType = currentKeyboardType,
        isKeyboardSwitched = isKeyboardSwitched,
    )
}