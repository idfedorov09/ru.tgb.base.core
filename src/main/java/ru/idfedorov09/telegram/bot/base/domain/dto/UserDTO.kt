package ru.idfedorov09.telegram.bot.base.domain.dto

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
    var isDeleted: Boolean = false,
    var currentKeyboardType: ReplyKeyboardType? = null,
    var isKeyboardSwitched: Boolean = false,
): BaseDTO<UserEntity>() {
    override fun toEntity() = UserEntity(
        id = id,
        tui = tui,
        lastTgNick = lastTgNick,
        roles = roles,
        lastUserActionType = lastUserActionType,
        isDeleted = isDeleted,
        currentKeyboardType = currentKeyboardType,
        isKeyboardSwitched = isKeyboardSwitched,
    )
}