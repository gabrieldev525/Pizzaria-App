package com.unifor.br.pizzadelivery.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.unifor.br.pizzadelivery.R
import com.unifor.br.pizzadelivery.models.Pizza

class PizzaListAdapter(private val context: Context,
                       private val dataSource: ArrayList<Pizza>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Pizza {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.pizza_list_item, parent, false)

        val currPizza = getItem(position)
        // TODO: Customize item list attributes

        return rowView
    }
}