package ru.idfedorov09.telegram.bot.base.service

import org.springframework.stereotype.Service
import ru.idfedorov09.telegram.bot.base.data.GlobalConstants.CURRENT_FLOW_PREFIX
import ru.mephi.sno.libs.flow.belly.FlowBuilder

@Service
class FlowBuilderService(
    private val redisService: RedisService,
) {

    private val flowBuilderMap = HashMap<String, FlowBuilder>()

    fun register(name: String, flowBuilder: FlowBuilder) {
        if (flowBuilderMap.contains(name))
            throw IllegalStateException("Flow already registered: $name")

        flowBuilderMap[name] = flowBuilder
    }

    fun getByName(name: String) = flowBuilderMap[name]

    fun getFlowBuilders() = flowBuilderMap

    fun getCurrentFlowName() = redisService.getSafe(CURRENT_FLOW_PREFIX)

    fun getCurrentFlow() = flowBuilderMap[getCurrentFlowName()]
}