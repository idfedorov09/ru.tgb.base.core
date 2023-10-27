package ru.idfedorov09.telegram.bot.flow

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.idfedorov09.telegram.bot.data.enums.BotStage
import ru.idfedorov09.telegram.bot.fetcher.TestFetcher
import ru.idfedorov09.telegram.bot.fetcher.ToggleStageFetcher

/**
 * Основной класс, в котором строится последовательность вычислений (граф)
 */
@Configuration
open class FlowConfiguration(
    private val testFetcher: TestFetcher,
    private val toggleStageFetcher: ToggleStageFetcher,
) {

    /**
     * Возвращает построенный граф; выполняется только при запуске приложения
     */
    @Bean(name = ["flowBuilder"])
    open fun flowBuilder(): FlowBuilder {
        val flowBuilder = FlowBuilder()
        flowBuilder.buildFlow()
        return flowBuilder
    }

    private fun FlowBuilder.buildFlow() {
        group {
            fetch(toggleStageFetcher)
            whenComplete(condition = { it.get<ExpContainer>()?.botStage == BotStage.APPEAL }) {
                fetch(testFetcher)
            }
        }
    }
}
