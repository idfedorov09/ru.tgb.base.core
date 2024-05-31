package ru.idfedorov09.telegram.bot.base.domain.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.idfedorov09.telegram.bot.base.domain.entity.UserEntity
import ru.idfedorov09.telegram.bot.base.repository.UserRepository
import kotlin.jvm.optionals.getOrNull

@Service
class UserService {
    @Autowired
    lateinit var userRepository: UserRepository<UserEntity>

    open fun findById(id: Long) = userRepository.findById(id).getOrNull()?.mapper()?.toDTO()

    // TODO: сохранить с учетом userData
    open fun save(userEntity: UserEntity): UserEntity? = null

}