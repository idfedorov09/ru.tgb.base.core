package ru.idfedorov09.telegram.bot.base.util

import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update
import ru.idfedorov09.telegram.bot.base.controller.UpdatesController
import ru.idfedorov09.telegram.bot.base.domain.service.RedisService
import ru.idfedorov09.telegram.bot.base.domain.service.UserQueue
import java.util.concurrent.Executors

@Component
class OnReceiver(
    private val redisService: RedisService,
    private val updatesUtil: UpdatesUtil,
    private val userQueue: UserQueue,
    private val updatesHandler: UpdatesController,
) {
    companion object {
        private val log = LoggerFactory.getLogger(OnReceiver::class.java)
    }

    /** Обрабатывает отдельное обновление **/
    private fun execOne(update: Update, executor: TelegramLongPollingBot) {
        log.info("Update received: $update")
        updatesHandler.handle(executor, update)
    }

    /** Обрабатывает пришедшее обновление, затем обрабатывая все, что есть в очереди **/
    private fun exec(update: Update, executor: TelegramLongPollingBot) {
        val chatId = updatesUtil.getChatId(update)

        if (chatId == null) {
            execOne(update, executor)
            return
        }

        val chatKey = updatesUtil.getChatKey(chatId)

        if (redisService.getSafe(chatKey) == null) {
            redisService.setValue(chatKey, "1")
            execOne(update, executor)
            redisService.del(chatKey)

            val upd = userQueue.popUpdate(chatId)
            upd?.let { onReceive(upd, executor) }
        } else {
            userQueue.push(update, chatId)
        }
    }

    fun onReceive(update: Update, executor: TelegramLongPollingBot) {
        CoroutineScope(Dispatchers.IO).launch {
            exec(update, executor)
        }
    }
}
