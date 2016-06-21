package com.nalatarate.meister.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nalatarate.meister.MeisterApplication


class EntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = null
        val intent: Intent
        if (!MeisterApplication.isLoggedIn(this@EntryActivity)) {
            intent = Intent(this, LoginActivity::class.java)
        } else {
            intent = Intent(this, HomeActivity::class.java)
        }
        startActivity(intent)
        finish()
    }

}