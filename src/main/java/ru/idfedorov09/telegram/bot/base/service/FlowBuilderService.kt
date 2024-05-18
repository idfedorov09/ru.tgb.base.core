package ru.idfedorov09.telegram.bot.base.service

import org.springframework.stereotype.Service
import ru.mephi.sno.libs.flow.belly.FlowBuilder

@Service
class FlowBuilderService {

    private val flowBuilderMap = HashMap<String, FlowBuilder>()

    fun register(name: String, flowBuilder: FlowBuilder) {
        flowBuilderMap[name] = flowBuilder
    }

    fun getFlowBuilders() = flowBuilderMap
}