package ru.idfedorov09.telegram.bot.base.domain.data.enum

enum class TextCommands(
    /** текст команды **/
    val commandText: String,
) {
    CHANGE_FLOW_COMMAND("/flow")
    ;

    /** Проверяет, является ли текст командой **/
    companion object {
        fun isTextCommand(text: String?) = entries.map { it.commandText }.any { text?.startsWith(it) ?: false }
    }

    operator fun invoke() = commandText
}
