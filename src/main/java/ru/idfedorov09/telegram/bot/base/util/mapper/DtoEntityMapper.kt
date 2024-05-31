package ru.idfedorov09.telegram.bot.base.util.mapper

import org.mapstruct.factory.Mappers

class DtoEntityMapper<D, E> private constructor(
    private val mapper: IMapper<D, E>,
    private val isDto: Boolean,
    private val isEntity: Boolean,
) {
    private var origin: Any? = null

    @Suppress("UNCHECKED_CAST")
    fun toEntity(): E {
        origin ?: throw NullPointerException("Can't map dto to $origin")
        if (!isDto)
            throw RuntimeException("Can't map dto to $origin")
        return mapper.toEntity(origin as D)
    }

    @Suppress("UNCHECKED_CAST")
    fun toDTO(): D {
        origin ?: throw NullPointerException("Can't map entity to $origin")
        if (!isEntity)
            throw RuntimeException("Can't map entity to $origin")
        return mapper.toDTO(origin as E)
    }

    fun origin(origin: Any): DtoEntityMapper<D, E> {
        this.origin = origin
        return this
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun <D, E> withDTO(): DtoEntityMapper<D, E> {
            val mapper = Mappers.getMapper(IMapper::class.java) as IMapper<D, E>
            return DtoEntityMapper(
                mapper = mapper,
                isDto = true,
                isEntity = false
            )
        }

        @Suppress("UNCHECKED_CAST")
        fun <D, E> withEntity(): DtoEntityMapper<D, E> {
            val mapper = Mappers.getMapper(IMapper::class.java) as IMapper<D, E>
            return DtoEntityMapper(
                mapper = mapper,
                isDto = false,
                isEntity = true
            )
        }
    }
}