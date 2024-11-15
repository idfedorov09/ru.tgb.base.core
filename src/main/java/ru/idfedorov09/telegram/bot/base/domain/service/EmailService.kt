package ru.idfedorov09.telegram.bot.base.domain.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import ru.idfedorov09.telegram.bot.base.util.ResourcesUtil
import java.util.*

@Service
class EmailService(
    private val mailSender: JavaMailSender
) {
    @Value("\${spring.mail.default}")
    private lateinit var defaultMail: String

    companion object {
        fun buildBody(templateName: String, parameters: Map<String, String>): String {
            val templateContent = ResourcesUtil.getContent("mail/$templateName.html")
            val defaultParameters = mapOf(
                "year" to Calendar.getInstance().get(Calendar.YEAR).toString(),
            )
            val prepared = applyBodyParams(templateContent, defaultParameters)
            return applyBodyParams(prepared, parameters)
        }

        private fun applyBodyParams(templateContent: String, parameters: Map<String, String>): String {
            var result = templateContent
            parameters.forEach { (key, value) ->
                result = result.replace("%$key%", value)
            }
            return result
        }
    }

    fun sendEmail(toEmail: String, subject: String, body: String, from: String? = null) {
        CoroutineScope(Dispatchers.IO).launch {
            val message = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, true, "UTF-8").apply {  }
            helper.setFrom(from ?: defaultMail)
            helper.setTo(toEmail)
            helper.setSubject(subject)
            helper.setText(body, true)
            mailSender.send(message)
        }
    }

}