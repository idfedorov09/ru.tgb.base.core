package ru.idfedorov09.telegram.bot.base.fetchers.bot

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import ru.idfedorov09.telegram.bot.base.executor.Executor
import ru.idfedorov09.telegram.bot.base.service.FlowBuilderService
import ru.idfedorov09.telegram.bot.base.util.UpdatesUtil
import ru.mephi.sno.libs.flow.belly.FlowBuilder
import ru.mephi.sno.libs.flow.belly.InjectData
import ru.mephi.sno.libs.flow.fetcher.GeneralFetcher

@Component
class TestFetcher(
    private val updatesUtil: UpdatesUtil,
    private val bot: Executor,
    private val flowBuilderService: FlowBuilderService,
) : GeneralFetcher() {

    @InjectData
    fun doFetch(
        update: Update,
    ) {
        val flowMap = flowBuilderService.getFlowBuilders()
        bot.execute(
            SendMessage().also {
                it.text = "Мапа:\n$flowMap"
                it.chatId = updatesUtil.getChatId(update)!!
            }
        )
    }
}
