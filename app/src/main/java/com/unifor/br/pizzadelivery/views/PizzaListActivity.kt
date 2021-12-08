package com.unifor.br.pizzadelivery.views

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.unifor.br.pizzadelivery.BaseActivity
import com.unifor.br.pizzadelivery.R
import com.unifor.br.pizzadelivery.adapter.PizzaListAdapter
import com.unifor.br.pizzadelivery.models.Pizza
import com.unifor.br.pizzadelivery.presenter.PizzaPresenter
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.coroutines.CoroutineContext

class PizzaListActivity : BaseActivity(), CoroutineScope {
    private lateinit var listView: ListView
    private lateinit var txtHour: TextView
    private lateinit var pizzaPresenter: PizzaPresenter
    private var pizzaList: ArrayList<Pizza> = ArrayList<Pizza>()
    private val client = OkHttpClient()

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    lateinit var mainHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pizza_list)

        listView = findViewById(R.id.pizza_list_view)
        txtHour = findViewById(R.id.txt_list_pizza_curr_hour)

        pizzaPresenter = PizzaPresenter(this)
        pizzaPresenter.getPizzaList() { result ->
            val adapter = PizzaListAdapter(this, result)
            listView.adapter = adapter
        }

        mainHandler = Handler(Looper.getMainLooper())

        launch {
            while(isActive) {
                fetchHours()
                delay(60000)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    fun onResult(result: String) {
        val hour = result.split("T")[1].split(".")[0].split(":")
        val finalHour = "${hour[0]}:${hour[1]}"
        txtHour.text = finalHour
    }

    fun makeToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    suspend fun fetchHours() {
        withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url("https://worldtimeapi.org/api/timezone/America/Fortaleza")
                .build()

            try {
                var response = client.newCall(request).execute()
                withContext(Dispatchers.Main) {
                    val body = response.body!!.string()
                    val json = JSONObject(body)
                    onResult(json.getString("datetime"))
                }
            } catch(e: Exception) {
                e.printStackTrace()

                withContext(Dispatchers.Main) {
                    makeToast("Aconteceu um erro ao tentar carregar os dados")
                }
            }
        }
    }

}