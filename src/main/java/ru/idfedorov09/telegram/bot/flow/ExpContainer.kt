package ru.idfedorov09.telegram.bot.flow

import ru.mephi.sno.libs.flow.belly.Mutable

/**
 * Объект контекста флоу, содержащий информацию о работающих фичах, режимах и тд и тп
 */
@Mutable
data class ExpContainer(
    val test: String = "test"
)
