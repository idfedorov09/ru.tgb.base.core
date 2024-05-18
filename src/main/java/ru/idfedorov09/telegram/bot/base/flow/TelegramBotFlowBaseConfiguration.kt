package ru.idfedorov09.telegram.bot.base.flow

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.idfedorov09.telegram.bot.base.data.GlobalConstants.QUALIFIER_SYSTEM_FLOW
import ru.idfedorov09.telegram.bot.base.fetchers.bot.ChooseFlowFetcher
import ru.idfedorov09.telegram.bot.base.service.FlowBuilderService
import ru.mephi.sno.libs.flow.belly.FlowBuilder

/**
 * Основной класс, в котором строится последовательность вычислений (граф) для бота
 */
@Configuration
open class TelegramBotFlowBaseConfiguration(
    private val chooseFlowFetcher: ChooseFlowFetcher,
    private val flowBuilderService: FlowBuilderService,
) {

    /**
     * Возвращает построенный граф; выполняется только при запуске приложения
     */
    @Bean(QUALIFIER_SYSTEM_FLOW)
    open fun flowBuilder(): FlowBuilder {
        val flowBuilder = FlowBuilder()
        flowBuilder.buildFlow()
        flowBuilderService.register(QUALIFIER_SYSTEM_FLOW, flowBuilder)
        return flowBuilder
    }

    private fun FlowBuilder.buildFlow() {
        group {
            fetch(chooseFlowFetcher)
        }
    }
}
