package ru.idfedorov09.telegram.bot.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.cache.CacheProperties.Redis
import org.springframework.stereotype.Service
import redis.clients.jedis.Jedis
import ru.idfedorov09.telegram.bot.data.model.RedisServerData
import ru.idfedorov09.telegram.bot.util.RedisUtil
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class RedisService @Autowired constructor(
    private var jedis: Jedis,
    private val redisServerData: RedisServerData,
) {

    companion object {
        private val log = LoggerFactory.getLogger(this.javaClass)
        private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    }

    /**
     * TODO: пофиксить эти штуки try{}..catch{}
     */
    fun getSafe(key: String): String? {
        try {
            try {
                return jedis.get(key)
            } catch (e: NullPointerException) {
                log.warn("Can't take value with key=$key from redis. Returning null")
                return null
            }
        } catch (e: Exception) {
            Thread.sleep(100)
            reconnect()
            return getSafe(key)
        }
    }

    fun getValue(key: String?): String? {
        try {
            return jedis[key]
        } catch (e: Exception) {
            Thread.sleep(100)
            reconnect()
            return getValue(key)
        }
    }

    fun setValue(key: String, value: String?) {
        try {
            value ?: run {
                jedis.del(key)
                return
            }
            jedis[key] = value
        } catch (e: Exception) {
            Thread.sleep(100)
            reconnect()
            setValue(key, value)
        }
    }

    fun setLastPollDate(date: LocalDateTime) {
        try {
            val dateTimeStr = date.format(formatter)
            jedis["last_poll_date"] = dateTimeStr
        } catch (e: Exception) {
            Thread.sleep(100)
            reconnect()
            setLastPollDate(date)
        }
    }

    fun lpop(key: String): String? {
        try {
            return jedis.lpop(key)
        } catch (e: Exception) {
            Thread.sleep(100)
            reconnect()
            return lpop(key)
        }
    }

    fun rpush(key: String, value: String) {
        try {
            jedis.rpush(key, value)
        } catch (e: Exception) {
            Thread.sleep(100)
            reconnect()
            return rpush(key, value)
        }
    }

    fun keys(key: String): Set<String> {
        try {
            return jedis.keys(key)
        } catch (e: Exception) {
            Thread.sleep(100)
            reconnect()
            return keys(key)
        }
    }

    fun del(key: String) {
        try {
            jedis.del(key)
        } catch (e: Exception) {
            Thread.sleep(100)
            reconnect()
            del(key)
        }
    }

    fun del(keys: Set<String>) {
        try {
            jedis.del(*keys.toTypedArray<String>())
        } catch (e: Exception) {
            Thread.sleep(100)
            reconnect()
            del(keys)
        }
    }

    fun getLastPollDate() = LocalDateTime.parse(getSafe("last_poll_date") ?: "1800-02-22 00:00:00", formatter)

    private fun reconnect() {
        jedis = RedisUtil.getConnection(redisServerData)
    }
}
