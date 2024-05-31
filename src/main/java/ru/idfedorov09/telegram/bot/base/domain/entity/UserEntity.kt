package ru.idfedorov09.telegram.bot.base.domain.entity

import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.DiscriminatorColumn
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType
import jakarta.persistence.Table
import ru.idfedorov09.telegram.bot.base.config.registry.LastUserActionType
import ru.idfedorov09.telegram.bot.base.config.registry.ReplyKeyboardType
import ru.idfedorov09.telegram.bot.base.config.registry.UserRole
import ru.idfedorov09.telegram.bot.base.domain.converter.LastUserActionTypeConverter
import ru.idfedorov09.telegram.bot.base.domain.converter.UserKeyboardTypeConverter
import ru.idfedorov09.telegram.bot.base.domain.converter.UserRoleConverter
import ru.idfedorov09.telegram.bot.base.domain.dto.UserDTO

@Entity
@Table(name = "users_table")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "class_type", columnDefinition = "TEXT")
open class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    open val id: Long? = null,

    /** id юзера в телеграме **/
    @Column(name = "tui")
    open val tui: String? = null,

    /** последний сохраненный ник в телеге **/
    @Column(name = "last_tg_nick")
    open val lastTgNick: String? = null,

    /** роли пользователя **/
    @ElementCollection
    @CollectionTable(name="user_roles")
    @Convert(converter = UserRoleConverter::class)
    open val roles: Set<UserRole> = mutableSetOf(),

    /** тип предыдущего действия пользователя **/
    @Column(name = "last_action_type", columnDefinition = "TEXT")
    @Convert(converter = LastUserActionTypeConverter::class)
    val lastUserActionType: LastUserActionType? = null,

    /** поле для временных данных юзера **/
    @Column(name = "user_data", columnDefinition = "TEXT")
    val data: String? = null,

    /** метка soft-delete **/
    @Column(name = "is_deleted")
    val isDeleted: Boolean = false,

    /** тип текущей реплай клавиатуры **/
    @Column(name = "current_keyboard_type", columnDefinition = "TEXT", updatable = false)
    @Convert(converter = UserKeyboardTypeConverter::class)
    val currentKeyboardType: ReplyKeyboardType? = null,

    /** Было ли выполнено переключение клавиатуры на новую **/
    @Column(name = "is_keyboard_switched", updatable = false)
    val isKeyboardSwitched: Boolean = false,

): BaseEntity<UserDTO, UserEntity>()
