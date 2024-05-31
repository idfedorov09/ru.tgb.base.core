package ru.idfedorov09.telegram.bot.base.domain.dto

import ru.idfedorov09.telegram.bot.base.provider.IMapperProvider
import ru.idfedorov09.telegram.bot.base.util.mapper.DtoEntityMapper

open class BaseDTO<D, E>: IMapperProvider<D, E> {
    private val mapper = DtoEntityMapper.withDTO<D, E>()
    override fun mapper() = mapper.origin(this)
}