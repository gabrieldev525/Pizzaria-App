package com.unifor.br.pizzadelivery

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.unifor.br.pizzadelivery.presenter.AuthPresenter
import com.unifor.br.pizzadelivery.views.LoginActivity
import com.unifor.br.pizzadelivery.views.PizzaListActivity

open class BaseActivity : AppCompatActivity() {
    private val authPresenter: AuthPresenter = AuthPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!authPresenter.isUserLogged()) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_home -> {
                val intent = Intent(this, PizzaListActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_logout -> {
                authPresenter.logout {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
                return true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

        return true
    }
}