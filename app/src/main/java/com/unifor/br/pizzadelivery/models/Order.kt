package com.unifor.br.pizzadelivery.models

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
class Order(
    val userId: String, val pizzaId: String, val size: String, val count: Int,
    val total: Double, val pizzaPrice: Double, val deliveryAddress: UserAddress,
    val orderDate: Date, val deliveryDate: Date, val paymentMethod: PaymentMethod
) {
    fun toHashMap(): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        map["user_id"] = userId
        map["pizza_id"] = pizzaId
        map["size"] = size
        map["count"] = count
        map["total"] = total
        map["pizza_price"] = pizzaPrice
        map["delivery_address"] = deliveryAddress.toHashMap()
        map["order_date"] = orderDate
        map["delivery_date"] = deliveryDate
        map["payment_method"] = paymentMethod.toHashMap()
        return map
    }
}