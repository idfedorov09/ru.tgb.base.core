package ru.idfedorov09.telegram.bot.base.domain.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import ru.idfedorov09.kotbot.domain.model.SmartString
import ru.idfedorov09.telegram.bot.base.config.registry.LastUserActionType
import ru.idfedorov09.telegram.bot.base.config.registry.RegistryHolder

@Converter(autoApply = true)
class LastUserActionTypeConverter : AttributeConverter<LastUserActionType, String> {
    override fun convertToDatabaseColumn(attribute: LastUserActionType?): String {
        return attribute?.type?.get() ?: "DEFAULT"
    }

    override fun convertToEntityAttribute(mark: String?): LastUserActionType? {
        return mark?.let { curMark ->
            val originLuat = SmartString<LastUserActionType>(curMark)
            val typeKey = originLuat.getWithoutParams()
            val result = typeKey?.let { RegistryHolder.getRegistry<LastUserActionType>().get(it) }
            result?.type?.addParameters(result, originLuat.getParams())
        }
    }
}
