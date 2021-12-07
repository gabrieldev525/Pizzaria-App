package com.unifor.br.pizzadelivery.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class PizzaSize(val size: String?, val price: Double?) {
    val sizeNames = mapOf("1" to "Pequena", "2" to "Média", "3" to "Grande")
    fun getSizeName(): String? {
        return sizeNames[size]
    }
}