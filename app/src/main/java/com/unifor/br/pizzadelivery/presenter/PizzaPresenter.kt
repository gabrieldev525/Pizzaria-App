package com.unifor.br.pizzadelivery.presenter

import android.app.Activity
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.unifor.br.pizzadelivery.Constants
import com.unifor.br.pizzadelivery.models.Pizza
import com.unifor.br.pizzadelivery.models.PizzaSize

class PizzaPresenter(val activity: Activity) {
    private var database: DatabaseReference = Firebase.database.reference

    fun getPizzaList(
        onFinish: (result: ArrayList<Pizza>) -> Unit) {
        database.child(Constants.PIZZAS_FIREBASE_KEY).get().addOnSuccessListener {
            var list = ArrayList<Pizza>()

            it.children.forEach { pizzaItem ->
                val sizes = HashMap<String, Any>()

                pizzaItem.child("sizes").children.forEach { size ->
                    val pizzaSize = PizzaSize(size.key, (size.child("price").value as Long).toDouble())
                    sizes[size.key.toString()] = pizzaSize
                }

                val pizza = Pizza(
                    pizzaItem.child("title").value.toString(), pizzaItem.child("description").value.toString(),
                    pizzaItem.child("photo").value.toString(), sizes)
                list.add(pizza)
            }

            onFinish(list)
        }
        .addOnFailureListener {
            Toast.makeText(activity, "Aconteceu um erro ao tentar carregar a lista de pizzas", Toast.LENGTH_LONG).show()
        }
    }
}