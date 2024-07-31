package ru.idfedorov09.telegram.bot.base.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import ru.idfedorov09.kotbot.domain.model.SmartString
import ru.idfedorov09.telegram.bot.base.domain.dto.CallbackDataDTO

@Entity
@Table(name = "callback_data")
open class CallbackDataEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "callback_id")
    open val id: Long? = null,
    /** id сообщения кнопки **/
    @Column(name = "chat_id")
    open var chatId: String? = null,
    /** чат, в котором эта кнопка находится **/
    @Column(name = "message_id")
    open var messageId: String? = null,
    /** информация, хранящаяся в коллбеке **/
    @Column(name = "data", columnDefinition = "TEXT")
    open var callbackData: String? = null,
    /** Текст на кнопке**/
    @Column(name = "text", columnDefinition = "TEXT")
    open var metaText: String? = null,
    /** url под кнопкой **/
    @Column(name = "url", columnDefinition = "TEXT")
    open var metaUrl: String? = null,
): BaseEntity<CallbackDataDTO>() {
    override fun toDTO() = CallbackDataDTO(
        id = id,
        chatId = chatId,
        messageId = messageId,
        callbackData = SmartString(callbackData),
        metaText = metaText,
        metaUrl = metaUrl,
    )
}