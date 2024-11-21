package ru.idfedorov09.telegram.bot.base.domain

import ru.idfedorov09.telegram.bot.base.config.registry.LastUserActionType

object LastUserActionTypes {
    val DEFAULT = LastUserActionType(
        type = "DEFAULT",
        description = "Дефолтный LUAT",
    )
}