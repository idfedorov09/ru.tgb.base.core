package ru.idfedorov09.telegram.bot.base.config.registry

import kotlin.reflect.KClass

abstract class RegistryModel(
    private val origin: KClass<out RegistryModel>,
    open val mark: String,
) {

    init {
        RegistryHolder.addRegistry(this::class)
    }

    protected fun registerModel() {
        RegistryHolder.register(this)
    }

    internal fun getRegistryClass(): KClass<out RegistryModel> = origin

    operator fun invoke() = mark
}
