package com.unifor.br.pizzadelivery.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.unifor.br.pizzadelivery.R
import com.unifor.br.pizzadelivery.presenter.AuthPresenter

class RegisterActivity : AppCompatActivity() {
    private lateinit var fullnameField: EditText
    private lateinit var cpfField: EditText
    private lateinit var dateBirthField: EditText
    private lateinit var cellphoneField: EditText
    private lateinit var streetField: EditText
    private lateinit var zipCodeField: EditText
    private lateinit var cityField: EditText
    private lateinit var stateField: EditText
    private lateinit var numberField: EditText
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText

    private lateinit var authPresenter: AuthPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        supportActionBar?.hide()

        fullnameField = findViewById(R.id.edittext_fullname)
        cpfField = findViewById(R.id.edittext_cpf)
        dateBirthField = findViewById(R.id.edittext_birth_date)
        cellphoneField = findViewById(R.id.edittext_cellphone)
        streetField = findViewById(R.id.edittext_street)
        zipCodeField = findViewById(R.id.edittext_zipcode)
        cityField = findViewById(R.id.edittext_city)
        stateField = findViewById(R.id.edittext_state)
        numberField = findViewById(R.id.edittext_number)
        emailField = findViewById(R.id.edittext_email)
        passwordField = findViewById(R.id.edittext_password)

        authPresenter = AuthPresenter(this)
    }

    fun onClickRegister(view: View) {
        val email = emailField.text.toString()
        val password = passwordField.text.toString()
        val fullname = fullnameField.text.toString()
        val cpf = cpfField.text.toString()
        val dateBirth = dateBirthField.text.toString()
        val cellphone = cellphoneField.text.toString()
        val street = streetField.text.toString()
        val zipCode = zipCodeField.text.toString()
        val city = cityField.text.toString()
        val state = stateField.text.toString()
        val number = numberField.text.toString()

        if(
            email.trim().isEmpty() || password.trim().isEmpty() || fullname.trim().isEmpty() ||
            cpf.trim().isEmpty() || dateBirth.trim().isEmpty() || cellphone.trim().isEmpty() ||
            street.trim().isEmpty() || zipCode.trim().isEmpty() || city.trim().isEmpty() ||
            state.trim().isEmpty() || number.trim().isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_LONG).show()
            return
        }

        authPresenter.createAccount(
            email, password, fullname, cpf, dateBirth, cellphone, street,
            zipCode, city, state, number,
        ) { result, exception ->
            if (result) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    exception!!.message ?: "Não foi possível criar o usuário",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun onClickLogin(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}