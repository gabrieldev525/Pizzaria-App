package com.unifor.br.pizzadelivery.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.unifor.br.pizzadelivery.R
import com.unifor.br.pizzadelivery.models.Pizza
import com.google.firebase.storage.StorageReference
import com.unifor.br.pizzadelivery.presenter.PizzaPresenter
import com.unifor.br.pizzadelivery.views.PizzaDetailActivity


class PizzaListAdapter(
    private val activity: Activity,
    private val dataSource: ArrayList<Pizza>,
) : BaseAdapter() {
    private lateinit var pizzaPresenter: PizzaPresenter

    private val inflater: LayoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

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

        pizzaPresenter = PizzaPresenter(activity)

        val currPizza = getItem(position)
        val pizzaTitle = rowView.findViewById<TextView>(R.id.txt_pizza_title)
        pizzaTitle.text = currPizza.title

        val btnDetail = rowView.findViewById<Button>(R.id.btn_to_detail)
        btnDetail.setOnClickListener {
            val intent = Intent(activity, PizzaDetailActivity::class.java)
            intent.putExtra("pizza_id", currPizza.id)
            activity.startActivity(intent)
        }

        //  Load image
        val pizzaImage = rowView.findViewById<ImageView>(R.id.img_pizza)
        if(currPizza.photo != null) {
            pizzaPresenter.getPizzaPhoto(currPizza.photo) {
                pizzaImage.setImageBitmap(it)
            }
        }

        return rowView
    }
}