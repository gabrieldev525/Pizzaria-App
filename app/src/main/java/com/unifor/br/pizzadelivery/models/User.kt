package com.unifor.br.pizzadelivery.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class User(
    userId: String, fullname: String, cpf: String, date_birth: String,
    cellphone: String, address: UserAddress, orders: Map<String, Order>
) {

}