package ru.idfedorov09.telegram.bot.base.config.registry

import ru.idfedorov09.kotbot.domain.model.SmartString

data class LastUserActionType(
    val type: SmartString<LastUserActionType>,
    val description: String? = null,
): RegistryModel(LastUserActionType::class, type()!!) {

    constructor(
        type: String,
        description: String? = null,
    ): this(
        type = SmartString(type),
        description = description,
    )

    init { registerModel() }
}