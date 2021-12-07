package com.unifor.br.pizzadelivery.presenter

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.unifor.br.pizzadelivery.Constants
import com.unifor.br.pizzadelivery.models.Pizza
import com.unifor.br.pizzadelivery.models.PizzaSize

class PizzaPresenter(val activity: Activity) {
    private var database: DatabaseReference = Firebase.database.reference
    private val storage = Firebase.storage
    private val storageRef = storage.reference

    fun getPizzaList(
        onFinish: (result: ArrayList<Pizza>) -> Unit) {
        database.child(Constants.PIZZAS_FIREBASE_KEY).get().addOnSuccessListener {
            var list = ArrayList<Pizza>()

            it.children.forEach { pizzaItem ->
                val sizes = HashMap<String, PizzaSize>()

                pizzaItem.child("sizes").children.forEach { size ->
                    val sizeValue = size.child("size").value.toString()
                    val pizzaSize = PizzaSize(
                        size.child("size").value.toString(),
                        (size.child("price").value as Long).toDouble())
                    sizes[size.key.toString()] = pizzaSize
                }

                val pizza = Pizza(
                    pizzaItem.key.toString(),
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

    fun getPizza(
        pizza_id: String, onFinish: (result: Pizza) -> Unit) {
        database.child(Constants.PIZZAS_FIREBASE_KEY).child(pizza_id).get().addOnSuccessListener {
            pizzaItem ->
            val sizes = HashMap<String, PizzaSize>()

            pizzaItem.child("sizes").children.forEach { size ->
                val sizeValue = size.child("size").value.toString()
                val pizzaSize = PizzaSize(
                    sizeValue,
                    (size.child("price").value as Long).toDouble())
                sizes[sizeValue] = pizzaSize
            }

            val pizza = Pizza(
                pizzaItem.key.toString(),
                pizzaItem.child("title").value.toString(), pizzaItem.child("description").value.toString(),
                pizzaItem.child("photo").value.toString(), sizes)

            onFinish(pizza)
        }
    }

    fun getPizzaPhoto(uri: String, onFinish: (bmp: Bitmap) -> Unit) {
        val MAX_IMAGE_SIZE = ((1024 * 1024) * 100).toLong();
        val photoReference: StorageReference = storageRef.child(uri)

        photoReference.getBytes(MAX_IMAGE_SIZE).addOnSuccessListener {
            val bmp = BitmapFactory.decodeByteArray(it as ByteArray, 0, it.size)
            onFinish(bmp)
        }
    }
}