package com.unifor.br.pizzadelivery.presenter

import android.app.Activity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.unifor.br.pizzadelivery.Constants
import com.unifor.br.pizzadelivery.models.User
import com.unifor.br.pizzadelivery.models.UserAddress

class AuthPresenter(private val activity: Activity) {
    private var auth: FirebaseAuth = Firebase.auth
    private var database: DatabaseReference = Firebase.database.reference

    fun isUserLogged(): Boolean {
        val currentUser = auth.currentUser
        return currentUser != null
    }

    fun createAccount(
        email: String, password: String,
        fullname: String, cpf: String, date_birth: String,
        cellphone: String, street: String, zip_code: String,
        city: String, state: String, number: String,
        onFinish: (result: Boolean, exception: Exception?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                val currUser = getCurrentUser()
                val address = UserAddress(street, zip_code, city, state, number)
                val userId = currUser!!.uid
                val user = User(userId, fullname, cpf, date_birth, cellphone, address)

                if(task.isSuccessful) {
                    database.child(Constants.USERS_FIREBASE_KEY).child(userId).setValue(user.toHashMap())
                        .addOnCompleteListener() {
                            onFinish(task.isSuccessful, task!!.exception)
                        }
                        .addOnCanceledListener {
                            Toast.makeText(activity, "Aconteceu um erro ao tentar criar o perfil do usuário", Toast.LENGTH_LONG).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(activity, "Aconteceu um erro ao tentar criar o perfil do usuário", Toast.LENGTH_LONG).show()
                        }
                } else {
                    onFinish(task.isSuccessful, task!!.exception)
                }
            }
            .addOnFailureListener {
                Toast.makeText(activity, "Aconteceu um erro ao tentar criar o usuário", Toast.LENGTH_LONG).show()
            }
            .addOnCanceledListener {
                Toast.makeText(activity, "Aconteceu um erro ao tentar criar o usuário", Toast.LENGTH_LONG).show()
            }
    }

    fun login(
        email: String, password: String,
        onFinish: (result: Boolean) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                onFinish(task.isSuccessful)
            }
    }

    fun logout(onFinish: (() -> Unit)) {
        auth.signOut()
        onFinish()
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}