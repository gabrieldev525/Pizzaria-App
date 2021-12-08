package com.unifor.br.pizzadelivery.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.unifor.br.pizzadelivery.DeliveryAddressOptions
import com.unifor.br.pizzadelivery.R
import com.unifor.br.pizzadelivery.presenter.UserPresenter

class AddressStepOrderActivity : AppCompatActivity() {
    private lateinit var userPresenter: UserPresenter

    private lateinit var txtStreet: TextView
    private lateinit var txtCityState: TextView
    private lateinit var txtZipCode: TextView
    private lateinit var btnCurrLocation: ConstraintLayout
    private lateinit var btnUserLocation: ConstraintLayout

    private var selectedOption: DeliveryAddressOptions = DeliveryAddressOptions.USER_LOCATION

    private var pizza_id: String? = null
    private var added_count: Int = 0
    private var size: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.address_step_finish_order)

        pizza_id = intent.getStringExtra("pizza_id")
        added_count = intent.getIntExtra("added_count", 0)
        size = intent.getStringExtra("size")

        if(pizza_id == null) {
            finish()
        }

        txtStreet = findViewById(R.id.txt_address_step_street)
        txtCityState = findViewById(R.id.txt_address_step_city_state)
        txtZipCode = findViewById(R.id.txt_address_step_zip_code)
        btnCurrLocation = findViewById(R.id.btn_address_step_option_current_location)
        btnUserLocation = findViewById(R.id.btn_address_step_option_user_location)

        userPresenter = UserPresenter(this)
        userPresenter.getCurrentUserProfile {
            val address = it.address

            txtStreet.text = address.street
            txtCityState.text = "${address.city}, ${address.state}"
            txtZipCode.text = address.zip_code

            btnCurrLocation.setOnClickListener {
                onClickOption(DeliveryAddressOptions.CURRENT_LOCATION)
            }
            btnUserLocation.setOnClickListener {
                onClickOption(DeliveryAddressOptions.USER_LOCATION)
            }
        }

        updateSelectedOptionHighlight()
    }

    fun onClickSubmit(view: View) {
        val intent = Intent(this, PaymentStepOrderActivity::class.java)
        intent.putExtra("pizza_id", pizza_id)
        intent.putExtra("added_count", added_count)
        intent.putExtra("size", size)
        intent.putExtra("location", selectedOption)
        startActivity(intent)
    }

    fun onClickOption(option: DeliveryAddressOptions) {
        selectedOption = option
        updateSelectedOptionHighlight()
    }

    fun updateSelectedOptionHighlight() {
        if(selectedOption == DeliveryAddressOptions.CURRENT_LOCATION) {
            btnCurrLocation.setBackgroundResource(R.drawable.bg_order_choice_item_selected)
            btnUserLocation.setBackgroundResource(R.drawable.bg_order_choice_item)
        } else if(selectedOption == DeliveryAddressOptions.USER_LOCATION) {
            btnUserLocation.setBackgroundResource(R.drawable.bg_order_choice_item_selected)
            btnCurrLocation.setBackgroundResource(R.drawable.bg_order_choice_item)
        }
    }
}