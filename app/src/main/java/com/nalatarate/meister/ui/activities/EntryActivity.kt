package com.nalatarate.meister.ui.activities

import android.content.Intent
import android.os.Bundle
import com.nalatarate.meister.MeisterApplication


class EntryActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = null
        val intent: Intent
        if (!MeisterApplication.isLoggedIn()) {
            intent = Intent(this, FirstActivity::class.java)
        } else {
            intent = Intent(this, HomeActivity::class.java)
        }
        startActivity(intent)
        finish()
    }

}