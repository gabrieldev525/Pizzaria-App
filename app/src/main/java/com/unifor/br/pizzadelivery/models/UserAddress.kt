package com.unifor.br.pizzadelivery.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class UserAddress(
    street: String, zip_code: String, city: String, state: String,
    number: String
) {
}