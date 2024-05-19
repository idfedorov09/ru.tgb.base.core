package ru.idfedorov09.telegram.bot.base.controller

import kotlinx.coroutines.Dispatchers
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update
import ru.idfedorov09.telegram.bot.base.UpdatesHandler
import ru.idfedorov09.telegram.bot.base.UpdatesSender
import ru.idfedorov09.telegram.bot.base.data.GlobalConstants.QUALIFIER_SYSTEM_FLOW
import ru.idfedorov09.telegram.bot.base.data.enum.TextCommands
import ru.idfedorov09.telegram.bot.base.data.model.UpdateControllerParams
import ru.idfedorov09.telegram.bot.base.service.FlowBuilderService
import ru.mephi.sno.libs.flow.belly.FlowContext

class UpdatesController(
    private val flowBuilderService: FlowBuilderService
) : UpdatesSender(), UpdatesHandler {

    companion object {
        private val log = LoggerFactory.getLogger(UpdatesController::class.java)
    }

    private var hasAccessToSystemFlowAction: UpdatesController.(UpdateControllerParams) -> Boolean = { true }

    // @Async("infinityThread") // if u need full async execution
    // TODO: конфигурация запуска
    override fun handle(telegramBot: TelegramLongPollingBot, update: Update) {
        val params = UpdateControllerParams(
            telegramBot = telegramBot,
            update = update,
        )

        if (hasAccessToSystemFlow(params))
            startSystemFlow(params)

        if (shouldStartSelectedFlow(params))
            startSelectedFlow(params)
    }

    private fun hasAccessToSystemFlow(params: UpdateControllerParams): Boolean = hasAccessToSystemFlowAction(params)

    fun setHasAccessToSystemFlow(hasAccessToSystemFlow: UpdatesController.(UpdateControllerParams) -> Boolean) {
        hasAccessToSystemFlowAction = hasAccessToSystemFlow
    }

    private fun shouldStartSelectedFlow(params: UpdateControllerParams): Boolean {
        val update = params.update

        if (update.hasMessage() && update.message.hasText() && TextCommands.isTextCommand(update.message.text))
            return false

        return flowBuilderService.isFlowSelected()
    }

    private fun startSystemFlow(params: UpdateControllerParams) = startFlowByName(QUALIFIER_SYSTEM_FLOW, params)

    private fun startSelectedFlow(params: UpdateControllerParams) {
        val currentFlowName = flowBuilderService.getCurrentFlowName() ?: run {
            log.warn("Flow is not selected.")
            return
        }

        startFlowByName(currentFlowName, params)
    }

    private fun startFlowByName(name: String, params: UpdateControllerParams) {
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
}
