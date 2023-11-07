package ru.idfedorov09.telegram.bot.controller

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import ru.idfedorov09.telegram.bot.data.GlobalConstants.ENDPOINT_HEAL
import ru.idfedorov09.telegram.bot.data.GlobalConstants.QUALIFIER_FLOW_HEALTH_STATUS
import ru.idfedorov09.telegram.bot.data.model.HealthStatus
import ru.mephi.sno.libs.flow.belly.FlowBuilder
import ru.mephi.sno.libs.flow.belly.FlowContext

@RestController
class HealthStatusController {

    @Autowired
    @Qualifier(QUALIFIER_FLOW_HEALTH_STATUS)
    private lateinit var flowBuilder: FlowBuilder

    // TODO: узнать статус бота (пройтись по редису, постгресу и прочему, собрать ошибки и тд)
    @GetMapping(ENDPOINT_HEAL)
    @OptIn(DelicateCoroutinesApi::class)
    fun healthStatusCheck(): HealthStatus {
        // контекст для флоу сбора статуса работы бота
        val flowContext = FlowContext()

        val flowJob = GlobalScope.launch {
            flowBuilder.initAndRun(flowContext = flowContext)
        }

        runBlocking { flowJob.join() }

        return flowContext.get<HealthStatus>()
            ?: throw NoSuchFieldException(
                "Health flow no contains ServiceStatus. Make sure HealthStatusFlow working correct.",
            )
    }
}
