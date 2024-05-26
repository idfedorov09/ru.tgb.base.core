package ru.idfedorov09.telegram.bot.base.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.idfedorov09.telegram.bot.base.domain.data.model.entity.CallbackData

interface CallbackDataRepository<T: CallbackData> : JpaRepository<T, Long>