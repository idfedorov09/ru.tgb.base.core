package ru.idfedorov09.telegram.bot.base.config.registry

import ru.idfedorov09.kotbot.domain.model.SmartString

data class LastUserActionType(
    val type: SmartString<LastUserActionType>,
    val description: String? = null,
    private val shouldRegister: Boolean = true,
): RegistryModel(LastUserActionType::class, type.getWithoutParams()!!) {

    constructor(
        type: String,
        description: String? = null,
    ): this(
        type = SmartString(type),
        description = description,
    )

    init { if (shouldRegister) registerModel() }

    fun copy(
        type: SmartString<LastUserActionType> = this.type,
        description: String? = this.description,
    ) = LastUserActionType(type.copy(), description, false)

    // TODO: тесты
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LastUserActionType) return false
        return this.type.getWithoutParams() == other.type.getWithoutParams()
    }

    override fun hashCode(): Int {
        return type.getWithoutParams()?.hashCode() ?: 0
    }
}