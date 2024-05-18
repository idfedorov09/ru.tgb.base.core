package ru.idfedorov09.telegram.bot.base.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.idfedorov09.telegram.bot.base.controller.UpdatesController
import ru.idfedorov09.telegram.bot.base.data.GlobalConstants.CURRENT_FLOW
import ru.idfedorov09.telegram.bot.base.data.GlobalConstants.QUALIFIER_SYSTEM_FLOW
import ru.mephi.sno.libs.flow.belly.FlowBuilder

@Service
class FlowBuilderService(
    private val redisService: RedisService,
) {
    private val flowBuilderMap = HashMap<String, FlowBuilder>()

    companion object {
        private val log = LoggerFactory.getLogger(UpdatesController::class.java)
    }

    fun register(name: String, flowBuilder: FlowBuilder) {
        if (flowBuilderMap.contains(name))
            throw IllegalStateException("Flow already registered: $name")

        flowBuilderMap[name] = flowBuilder
    }

    fun getByName(name: String) = flowBuilderMap[name]

    fun getFlowBuilders() = flowBuilderMap

    fun getCurrentFlowName() = redisService.getSafe(CURRENT_FLOW)

    fun getCurrentFlow() = flowBuilderMap[getCurrentFlowName()]

    fun setSelectedFlow(name: String): Boolean{
        if (!flowBuilderMap.contains(name)) {
            log.warn("Flow doesn't exist: $name")
            return false
        }
        redisService.setValue(CURRENT_FLOW, name)
        return true
    }

    fun isFlowSelected() = !(getCurrentFlowName() == null || getCurrentFlowName() == QUALIFIER_SYSTEM_FLOW)
}