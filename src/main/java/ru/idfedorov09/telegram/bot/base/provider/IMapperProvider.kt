package ru.idfedorov09.telegram.bot.base.provider

import ru.idfedorov09.telegram.bot.base.util.mapper.DtoEntityMapper

interface IMapperProvider<D, E> {
    fun mapper(): DtoEntityMapper<D, E>
}