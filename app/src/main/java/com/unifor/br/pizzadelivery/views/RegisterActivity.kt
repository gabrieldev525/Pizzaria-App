package com.unifor.br.pizzadelivery.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.unifor.br.pizzadelivery.R

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        supportActionBar?.hide()
    }
}