package com.unifor.br.pizzadelivery.views

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.unifor.br.pizzadelivery.R
import com.unifor.br.pizzadelivery.adapter.PizzaListAdapter
import com.unifor.br.pizzadelivery.models.Pizza

class PizzaListActivity : AppCompatActivity() {
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pizza_list)

        listView = findViewById<ListView>(R.id.pizza_list_view)
        // listView.divider = null

        val list = ArrayList<Pizza>()
        for(i in 1..10) {
            list.add(Pizza())
        }

        val adapter = PizzaListAdapter(this, list)
        listView.adapter = adapter
    }
}