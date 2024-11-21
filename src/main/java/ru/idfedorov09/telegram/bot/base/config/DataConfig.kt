package ru.idfedorov09.telegram.bot.base.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.idfedorov09.telegram.bot.base.domain.dto.CallbackDataDTO
import ru.idfedorov09.telegram.bot.base.domain.service.CallbackDataService

@Configuration
open class DataConfig {
    @Bean
    open fun callbackDataDTOInitializer(
        callbackDataService: CallbackDataService
    ): CallbackDataDTO.Companion {
        CallbackDataDTO.init(callbackDataService)
        return CallbackDataDTO
    }
}