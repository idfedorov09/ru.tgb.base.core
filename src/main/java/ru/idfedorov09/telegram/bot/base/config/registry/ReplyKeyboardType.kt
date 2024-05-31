package ru.idfedorov09.telegram.bot.base.config.registry

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow

data class ReplyKeyboardType(
    val type: String,
    /** Как клавиатура рисуется **/
    var builder: IReplyButtonBuilder,
): RegistryModel(ReplyKeyboardType::class, type) {

    fun build(roles: Set<UserRole>): List<KeyboardRow> {
        val keyboard = builder.build(roles)
        return keyboard.map { line ->
            KeyboardRow().also { row ->
                line.map { row.add(it.command) }
            }
        }
    }
}