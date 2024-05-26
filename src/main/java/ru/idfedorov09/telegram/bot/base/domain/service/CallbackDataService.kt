package ru.idfedorov09.telegram.bot.base.domain.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.idfedorov09.telegram.bot.base.domain.data.model.entity.CallbackData
import ru.idfedorov09.telegram.bot.base.domain.repository.CallbackDataRepository
import kotlin.jvm.optionals.getOrNull

@Service
class CallbackDataService() {
    @Autowired
    lateinit var callbackDataRepository: CallbackDataRepository<CallbackData>

    fun findById(id: Long) = callbackDataRepository.findById(id).getOrNull()

    fun save(callbackData: CallbackData): CallbackData = callbackDataRepository.save(callbackData)
}