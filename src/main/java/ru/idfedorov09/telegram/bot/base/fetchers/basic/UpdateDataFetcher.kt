package ru.idfedorov09.telegram.bot.base.fetchers.basic

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.meta.api.objects.Update
import ru.idfedorov09.telegram.bot.base.domain.dto.UserDTO
import ru.idfedorov09.telegram.bot.base.domain.service.UserService
import ru.idfedorov09.telegram.bot.base.executor.Executor
import ru.idfedorov09.telegram.bot.base.fetchers.DefaultFetcher
import ru.mephi.sno.libs.flow.belly.InjectData

@Component
open class UpdateDataFetcher(
    private val userService: UserService,
    private val bot: Executor,
): DefaultFetcher() {

    @InjectData
    open fun doFetch(
        user: UserDTO?,
        update: Update,
    ) {
        if (update.callbackQuery != null) {
            bot.execute(AnswerCallbackQuery(update.callbackQuery.id))
        }
        if (user != null) updateUser(user)
    }

    open fun updateUser(user: UserDTO) {
        userService.save(user)
    }
}