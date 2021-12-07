package com.unifor.br.pizzadelivery.models

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
class Order(
    userId: String, pizzaId: String, size: String, count: Int,
    total: Int, pizzaPrice: Double, deliveryAddress: UserAddress,
    orderDate: Date, deliveryDate: Date, paymentMethod: PaymentMethod
) {
}