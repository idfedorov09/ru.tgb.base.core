package ru.idfedorov09.telegram.bot.base.domain.registry

data class TextCommand(
    /** текст команды **/
    val command: String,
    /** роли которым доступна эта команда **/
    val allowedRoles: List<UserRole> = listOf(/* TODO: user role*/),
    /** описание **/
    val description: String? = "",
    /** использование в меню помощи **/
    val showInHelp: Boolean = true,
)