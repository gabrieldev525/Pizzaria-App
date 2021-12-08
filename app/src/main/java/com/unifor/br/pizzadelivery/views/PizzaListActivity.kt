package com.unifor.br.pizzadelivery.views

import android.os.Bundle
import android.widget.ListView
import com.unifor.br.pizzadelivery.BaseActivity
import com.unifor.br.pizzadelivery.R
import com.unifor.br.pizzadelivery.adapter.PizzaListAdapter
import com.unifor.br.pizzadelivery.models.Pizza
import com.unifor.br.pizzadelivery.presenter.PizzaPresenter

class PizzaListActivity : BaseActivity() {
    private lateinit var listView: ListView
    private lateinit var pizzaPresenter: PizzaPresenter
    private var pizzaList: ArrayList<Pizza> = ArrayList<Pizza>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pizza_list)

        listView = findViewById<ListView>(R.id.pizza_list_view)

        pizzaPresenter = PizzaPresenter(this)
        pizzaPresenter.getPizzaList() { result ->
            val adapter = PizzaListAdapter(this, result)
            listView.adapter = adapter
        }
    }
}