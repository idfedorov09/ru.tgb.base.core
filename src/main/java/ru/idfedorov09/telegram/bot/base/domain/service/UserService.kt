package ru.idfedorov09.telegram.bot.base.domain.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.idfedorov09.telegram.bot.base.domain.dto.UserDTO
import ru.idfedorov09.telegram.bot.base.domain.entity.UserEntity
import ru.idfedorov09.telegram.bot.base.repository.UserRepository
import kotlin.jvm.optionals.getOrNull

@Service
class UserService {
    @Autowired
    lateinit var userRepository: UserRepository<UserEntity>

    open fun findById(id: Long) = userRepository.findById(id).getOrNull()?.toDTO()

    open fun save(userEntity: UserEntity): UserEntity? {
        return userRepository.save(userEntity)
    }

    open fun save(userDTO: UserDTO) = save(userDTO.toEntity())?.toDTO()

    open fun findNotDeletedByTui(tui: String) =
        userRepository.findNotDeletedByTui(tui)?.toDTO()

    open fun updateKeyboardSwitchedForUserTui(
        tui: String,
        isSwitched: Boolean,
    ) = userRepository.updateKeyboardSwitchedForUserTui(tui, isSwitched)
}