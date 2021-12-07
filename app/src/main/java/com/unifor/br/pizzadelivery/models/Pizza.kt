package com.unifor.br.pizzadelivery.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Pizza(
    title: String, description: String? = "", photo: String? = "",
    sizes: Map<String, PizzaSize> = mapOf()
) {
}