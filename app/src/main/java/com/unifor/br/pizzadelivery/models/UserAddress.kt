package com.unifor.br.pizzadelivery.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class UserAddress(
    val street: String, val zip_code: String, val city: String, val state: String,
    val number: String
) {
    fun toHashMap(): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        map["street"] = street
        map["zip_code"] = zip_code
        map["city"] = city
        map["state"] = state
        map["number"] = number
        return map
    }
}