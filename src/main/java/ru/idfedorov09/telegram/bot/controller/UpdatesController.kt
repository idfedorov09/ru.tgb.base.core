package ru.idfedorov09.telegram.bot.controller

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update
import ru.idfedorov09.telegram.bot.UpdatesHandler
import ru.idfedorov09.telegram.bot.UpdatesSender
import ru.idfedorov09.telegram.bot.flow.ExpContainer
import ru.idfedorov09.telegram.bot.service.RedisService
import ru.idfedorov09.telegram.bot.util.UpdatesUtil
import ru.mephi.sno.libs.flow.belly.FlowBuilder
import ru.mephi.sno.libs.flow.belly.FlowContext

@Component
class UpdatesController : UpdatesSender(), UpdatesHandler {

    @Autowired
    private lateinit var flowBuilder: FlowBuilder

    @Autowired
    private lateinit var updatesUtil: UpdatesUtil

    @Autowired
    private lateinit var redisService: RedisService

    companion object {
        private val log = LoggerFactory.getLogger(this.javaClass)
    }

    // TODO: удалить
    @Deprecated(message = "ненужно, тк внутри лежат сервисы. Зачем в свой контекст сувать, если есть спринг?")
    private fun toContext() = listOf<Any>(
        updatesUtil,
        redisService,
    )

    /**
     * Функция, которая возвращает то, что нужно обновить в контексте перед вызовои графа
     */
    private fun generateObjectToResetFlowContext() = arrayOf<Any>(
        ExpContainer(),
    )

    // @Async("infinityThread") // if u need full async execution
    @OptIn(DelicateCoroutinesApi::class)
    override fun handle(telegramBot: TelegramLongPollingBot, update: Update) {
        // Во время каждой прогонки графа создается свой контекст,
        // в который кладется бот и само обновление
        var flowContext = FlowContext()
        flowContext.insertObject(telegramBot)
        flowContext.insertObject(update)

        toContext().forEach { flowContext.insertObject(it) }

        val flowJob = GlobalScope.launch {
            flowBuilder.initAndRun(
                flowContext = flowContext,
                objectsToReset = generateObjectToResetFlowContext(),
            )
            // TODO: подумать, что сделать с этим; возможно, это лишнее действие
            flowContext.clear()
        }

        // ожидаем, когда граф прогонится
        runBlocking {
            flowJob.join()
        }
    }
}
