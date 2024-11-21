package ru.idfedorov09.telegram.bot.base.domain.dto

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import ru.idfedorov09.kotbot.domain.model.SmartString
import ru.idfedorov09.telegram.bot.base.domain.entity.CallbackDataEntity
import ru.idfedorov09.telegram.bot.base.domain.service.CallbackDataService

data class CallbackDataDTO(
    val id: Long? = null,
    val chatId: String? = null,
    val messageId: String? = null,
    var callbackData: SmartString<CallbackDataDTO> = SmartString(),
    val metaText: String? = null,
    val metaUrl: String? = null,
) : BaseDTO<CallbackDataEntity>() {

    constructor(
        id: Long? = null,
        chatId: String? = null,
        messageId: String? = null,
        callbackData: String,
        metaText: String? = null,
        metaUrl: String? = null,
    ) : this(
        id = id,
        chatId = chatId,
        messageId = messageId,
        callbackData = SmartString(callbackData),
        metaText = metaText,
        metaUrl = metaUrl,
    )

    companion object {
        private lateinit var callbackDataService: CallbackDataService
        fun init(service: CallbackDataService) {
            callbackDataService = service
        }
    }

    /**
     * Создает кнопку, которуж можно добавить в клавиатуру и отправить пользователю
     */
    fun createKeyboard() = InlineKeyboardButton().also {
        it.text = metaText!!
        it.callbackData = id!!.toString()
        it.url = metaUrl
        callbackData.getParams()
    }

    override fun toEntity() = CallbackDataEntity(
        id = id,
        chatId = chatId,
        messageId = messageId,
        callbackData = callbackData(),
        metaText = metaText,
        metaUrl = metaUrl,
    )

    fun save() = callbackDataService.save(this)!!
}
