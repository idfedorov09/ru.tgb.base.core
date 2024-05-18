package ru.idfedorov09.telegram.bot.base.controller

import kotlinx.coroutines.Dispatchers
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update
import ru.idfedorov09.telegram.bot.base.UpdatesHandler
import ru.idfedorov09.telegram.bot.base.UpdatesSender
import ru.idfedorov09.telegram.bot.base.data.GlobalConstants.QUALIFIER_SYSTEM_FLOW
import ru.idfedorov09.telegram.bot.base.service.FlowBuilderService
import ru.idfedorov09.telegram.bot.base.service.RedisService
import ru.mephi.sno.libs.flow.belly.FlowBuilder
import ru.mephi.sno.libs.flow.belly.FlowContext
import java.time.format.DateTimeFormatter

@Component
class UpdatesController(
    private val flowBuilderService: FlowBuilderService,
) : UpdatesSender(), UpdatesHandler {

    companion object {
        private val log = LoggerFactory.getLogger(UpdatesController::class.java)
    }

    @Autowired
    @Qualifier(QUALIFIER_SYSTEM_FLOW)
    private lateinit var systemFlowBuilder: FlowBuilder

    // @Async("infinityThread") // if u need full async execution
    override fun handle(telegramBot: TelegramLongPollingBot, update: Update) {
        val params = Params(
            telegramBot = telegramBot,
            update = update,
        )

        startSystemFlow(params)
        startSelectedFlow(params)
    }

    private fun startSystemFlow(params: Params) = startFlowByName(QUALIFIER_SYSTEM_FLOW, params)

    private fun startSelectedFlow(params: Params) {
        val currentFlowName = flowBuilderService.getCurrentFlowName() ?: run {
            log.warn("Flow is not selected.")
            return
        }

        startFlowByName(currentFlowName, params)
    }

    private fun startFlowByName(name: String, params: Params) {
        val flowBuilder = flowBuilderService.getByName(name) ?: run {
            log.error("Flow with name $name not found.")
            return
        }

        // Во время каждой прогонки графа создается свой контекст,
        // в который кладется бот и само обновление
        val flowContext = FlowContext()

        // прогоняем граф с ожиданием
        flowBuilder.initAndRun(
            flowContext = flowContext,
            dispatcher = Dispatchers.Default,
            wait = true,
            params.telegramBot,
            params.update,
        )
    }

    private data class Params(
        val telegramBot: TelegramLongPollingBot,
        val update: Update,
    )
}
