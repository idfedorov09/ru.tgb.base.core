package ru.idfedorov09.telegram.bot.base.config

import jakarta.annotation.PostConstruct
import org.aspectj.lang.annotation.Aspect
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import ru.mephi.sno.libs.flow.config.BaseFlowConfiguration

@Aspect
@Component
class FlowConfig(private val applicationContext: ApplicationContext) {

    @PostConstruct
    fun buildFlows() {
        val flowBeans = applicationContext.getBeansOfType(BaseFlowConfiguration::class.java)
        flowBeans.values.forEach {
            it.flowBuilder()
        }
    }
}
