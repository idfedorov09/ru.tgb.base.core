package ru.idfedorov09.telegram.bot.base.domain.dto

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import ru.idfedorov09.telegram.bot.base.domain.entity.CallbackDataEntity

data class CallbackDataDTO(
    val id: Long? = null,
    val chatId: String? = null,
    val messageId: String? = null,
    var callbackData: String? = null,
    val metaText: String? = null,
    val metaUrl: String? = null,
) : BaseDTO<CallbackDataEntity>() {

    private val mapper = ObjectMapper()

    /**
     * Создает кнопку, которуж можно добавить в клавиатуру и отправить пользователю
     */
    fun createKeyboard() = InlineKeyboardButton().also {
        it.text = metaText!!
        it.callbackData = id!!.toString()
        it.url = metaUrl
    }

    override fun toEntity() = CallbackDataEntity(
        id = id,
        chatId = chatId,
        messageId = messageId,
        callbackData = callbackData,
        metaText = metaText,
        metaUrl = metaUrl,
    )

    // TODO: separator -> variable (const)
    private fun rewriteParamPart(value: String) {
        callbackData = callbackData?.let {
            val separatorPos = it.indexOf("&")
            if (separatorPos != -1) {
                it.substring(0, separatorPos) + "&" + value
            } else {
                "$it&$value"
            }
        } ?: "&$value"
    }

    fun setParameters(params: Map<String, Any>): CallbackDataDTO {
        val newParamPart = mapper.writeValueAsString(params)
        rewriteParamPart(newParamPart)
        return this
    }

    fun setParameters(vararg params: Pair<String, Any>) = setParameters(mapOf(*params))

    fun addParameters(params: Map<String, Any>): CallbackDataDTO {
        val existingParams: MutableMap<String, Any> = getParams().toMutableMap()
        existingParams.putAll(params)
        return setParameters(existingParams)
    }

    fun addParameters(vararg params: Pair<String, Any>) = addParameters(mapOf(*params))

    fun getParams(): Map<String, String> {
        val paramsPart = callbackData
            ?.split("&") // TODO: separator const
            ?.takeIf { it.size > 1 }
            ?.last()
            ?: return emptyMap()

        return paramsPart.jsonToMap()
    }

    private fun String.jsonToMap(): Map<String, String> {
        return mapper.readValue(this, object : TypeReference<Map<String, String>>() {})
    }
}
