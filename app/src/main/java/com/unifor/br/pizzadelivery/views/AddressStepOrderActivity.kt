package com.unifor.br.pizzadelivery.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.unifor.br.pizzadelivery.BaseActivity
import com.unifor.br.pizzadelivery.DeliveryAddressOptions
import com.unifor.br.pizzadelivery.R
import com.unifor.br.pizzadelivery.models.UserAddress
import com.unifor.br.pizzadelivery.presenter.UserPresenter
import java.util.*

class AddressStepOrderActivity : BaseActivity() {
    private lateinit var userPresenter: UserPresenter

    private lateinit var txtStreet: TextView
    private lateinit var txtCityState: TextView
    private lateinit var txtZipCode: TextView

    private lateinit var txtStreetCurrLocation: TextView
    private lateinit var txtCityStateCurrLocation: TextView
    private lateinit var txtZipCodeCurrLocation: TextView

    private lateinit var btnCurrLocation: ConstraintLayout
    private lateinit var btnUserLocation: ConstraintLayout

    private var selectedOption: DeliveryAddressOptions = DeliveryAddressOptions.USER_LOCATION

    private var pizza_id: String? = null
    private var added_count: Int = 0
    private var size: String? = null

    val PERMISSION_ID = 42
    private var longitude: Double? = null
    private var latitude: Double? = null
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private lateinit var userAddress: UserAddress

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

        txtStreetCurrLocation = findViewById(R.id.txt_address_step_street_curr_location)
        txtCityStateCurrLocation = findViewById(R.id.txt_address_step_city_state_curr_location)
        txtZipCodeCurrLocation = findViewById(R.id.txt_address_step_zip_code_curr_location)

        btnCurrLocation = findViewById(R.id.btn_address_step_option_current_location)
        btnUserLocation = findViewById(R.id.btn_address_step_option_user_location)

        userPresenter = UserPresenter(this)
        userPresenter.getCurrentUserProfile {
            userAddress = it.address

            txtStreet.text = userAddress.street
            txtCityState.text = "${userAddress.city}, ${userAddress.state}"
            txtZipCode.text = userAddress.zip_code

            btnCurrLocation.setOnClickListener {
                onClickOption(DeliveryAddressOptions.CURRENT_LOCATION)
            }
            btnUserLocation.setOnClickListener {
                onClickOption(DeliveryAddressOptions.USER_LOCATION)
            }
        }

        updateSelectedOptionHighlight()

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
    }

    fun onClickSubmit(view: View) {
        val intent = Intent(this, PaymentStepOrderActivity::class.java)
        intent.putExtra("pizza_id", pizza_id)
        intent.putExtra("added_count", added_count)
        intent.putExtra("size", size)
        intent.putExtra("location", selectedOption)

        if(selectedOption == DeliveryAddressOptions.CURRENT_LOCATION) {
            val address = getAddressFromLocation(latitude!!, longitude!!)
            intent.putExtra("street", address!!.getAddressLine(0))
            intent.putExtra("zip_code", address!!.postalCode)
            intent.putExtra("state", address!!.adminArea)
            intent.putExtra("city", address!!.locality)
            intent.putExtra("number", "")
        } else if(selectedOption == DeliveryAddressOptions.USER_LOCATION) {
            intent.putExtra("street", userAddress.street)
            intent.putExtra("zip_code", userAddress.zip_code)
            intent.putExtra("state", userAddress.state)
            intent.putExtra("city", userAddress.city)
            intent.putExtra("number", userAddress.number)
        }
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

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            latitude = mLastLocation.latitude
            longitude = mLastLocation.longitude
            updateCurrLocationText()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                try {
                    mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                        var location: Location? = task.result
                        if (location == null) {
                            requestNewLocationData()
                        } else {
                            latitude = location.latitude
                            longitude = location.longitude
                            updateCurrLocationText()
                        }
                    }
                } catch(e: Exception) {
                    requestNewLocationData()
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun getAddressFromLocation(latitude: Double, longitude: Double): Address? {
        val gcd = Geocoder(this, Locale.getDefault())
        val addresses = gcd.getFromLocation(latitude, longitude, 1)
        if(addresses.size > 0)
            return addresses[0]
        return null
    }

    private fun updateCurrLocationText() {
        if(latitude != null && longitude != null) {
            val address = getAddressFromLocation(latitude!!, longitude!!)
            txtStreetCurrLocation.text = address!!.getAddressLine(0)
            txtZipCodeCurrLocation.text = address!!.postalCode
            txtCityStateCurrLocation.text = "${address.locality}, ${address.adminArea}"
        }
    }
}