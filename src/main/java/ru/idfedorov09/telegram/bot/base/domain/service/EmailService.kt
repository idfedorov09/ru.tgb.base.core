package ru.idfedorov09.telegram.bot.base.domain.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val mailSender: JavaMailSender
) {
    @Value("\${spring.mail.default}")
    private lateinit var defaultMail: String

    fun sendEmail(toEmail: String, subject: String, body: String, from: String?) {
        val message = SimpleMailMessage().also {
            it.from = from ?: defaultMail
            it.setTo(toEmail)
            it.setSubject(subject)
            it.setText(body)
        }
        mailSender.send(message)
    }
}