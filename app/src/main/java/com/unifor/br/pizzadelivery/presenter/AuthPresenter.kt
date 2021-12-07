package com.unifor.br.pizzadelivery.presenter

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.unifor.br.pizzadelivery.models.User

class AuthPresenter(private val activity: Activity) {
    private lateinit var auth: FirebaseAuth

    init {
        auth = Firebase.auth
    }

    fun isUserLogged(): Boolean {
        val currentUser = auth.currentUser
        return currentUser != null
    }

    fun createAccount(
        email: String, password: String,
        onFinish: (result: Boolean, firebaseUser: FirebaseUser?) -> Void) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                onFinish(task.isSuccessful, auth.currentUser)
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

    fun getCurrentUser(): User {
        val currUser = auth.currentUser
        return User(currUser?.email, null)
    }
}