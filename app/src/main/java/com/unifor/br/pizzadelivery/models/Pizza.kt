package com.unifor.br.pizzadelivery.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Pizza(
    val title: String, val description: String? = "", val photo: String? = "",
    val sizes: HashMap<String, Any> = hashMapOf()
) {
}