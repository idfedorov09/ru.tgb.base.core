package ru.idfedorov09.telegram.bot.base.util.mapper

import org.mapstruct.Mapper

@Mapper
interface IMapper<D, E> {
    fun toEntity(dto: D): E
    fun toDTO(entity: E): D
}