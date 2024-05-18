package ru.idfedorov09.telegram.bot.base.fetchers.bot

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import ru.idfedorov09.telegram.bot.base.data.enum.TextCommands
import ru.idfedorov09.telegram.bot.base.executor.Executor
import ru.idfedorov09.telegram.bot.base.service.FlowBuilderService
import ru.idfedorov09.telegram.bot.base.util.UpdatesUtil
import ru.mephi.sno.libs.flow.belly.InjectData
import ru.mephi.sno.libs.flow.fetcher.GeneralFetcher

@Component
class ChooseFlowFetcher(
    private val updatesUtil: UpdatesUtil,
    private val bot: Executor,
    private val flowBuilderService: FlowBuilderService,
) : GeneralFetcher() {

    private val SEPARATOR = "@##@"
    private val selectFlowCallbackPrefix = "#selectFlow$SEPARATOR"
    private val selectFlowCallbackPrefixForced = "#selectFlowF$SEPARATOR"

    @InjectData
    fun doFetch(
        update: Update,
    ) {
        when {
            update.hasMessage() && update.message.hasText() -> textCommandsHandler(update)
            update.hasCallbackQuery() -> callbackQueryHandler(update)
            else -> defaultHandler(update)
        }
    }

    private fun defaultHandler(update: Update) {
        if (!flowBuilderService.isFlowSelected()) {
            bot.execute(
                SendMessage().apply {
                    text = "Привет! Флоу для работы бота еще не выбран. Выбери нужный из списка"
                    chatId = updatesUtil.getChatId(update)!!
                    replyMarkup = flowSelectKeyboard()
                }
            )
        }
    }

    private fun textCommandsHandler(update: Update) {
        val text = update.message.text

        return text.run {
            when {
                startsWith(TextCommands.CHANGE_FLOW_COMMAND()) -> changeFlow(update)
                else -> commonTextHandler(update)
            }
        }
    }

    private fun commonTextHandler(update: Update) {
        defaultHandler(update)
    }

    private fun callbackQueryHandler(update: Update) {
        val callbackData = update.callbackQuery.data

        callbackData.apply {
            when {
                startsWith(selectFlowCallbackPrefix) -> selectFlow(update, false)
                startsWith(selectFlowCallbackPrefixForced) -> selectFlow(update, true)
                else -> defaultHandler(update)
            }
        }
    }

    private fun changeFlow(update: Update) {
        bot.execute(
            SendMessage().apply {
                text = "Текущий флоу: ${flowBuilderService.getCurrentFlowName()}. " +
                        "Если хочешь сменить, выбери нужный флоу из списка"
                chatId = updatesUtil.getChatId(update)!!
                replyMarkup = flowSelectKeyboard(selectFlowCallbackPrefixForced)
            }
        )
    }

    private fun selectFlow(update: Update, forced: Boolean) {
        if (!forced && flowBuilderService.isFlowSelected()) {
            bot.execute(
                DeleteMessage().apply {
                    chatId = updatesUtil.getChatId(update)!!
                    messageId = update.callbackQuery.message.messageId
                }
            )
            return
        }
        val flowName = update.callbackQuery.data.split(SEPARATOR).lastOrNull() ?: run {
            bot.execute(
                SendMessage().apply {
                    text = "Выбран некорректный флоу."
                    chatId = updatesUtil.getChatId(update)!!
                }
            )
            return
        }

        val res = flowBuilderService.setSelectedFlow(flowName)
        if (!res) {
            bot.execute(
                SendMessage().apply {
                    text = "Такого флоу не существует."
                    chatId = updatesUtil.getChatId(update)!!
                }
            )
        } else {
            bot.execute(
                SendMessage().apply {
                    text = "Флоу успешно выбран"
                    chatId = updatesUtil.getChatId(update)!!
                }
            )
        }

        bot.execute(
            DeleteMessage().apply {
                chatId = updatesUtil.getChatId(update)!!
                messageId = update.callbackQuery.message.messageId
            }
        )
    }

    private fun flowSelectKeyboard(prefix: String = selectFlowCallbackPrefix): InlineKeyboardMarkup {
        val flowBuildersList = flowBuilderService.getFlowBuilders().keys.map {
            CallbackData(
                text = it,
                callbackData = "$prefix$it"
            )
        }
        return createKeyboard(*flowBuildersList.toTypedArray())
    }

    private fun createKeyboard(vararg callbackData: CallbackData): InlineKeyboardMarkup {
        val keyboard =
            listOf(*callbackData).map { button ->
                InlineKeyboardButton().also {
                    it.text = button.text
                    it.callbackData = button.callbackData
                }
            }.map { listOf(it) }
        return createKeyboard(keyboard)
    }

    private fun createKeyboard(keyboard: List<List<InlineKeyboardButton>>) = InlineKeyboardMarkup().also { it.keyboard = keyboard }

    private data class CallbackData(
        val text: String,
        val callbackData: String? = null,
        val url: String? = null,
    )
}
