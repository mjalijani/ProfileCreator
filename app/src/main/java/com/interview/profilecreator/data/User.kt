package com.interview.profilecreator.data

import com.google.gson.Gson

data class User(
    val name: String,
    val email: String,
    val image: String,
    val pass: String,
    val website: String
)

class UserArgType : JsonNavType<User>() {
    override fun fromJsonParse(value: String): User =
        Gson().fromJson(value, User::class.java)

    override fun User.getJsonParse(): String = Gson().toJson(this)

}