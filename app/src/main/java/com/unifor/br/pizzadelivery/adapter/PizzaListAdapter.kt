package com.unifor.br.pizzadelivery.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.unifor.br.pizzadelivery.R
import com.unifor.br.pizzadelivery.models.Pizza
import com.google.firebase.storage.StorageReference




class PizzaListAdapter(
    private val context: Context,
    private val dataSource: ArrayList<Pizza>,
) : BaseAdapter() {

    val storage = Firebase.storage
    val storageRef = storage.reference

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
        val pizzaTitle = rowView.findViewById<TextView>(R.id.txt_pizza_title)
        val pizzaImage = rowView.findViewById<ImageView>(R.id.img_pizza)
        pizzaTitle.text = currPizza.title

        val MAX_IMAGE_SIZE = ((1024 * 1024) * 100).toLong();

        val photoReference: StorageReference = storageRef.child(currPizza.photo.toString())
        photoReference.getBytes(MAX_IMAGE_SIZE).addOnSuccessListener {
            val bmp = BitmapFactory.decodeByteArray(it as ByteArray, 0, it.size)
            pizzaImage.setImageBitmap(bmp)
        }

        return rowView
    }
}