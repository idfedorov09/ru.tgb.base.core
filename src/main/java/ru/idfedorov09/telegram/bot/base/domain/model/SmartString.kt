package ru.idfedorov09.kotbot.domain.model

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper

/**
 * "Умная" строка - позволяет удобно работать с параметрами.
 */
data class SmartString<T>(
    private var value: String? = null,
) {
    private val mapper = ObjectMapper()

    companion object {
        private const val SEPARATOR = "&"
    }

    private fun rewriteParamPart(newValue: String) {
        value = value?.let {
            val separatorPos = it.indexOf(SEPARATOR)
            if (separatorPos != -1) {
                it.substring(0, separatorPos) + SEPARATOR + newValue
            } else {
                "$it$SEPARATOR$newValue"
            }
        } ?: "$SEPARATOR$newValue"
    }

    fun setParameters(
        origin: T,
        params: Map<String, Any>,
    ): T {
        val newParamPart = mapper.writeValueAsString(params)
        rewriteParamPart(newParamPart)
        return origin
    }

    fun setParameters(
        origin: T,
        vararg params: Pair<String, Any>,
    ) = setParameters(origin, mapOf(*params))

    fun addParameters(
        origin: T,
        params: Map<String, Any>,
    ): T {
        val existingParams: MutableMap<String, Any> = getParams().toMutableMap()
        existingParams.putAll(params)
        return setParameters(origin, existingParams)
    }

    fun addParameters(
        origin: T,
        vararg params: Pair<String, Any>,
    ) = addParameters(origin, mapOf(*params))

    fun getParams(): Map<String, String> {
        val paramsPart = value
            ?.split(SEPARATOR)
            ?.takeIf { it.size > 1 }
            ?.last()
            ?: return emptyMap()

        return paramsPart.jsonToMap()
    }

    private fun String.jsonToMap(): Map<String, String> {
        return mapper.readValue(this, object : TypeReference<Map<String, String>>() {})
    }

    fun get() = value
    operator fun invoke() = get()
}