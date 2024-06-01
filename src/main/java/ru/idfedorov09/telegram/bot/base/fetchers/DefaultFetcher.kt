package ru.idfedorov09.telegram.bot.base.fetchers

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update
import ru.idfedorov09.telegram.bot.base.config.FetcherConfigContainer
import ru.idfedorov09.telegram.bot.base.config.registry.RegistryHolder
import ru.idfedorov09.telegram.bot.base.config.registry.UserRole
import ru.idfedorov09.telegram.bot.base.domain.Roles
import ru.idfedorov09.telegram.bot.base.domain.annotation.FetcherPerms
import ru.idfedorov09.telegram.bot.base.domain.dto.UserDTO
import ru.idfedorov09.telegram.bot.base.domain.service.MessageSenderService
import ru.idfedorov09.telegram.bot.base.util.MessageParams
import ru.mephi.sno.libs.flow.belly.FlowContext
import ru.mephi.sno.libs.flow.fetcher.GeneralFetcher
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation

@Component
class DefaultFetcher : GeneralFetcher() {
    private lateinit var flowContext: FlowContext

    @Autowired
    private lateinit var messageSenderService: MessageSenderService

    companion object {
        private val log = LoggerFactory.getLogger(DefaultFetcher::class.java)
    }

    override fun fetchCall(
        flowContext: FlowContext,
        doFetchMethod: KFunction<*>,
        params: MutableList<Any?>,
    ): Any? {
        this.flowContext = flowContext
        val exp = this.flowContext.get<FetcherConfigContainer>() ?: FetcherConfigContainer()
        if (!exp.shouldContinueExecutionFlow) {
            return null
        }
        if (!isValidPerms(flowContext, doFetchMethod)) return null

        return runCatching {
            super.fetchCall(flowContext, doFetchMethod, params)
        }.onFailure { e ->
            log.error("ERROR: $e")
            log.debug(e.stackTraceToString())
            stopFlowNextExecution()
        }.getOrNull()
    }

    private fun isValidPerms(
        flowContext: FlowContext,
        doFetchMethod: KFunction<*>,
    ): Boolean {
        val fetcherPermsAnnotation = doFetchMethod.findAnnotation<FetcherPerms>() ?: return true
        val user = flowContext.get<UserDTO>() ?: return true
        if (Roles.ROOT in user.roles) return true
        val allowPerms = fetcherPermsAnnotation.roles.map { RegistryHolder.getRegistry<UserRole>().get(it) }
        return allowPerms.all { it in user.roles }
    }

    /**
     * Прерывает дальнейшее выполнение графа в рамках сессии (прогонки графа)
     */
    @Synchronized
    fun stopFlowNextExecution() {
        val exp = flowContext.get<FetcherConfigContainer>() ?: FetcherConfigContainer()
        exp.apply {
            shouldContinueExecutionFlow = false
            flowContext.insertObject(this)
        }
    }

    /**
     * Метод который удаляет сообщение из только что пришедшего обновления
     * Вызывает исключение RuntimeException если в контексте нет Update или обновление не содержит сообщение
     */
    fun deleteUpdateMessage() {
        val update =
            flowContext.get<Update>()
                ?: throw RuntimeException("Can't delete the message: there's no update in the context.")

        if (!update.hasMessage()) {
            throw RuntimeException("Can't delete the message: there's no message in the update.")
        }

        messageSenderService.deleteMessage(
            MessageParams(
                chatId = update.message.chatId.toString(),
                messageId = update.message.messageId,
            ),
        )
    }
}