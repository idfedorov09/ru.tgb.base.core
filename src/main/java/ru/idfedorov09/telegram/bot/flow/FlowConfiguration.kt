package ru.idfedorov09.telegram.bot.flow

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.idfedorov09.telegram.bot.fetcher.TestFetcher

/**
 * Основной класс, в котором строится последовательность вычислений (граф)
 */
@Configuration
open class FlowConfiguration(
    private val testFetcher: TestFetcher
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
            fetch(testFetcher)
        }
    }
}
