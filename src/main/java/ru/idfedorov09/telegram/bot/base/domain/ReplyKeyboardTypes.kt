package ru.idfedorov09.telegram.bot.base.domain

import ru.idfedorov09.telegram.bot.base.config.registry.IReplyButtonBuilder
import ru.idfedorov09.telegram.bot.base.config.registry.ReplyKeyboardType
import ru.idfedorov09.telegram.bot.base.config.registry.TextCommand
import ru.idfedorov09.telegram.bot.base.config.registry.UserRole

object ReplyKeyboardTypes {
    val EMPTY = ReplyKeyboardType(
        "empty",
        builder = object : IReplyButtonBuilder {
            override fun build(roles: Set<UserRole>): MutableList<MutableList<TextCommand>> {
                return mutableListOf(mutableListOf())
            }
        }
    )
}