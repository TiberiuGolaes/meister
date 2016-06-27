package com.nalatarate.meister.ui.activities

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import com.facebook.CallbackManager
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.nalatarate.meister.MeisterApplication
import com.nalatarate.meister.R
import kotlinx.android.synthetic.main.activity_first.*
import java.util.*


class FirstActivity : BaseActivity() {

    lateinit var callbackManager: CallbackManager
    private val fbIconScale: Float = 1.6f
    private var state = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        MeisterApplication.FBLogout()
        callbackManager = com.facebook.CallbackManager.Factory.create()
        btnFacebook.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"))
        btnFacebook.registerCallback(callbackManager, object : com.facebook.FacebookCallback<LoginResult> {

            override fun onSuccess(result: LoginResult?) {
                if (result != null) {
                }
            }

            override fun onCancel() {
            }

            override fun onError(error: FacebookException?) {
            }
        })
        resizeFacebookButton()
        btnLogin.setOnClickListener {
            startActivity(Intent(this@FirstActivity, LoginActivity::class.java))
        }

        btnRegister.setOnClickListener {
            startActivity(Intent(this@FirstActivity, RegisterActivity::class.java))
        }
    }


    internal fun resizeFacebookButton() {
        var drawable: Drawable
        try {
            drawable = ContextCompat.getDrawable(this@FirstActivity, com.facebook.R.drawable.com_facebook_button_icon)
            drawable.setBounds(0, 0, Math.round(drawable.intrinsicWidth * fbIconScale),
                    Math.round(drawable.intrinsicHeight * fbIconScale))
            btnFacebook.setCompoundDrawables(drawable, null, null, null)
            btnFacebook.compoundDrawablePadding = resources.getDimensionPixelOffset(R.dimen.space_small);
            btnFacebook.setPadding(
                    resources.getDimensionPixelSize(
                            R.dimen.space_small),
                    resources.getDimensionPixelSize(
                            R.dimen.space_small),
                    0,
                    resources.getDimensionPixelSize(
                            R.dimen.space_small))
        } catch (e: Exception) {
            Log.d("Error", "Facebook icon could not scale")
            return
        }
    }
}