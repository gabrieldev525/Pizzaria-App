package com.unifor.br.pizzadelivery.views

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.unifor.br.pizzadelivery.BaseActivity
import com.unifor.br.pizzadelivery.R
import com.unifor.br.pizzadelivery.models.Pizza
import com.unifor.br.pizzadelivery.models.PizzaSize
import com.unifor.br.pizzadelivery.presenter.PizzaPresenter

class PizzaDetailActivity : BaseActivity() {
    private lateinit var pizzaPresenter: PizzaPresenter

    private lateinit var imgPizza: ImageView
    private lateinit var txtPizzaTitle: TextView
    private lateinit var txtPizzaDescription: TextView
    private lateinit var txtPizzaSize1: TextView
    private lateinit var txtPizzaSize2: TextView
    private lateinit var txtPizzaSize3: TextView
    private lateinit var btnPizzaSize1: ConstraintLayout
    private lateinit var btnPizzaSize2: ConstraintLayout
    private lateinit var btnPizzaSize3: ConstraintLayout
    private lateinit var txtPrice: TextView
    private lateinit var txtCountToAdd: TextView
    private lateinit var btnLessCount: ConstraintLayout
    private lateinit var btnPlusCount: ConstraintLayout

    private var selectedSize: PizzaSize? = null
    private var currPizza: Pizza? = null
    private var addedCount: Int = 1
    private var total: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pizza_detail)

        imgPizza = findViewById(R.id.img_pizza_detail)
        txtPizzaTitle = findViewById(R.id.txt_pizza_title)
        txtPizzaDescription = findViewById(R.id.txt_pizza_description)
        txtPizzaSize1 = findViewById(R.id.txt_pizza_size_1)
        txtPizzaSize2 = findViewById(R.id.txt_pizza_size_2)
        txtPizzaSize3 = findViewById(R.id.txt_pizza_size_3)

        // Size buttons
        btnPizzaSize1 = findViewById(R.id.btn_pizza_size_1)
        btnPizzaSize1.setOnClickListener {
            onClickSizeButton(currPizza!!.sizes["1"])
        }
        btnPizzaSize2 = findViewById(R.id.btn_pizza_size_2)
        btnPizzaSize2.setOnClickListener {
            onClickSizeButton(currPizza!!.sizes["2"])
        }
        btnPizzaSize3 = findViewById(R.id.btn_pizza_size_3)
        btnPizzaSize3.setOnClickListener {
            onClickSizeButton(currPizza!!.sizes["3"])
        }

        // Pizza counts widgets
        txtPrice = findViewById(R.id.txt_price)
        txtCountToAdd = findViewById(R.id.txt_count_to_add)
        btnLessCount = findViewById(R.id.btn_less)
        btnLessCount.setOnClickListener {
            onClickLessButton()
        }
        btnPlusCount = findViewById(R.id.btn_plus)
        btnPlusCount.setOnClickListener {
            onClickPlusButton()
        }

        val pizza_id = intent.getStringExtra("pizza_id")
        pizzaPresenter = PizzaPresenter(this)

        if (pizza_id != null) {
            pizzaPresenter.getPizza(pizza_id) {
                currPizza = it

                txtPizzaTitle.text = it.title
                txtPizzaDescription.text = it.description

                txtPizzaSize1.text = it.sizes["1"]!!.getSizeName()
                txtPizzaSize2.text = it.sizes["2"]!!.getSizeName()
                txtPizzaSize3.text = it.sizes["3"]!!.getSizeName()

                selectedSize = currPizza!!.sizes["3"]
                updateTotal()
                updateCount()
                updateSizeHighlight()

                if(it.photo != null) {
                    pizzaPresenter.getPizzaPhoto(it.photo) {
                        bmp -> imgPizza.setImageBitmap(bmp)
                    }
                }
            }
        } else {
            finish()
        }
    }

    fun onClickSubmitButton(view: View) {
        val intent = Intent(this, AddressStepOrderActivity::class.java)
        intent.putExtra("pizza_id", currPizza!!.id)
        intent.putExtra("added_count", addedCount)
        intent.putExtra("size", selectedSize!!.size)
        startActivity(intent)
    }

    fun onClickSizeButton(size: PizzaSize?) {
        selectedSize = size
        updateTotal()

        updateSizeHighlight()
    }

    fun updateSizeHighlight() {
        val colorGray = ContextCompat.getColor(this, R.color.gray_color_2)
        val colorYellow = ContextCompat.getColor(this, R.color.yellow)

        btnPizzaSize1.backgroundTintList = ColorStateList.valueOf(colorGray)
        btnPizzaSize2.backgroundTintList = ColorStateList.valueOf(colorGray)
        btnPizzaSize3.backgroundTintList = ColorStateList.valueOf(colorGray)

        if(selectedSize!!.size.equals("1"))
            btnPizzaSize1.backgroundTintList = ColorStateList.valueOf(colorYellow)
        else if(selectedSize!!.size.equals("2"))
            btnPizzaSize2.backgroundTintList = ColorStateList.valueOf(colorYellow)
        else if(selectedSize!!.size.equals("3"))
            btnPizzaSize3.backgroundTintList = ColorStateList.valueOf(colorYellow)
    }

    fun onClickLessButton() {
        if(addedCount > 1)
            addedCount--
        updateCount()
        updateTotal()
    }

    fun onClickPlusButton() {
        addedCount++
        updateCount()
        updateTotal()
    }

    fun updateCount() {
        txtCountToAdd.text = addedCount.toString()
    }

    fun updateTotal() {
        total = selectedSize!!.price?.times(addedCount) ?: 0.0
        txtPrice.text = "R$${String.format("%.2f", total)}"
    }

}