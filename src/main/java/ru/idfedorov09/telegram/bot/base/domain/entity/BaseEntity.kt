package ru.idfedorov09.telegram.bot.base.domain.entity

import jakarta.persistence.Transient
import ru.idfedorov09.telegram.bot.base.domain.dto.BaseDTO
import ru.idfedorov09.telegram.bot.base.provider.IMapperProvider
import ru.idfedorov09.telegram.bot.base.util.mapper.DtoEntityMapper

open class BaseEntity<D: BaseDTO<D, E>, E> : IMapperProvider<D, E> {
    @Transient
    private val mapper = DtoEntityMapper.withEntity<D, E>()

    @Transient
    override fun mapper() = mapper.origin(this)
}