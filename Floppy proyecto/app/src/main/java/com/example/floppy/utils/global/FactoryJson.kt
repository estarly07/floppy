package com.example.floppy.utils.global

import com.example.floppy.domain.entities.UserEntity
import com.example.floppy.domain.models.Estado
import com.example.floppy.domain.models.Message
import com.example.floppy.domain.models.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList

class FactoryJson {
    val gson = Gson()

    enum class TypeObject {
        USER,
        USERS,
        USERENTITY,
        STATES,
        MESSAGES,
        MESSAGE

    }

    fun Any.toJson(): String =  gson.toJson(this)

    fun String.fromJson(typeObject: TypeObject):Any = when (typeObject) {
        TypeObject.USER -> gson.fromJson(this, User::class.java)
        TypeObject.USERENTITY -> gson.fromJson(this, UserEntity::class.java)
        TypeObject.MESSAGE -> gson.fromJson(this, Message::class.java)

        TypeObject.STATES -> {
            val type = object : TypeToken<ArrayList<ArrayList<Estado>>>() {}.type
            val arrayStates: ArrayList<ArrayList<Estado>> = gson.fromJson(this, type)
            arrayStates
        }
        TypeObject.USERS -> {
            val type = object : TypeToken<ArrayList<User>>() {}.type
            println(this)
            val arrayUser: ArrayList<User> = gson.fromJson(this, type)
            arrayUser
        }
        TypeObject.MESSAGES -> {
            val type = object : TypeToken<ArrayList<Message>>() {}.type
            val messages: ArrayList<Message> = gson.fromJson(this, type)
            messages
        }
    }
}
