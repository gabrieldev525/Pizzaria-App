package com.unifor.br.pizzadelivery

class Constants {
    companion object {
        const val USERS_FIREBASE_KEY = "users"
        const val PIZZAS_FIREBASE_KEY = "pizzas"
        const val ORDERS_FIREBASE_KEY = "orders"

        val PAYMENT_OPTIONS_MAP = mapOf(
            OrderPaymentOptions.CREDIT_DEBIT_CARD to 1,
            OrderPaymentOptions.MONEY to 2,
            OrderPaymentOptions.PIX to 3)
    }
}

enum class DeliveryAddressOptions {
    CURRENT_LOCATION, USER_LOCATION
}
enum class OrderPaymentOptions {
    CREDIT_DEBIT_CARD, MONEY, PIX
}