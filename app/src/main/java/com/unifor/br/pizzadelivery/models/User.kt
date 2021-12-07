package com.unifor.br.pizzadelivery.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class User(
    val userId: String, val fullname: String, val cpf: String, val date_birth: String,
    val cellphone: String, val address: UserAddress, val orders: Map<String, Order> = mapOf()
) {
    fun toHashMap(): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        map["user_id"] = userId
        map["fullname"] = fullname
        map["cpf"] = cpf
        map["date_birth"] = date_birth
        map["cellphone"] = cellphone
        map["address"] = address.toHashMap()
        return map
    }
}