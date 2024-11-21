package ru.idfedorov09.telegram.bot.base.fetchers.collector

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update
import ru.idfedorov09.telegram.bot.base.domain.ReplyKeyboardTypes.EMPTY
import ru.idfedorov09.telegram.bot.base.domain.Roles.USER
import ru.idfedorov09.telegram.bot.base.domain.dto.UserDTO
import ru.idfedorov09.telegram.bot.base.domain.service.UserService
import ru.idfedorov09.telegram.bot.base.fetchers.DefaultFetcher
import ru.idfedorov09.telegram.bot.base.util.UpdatesUtil
import ru.mephi.sno.libs.flow.belly.InjectData

@Component
class BaseDataCollector(
    private val updatesUtil: UpdatesUtil,
    private val userService: UserService,
): DefaultFetcher() {

    @InjectData
    fun doFetch(
        update: Update,
    ) {
        val tgUser = updatesUtil.getUser(update) ?: run {
            stopFlowNextExecution()
            return
        }
        val tui = tgUser.id.toString()

        collectUserDTO(tui, tgUser.userName)
        // TODO: other collectors?
    }

    private fun collectUserDTO(
        tui: String,
        username: String,
    ) {
        val userFromDatabase = userService
            .findNotDeletedByTui(tui)
            ?: UserDTO(
                tui = tui,
                lastTgNick = username,
                roles = mutableSetOf(USER),
                currentKeyboardType = EMPTY
            ).let { userService.save(it) }

        addToContext(userFromDatabase)
    }
}