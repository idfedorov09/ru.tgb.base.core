package ru.idfedorov09.telegram.bot.base.domain.data.model.entity

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorColumn
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType
import jakarta.persistence.Table
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

@Entity
@Table(name = "callback_data")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "class_type", columnDefinition = "TEXT")
open class CallbackData(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,
    /** id сообщения кнопки **/
    @Column(name = "chat_id")
    open val chatId: String? = null,
    /** чат, в котором эта кнопка находится **/
    @Column(name = "message_id")
    open val messageId: String? = null,
    /** информация, хранящаяся в коллбеке **/
    @Column(name = "data", columnDefinition = "TEXT")
    open val callbackData: String? = null,
    /** Текст на кнопке**/
    @Column(name = "text", columnDefinition = "TEXT")
    open val metaText: String? = null,
    /** url под кнопкой **/
    @Column(name = "url", columnDefinition = "TEXT")
    open val metaUrl: String? = null,
) {
    /**
     * Создает кнопку, которуж можно добавить в клавиатуру и отправить пользователю
     */
    fun createKeyboard() = InlineKeyboardButton().also {
        it.text = metaText!!
        it.callbackData = id!!.toString()
        it.url = metaUrl
    }
}