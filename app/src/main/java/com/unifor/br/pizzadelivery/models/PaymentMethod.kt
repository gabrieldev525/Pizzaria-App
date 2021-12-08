package com.unifor.br.pizzadelivery.models

import com.google.firebase.database.IgnoreExtraProperties
import java.util.HashMap

@IgnoreExtraProperties
class PaymentMethod(val type: Int) {
    fun toHashMap(): HashMap<String, Int> {
        val map = HashMap<String, Int>()
        map["type"] = type
        return map
    }
}