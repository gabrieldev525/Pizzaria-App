package com.unifor.br.pizzadelivery.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.unifor.br.pizzadelivery.R

class OrderCompleteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.completed_order)
    }

    fun onClickOK(view: View) {
        val intent = Intent(this, PizzaListActivity::class.java)
        startActivity(intent)
    }
}