package ru.idfedorov09.telegram.bot.base.domain.dto

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import ru.idfedorov09.telegram.bot.base.domain.entity.CallbackDataEntity
import ru.idfedorov09.telegram.bot.base.provider.IMapperProvider
import ru.idfedorov09.telegram.bot.base.util.mapper.DtoEntityMapper

data class CallbackDataDTO(
    val id: Long? = null,
    val chatId: String? = null,
    val messageId: String? = null,
    val callbackData: String? = null,
    val metaText: String? = null,
    val metaUrl: String? = null,
): BaseDTO<CallbackDataDTO, CallbackDataEntity>() {
    /**
     * Создает кнопку, которуж можно добавить в клавиатуру и отправить пользователю
     */
    fun createKeyboard() = InlineKeyboardButton().also {
        it.text = metaText!!
        it.callbackData = id!!.toString()
        it.url = metaUrl
    }
}
