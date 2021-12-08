package com.unifor.br.pizzadelivery.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.unifor.br.pizzadelivery.BaseActivity
import com.unifor.br.pizzadelivery.Constants
import com.unifor.br.pizzadelivery.OrderPaymentOptions
import com.unifor.br.pizzadelivery.R
import com.unifor.br.pizzadelivery.presenter.AuthPresenter
import com.unifor.br.pizzadelivery.presenter.OrderPresenter

class PaymentStepOrderActivity : BaseActivity() {
    private var orderPresenter: OrderPresenter = OrderPresenter(this)
    private var authPresenter: AuthPresenter = AuthPresenter(this)

    private lateinit var btnCreditDebitCard: ConstraintLayout
    private lateinit var btnMoney: ConstraintLayout
    private lateinit var btnPix: ConstraintLayout

    private var selectedOption: OrderPaymentOptions = OrderPaymentOptions.CREDIT_DEBIT_CARD

    private var pizza_id: String? = null
    private var added_count: Int = 0
    private var size: String? = null
    private var location: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment_step_finish_order)

        pizza_id = intent.getStringExtra("pizza_id")
        added_count = intent.getIntExtra("added_count", 0)
        size = intent.getStringExtra("size")
        location = intent.getStringExtra("location")
        if(pizza_id == null) {
            finish()
        }

        btnCreditDebitCard = findViewById(R.id.btn_payment_step_option_debit_credit_card)
        btnCreditDebitCard.setOnClickListener {
            onClickOption(OrderPaymentOptions.CREDIT_DEBIT_CARD)
        }
        btnMoney = findViewById(R.id.btn_payment_step_option_money)
        btnMoney.setOnClickListener {
            onClickOption(OrderPaymentOptions.MONEY)
        }
        btnPix = findViewById(R.id.btn_payment_step_option_pix)
        btnPix.setOnClickListener {
            onClickOption(OrderPaymentOptions.PIX)
        }

        updateSelectedOptionHighlight()
    }

    fun onClickSubmit(view: View) {
        val currUser = authPresenter.getCurrentUser()

        if(pizza_id == null) {
            Toast.makeText(this, "Nenhuma pizza selecionada. Selecione o seu pedido", Toast.LENGTH_LONG).show()
            return
        }

        if(authPresenter.isUserLogged()) {
            orderPresenter.registerOrder(
                currUser!!.uid, pizza_id!!,
                size!!, added_count!!,
                Constants.PAYMENT_OPTIONS_MAP[selectedOption] ?: 1) { result, exception ->
                if (result) {
                    val intent = Intent(this, OrderCompleteActivity::class.java)
                    startActivity(intent)
                } else
                    Toast.makeText(this,
                        "Aconteceu um erro ao tentar registrar seu pedido",
                        Toast.LENGTH_LONG).show()
            }
        }
    }

    fun onClickOption(option: OrderPaymentOptions) {
        selectedOption = option
        updateSelectedOptionHighlight()
    }

    fun updateSelectedOptionHighlight() {
        btnCreditDebitCard.setBackgroundResource(R.drawable.bg_order_choice_item)
        btnMoney.setBackgroundResource(R.drawable.bg_order_choice_item)
        btnPix.setBackgroundResource(R.drawable.bg_order_choice_item)

        if(selectedOption == OrderPaymentOptions.CREDIT_DEBIT_CARD)
            btnCreditDebitCard.setBackgroundResource(R.drawable.bg_order_choice_item_selected)
        else if(selectedOption == OrderPaymentOptions.MONEY)
            btnMoney.setBackgroundResource(R.drawable.bg_order_choice_item_selected)
        else if(selectedOption == OrderPaymentOptions.PIX)
            btnPix.setBackgroundResource(R.drawable.bg_order_choice_item_selected)
    }
}