package com.unifor.br.pizzadelivery.presenter

import android.app.Activity
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.unifor.br.pizzadelivery.Constants
import com.unifor.br.pizzadelivery.models.User
import com.unifor.br.pizzadelivery.models.UserAddress

class UserPresenter(val activity: Activity) {
    private val authPresenter: AuthPresenter = AuthPresenter(activity)
    private var database: DatabaseReference = Firebase.database.reference

    fun getCurrentUserProfile(onFinish: ((result: User) -> Unit)) {
        val loggedUser = authPresenter.getCurrentUser()

        database.child(Constants.USERS_FIREBASE_KEY).child(loggedUser!!.uid).get()
            .addOnSuccessListener {
                val addressChild = it.child("address")
                val address = UserAddress(
                    addressChild.child("street").value.toString(), addressChild.child("zip_code").value.toString(),
                    addressChild.child("city").value.toString(), addressChild.child("state").value.toString(),
                    addressChild.child("number").value.toString()
                )

                val user = User(
                    it.child("user_id").value.toString(), it.child("fullname").value.toString(),
                    it.child("cpf").value.toString(), it.child("date_birth").value.toString(),
                    it.child("cellphone").value.toString(),
                    address)

                onFinish(user)
            }
            .addOnFailureListener {
                Toast.makeText(activity, "Aconteceu um erro ao tentar buscar informações do usuário", Toast.LENGTH_LONG).show()
            }
    }
}