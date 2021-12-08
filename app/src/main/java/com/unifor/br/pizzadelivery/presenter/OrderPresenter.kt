package com.unifor.br.pizzadelivery.presenter

import android.app.Activity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.unifor.br.pizzadelivery.Constants
import com.unifor.br.pizzadelivery.models.Order
import com.unifor.br.pizzadelivery.models.PaymentMethod
import com.unifor.br.pizzadelivery.models.UserAddress
import java.util.*

class OrderPresenter(activity: Activity) {
    private var database: DatabaseReference = Firebase.database.reference
    private var pizzaPresenter: PizzaPresenter = PizzaPresenter(activity)

    fun registerOrder(
        userId: String, pizzaId: String, size: String, count: Int,
        street: String, zip_code: String, state: String, city: String, number: String,
        paymentMethod: Int, onFinish: ((result: Boolean, exception: Exception?) -> Unit)) {
        val orderID = UUID.randomUUID().toString()

        pizzaPresenter.getPizza(pizzaId) { pizzaItem ->
            var deliveryAddress = UserAddress(street, zip_code, city, state, number)
            val pizzaPrice = pizzaItem.sizes[size]!!.price ?: 0.0
            val total = pizzaPrice.times(count) ?: 0.0

            val order = Order(
                userId, pizzaId, size, count, total, pizzaPrice, deliveryAddress, Date(), Date(), PaymentMethod(paymentMethod))

            database.child(Constants.ORDERS_FIREBASE_KEY)
                .child(orderID)
                .setValue(order.toHashMap())
                .addOnSuccessListener {
                    database.child(Constants.USERS_FIREBASE_KEY)
                        .child(userId)
                        .child(Constants.ORDERS_FIREBASE_KEY)
                        .child(orderID)
                        .setValue(order.toHashMap())
                        .addOnSuccessListener {
                            onFinish(true, null)
                        }
                        .addOnFailureListener {
                            onFinish(false, it)
                        }
                }
                .addOnFailureListener {
                    onFinish(false, it)
                }
        }
    }
}