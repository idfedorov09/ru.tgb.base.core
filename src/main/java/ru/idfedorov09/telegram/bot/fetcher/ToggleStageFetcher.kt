package ru.idfedorov09.telegram.bot.fetcher

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update
import ru.idfedorov09.telegram.bot.data.enums.BotStage
import ru.idfedorov09.telegram.bot.executor.TelegramPollingBot
import ru.idfedorov09.telegram.bot.fetcher.general.GeneralFetcher
import ru.idfedorov09.telegram.bot.flow.ExpContainer
import ru.idfedorov09.telegram.bot.flow.InjectData
import ru.idfedorov09.telegram.bot.service.RedisService
import ru.idfedorov09.telegram.bot.util.UpdatesUtil

@Component
class ToggleStageFetcher(
    private val updatesUtil: UpdatesUtil,
    private val redisService: RedisService,
) : GeneralFetcher() {
    companion object {
        private val log = LoggerFactory.getLogger(this.javaClass)
    }

    @InjectData
    fun doFetch(
        update: Update,
        bot: TelegramPollingBot,
        exp: ExpContainer,
    ) {
        exp.botStage = redisService.getSafe("TEST____stageeeee").let {
            if (it == null) {
                exp.botStage
            } else {
                BotStage.valueOf(it)
            }
        }

        if (exp.botStage == BotStage.GAME) {
            exp.botStage = BotStage.APPEAL
        } else {
            exp.botStage = BotStage.GAME
        }
        redisService.setValue("TEST____stageeeee", exp.botStage.name)
        log.info("changed botStage to {}", exp.botStage)
    }
}
