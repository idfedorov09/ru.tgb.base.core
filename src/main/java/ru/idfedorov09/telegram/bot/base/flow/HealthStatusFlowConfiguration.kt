package ru.idfedorov09.telegram.bot.base.flow

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.idfedorov09.telegram.bot.base.data.GlobalConstants.QUALIFIER_FLOW_HEALTH_STATUS
import ru.idfedorov09.telegram.bot.base.fetchers.health.HealthStatusMapper
import ru.mephi.sno.libs.flow.belly.FlowBuilder

@Configuration
open class HealthStatusFlowConfiguration(
    private val healthStatusMapper: HealthStatusMapper,
) {

    @Bean(QUALIFIER_FLOW_HEALTH_STATUS)
    open fun flowBuilder(): FlowBuilder {
        val flowBuilder = FlowBuilder()
        flowBuilder.buildFlow()
        return flowBuilder
    }

    private fun FlowBuilder.buildFlow() {
        fetch(healthStatusMapper)
    }
}
