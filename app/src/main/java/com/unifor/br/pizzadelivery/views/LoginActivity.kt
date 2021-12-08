package com.unifor.br.pizzadelivery.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.unifor.br.pizzadelivery.R
import com.unifor.br.pizzadelivery.presenter.AuthPresenter

class LoginActivity : AppCompatActivity() {
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var authPresenter: AuthPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        supportActionBar?.hide()
        authPresenter = AuthPresenter(this)

        if(authPresenter.isUserLogged()) {
            val intent = Intent(this, PizzaListActivity::class.java)
            startActivity(intent)
        }

        emailField = findViewById(R.id.edittext_email)
        passwordField = findViewById(R.id.edittext_password)
    }

    fun onClickRegister(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    fun onClickLogin(view: View) {
        if(emailField.text.trim().isEmpty() || passwordField.text.trim().isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_LONG).show()
            return
        }

        authPresenter.login(emailField.text.toString(), passwordField.text.toString()) { result ->
            if(result) {
                val intent = Intent(this, PizzaListActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Usuário ou senha inválidos", Toast.LENGTH_LONG).show()
            }
        }
    }
}