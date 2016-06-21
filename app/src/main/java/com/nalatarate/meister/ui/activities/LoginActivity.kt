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
import com.nalatarate.meister.api.model.data.DataCreateSession
import com.nalatarate.meister.api.requester.SessionRequester
import com.nalatarate.meister.utils.InertObserver
import kotlinx.android.synthetic.main.activity_login.*
import rx.schedulers.Schedulers
import java.util.*


class LoginActivity : BaseActivity() {

    lateinit var callbackManager: CallbackManager
    private val fbIconScale: Float = 1.6f
    private var state = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
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
            state = 1
            initialView.visibility = View.GONE
            loginView.visibility = View.VISIBLE
        }
        btnLoginConfirm.setOnClickListener {
            if (emailLogin.isValid() && passwordLogin.isValid()) {
                login()
            } else {
                if (!emailLogin.isValid()) {
                    login_email_wrapper.error = getString(R.string.error_email)
                } else {
                    login_email_wrapper.error = null
                }
                if (!passwordLogin.isValid()) {
                    login_password_wrapper.error = getString(R.string.error_password)
                } else {
                    login_password_wrapper.error = null
                }
            }
        }
        btnForgotPassword.setOnClickListener {
            state = 2
            loginView.visibility = View.GONE
            passwordView.visibility = View.VISIBLE
        }
        btnReset.setOnClickListener {
            if (!emailPassword.isValid()) {
                password_email_wrapper.error = getString(R.string.error_email)
            } else {
                password_email_wrapper.error = null
                resetPassword()
            }
        }
        btnRegister.setOnClickListener {
            state = 3
            initialView.visibility = View.GONE
            registerView.visibility = View.VISIBLE
        }
        btnRegisterConfirm.setOnClickListener {
            if (emailRegister.isValid() && passwordRegister.isValid() && passwordRegister.text.toString().equals(passwordRegister2.text.toString())) {
                register()
            } else {
                if (!emailRegister.isValid()) {
                    register_email_wrapper.error = getString(R.string.error_email)
                } else {
                    register_email_wrapper.error = null
                }
                if (!passwordRegister.text.equals(passwordRegister2.text)) {
                    register_password_wrapper.error = getString(R.string.password_missmatch)
                    register_password2_wrapper.error = getString(R.string.password_missmatch)
                } else {
                    register_password_wrapper.error = null
                    register_password2_wrapper.error = null
                }
                if (!passwordRegister.isValid()) {
                    register_password_wrapper.error = getString(R.string.error_password)
                } else {
                    register_password_wrapper.error = null
                }
                if (!passwordRegister2.isValid()) {
                    register_password2_wrapper.error = getString(R.string.error_password)
                } else {
                    register_password2_wrapper.error = null
                }
            }
        }
        emailRegister.setOnFocusChangeListener { view, motionEvent ->
            register_email_wrapper.error = null
        }
        emailPassword.setOnFocusChangeListener { view, b ->
            password_email_wrapper.error = null
        }
        emailLogin.setOnFocusChangeListener { view, b ->
            login_email_wrapper.error = null

        }
        passwordRegister.setOnFocusChangeListener { view, motionEvent ->
            register_password_wrapper.error = null
        }
        passwordRegister2.setOnFocusChangeListener { view, motionEvent ->
            register_password2_wrapper.error = null
        }
        passwordLogin.setOnFocusChangeListener { view, motionEvent ->
            login_password_wrapper.error = null
        }
    }

    fun login() {
        MeisterApplication.showProgressBar(this@LoginActivity)
        SessionRequester.login(emailLogin.text.toString(), passwordLogin.text.toString(), "ABCDEFGH12345678")
                .subscribeOn(Schedulers.io()).subscribe(object : InertObserver<DataCreateSession>() {


            override fun onError(e: Throwable?) {
                MeisterApplication.closeProgressBar()
                Snackbar.make(btnLoginConfirm, "${e?.message}", Snackbar.LENGTH_LONG)
            }

            override fun onNext(session: DataCreateSession){
                Log.d("Results", "${session.userSession.sessionId},${session.userSession.userId}")
                MeisterApplication.closeProgressBar()
                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            }
        })
    }

    fun resetPassword() {

    }

        fun register() {
            MeisterApplication.showProgressBar(this@LoginActivity)
            SessionRequester.register(emailRegister.text.toString(), passwordRegister.text.toString(), "ABCDEFGH12345678")
                    .subscribeOn(Schedulers.io()).subscribe(object : InertObserver<DataCreateSession>() {

                override fun onError(e: Throwable?) {
                    MeisterApplication.closeProgressBar()
                    Snackbar.make(btnLoginConfirm, "${e?.message}", Snackbar.LENGTH_LONG)
                }

                override fun onNext(session: DataCreateSession){
                    Log.d("Results", "${session.userSession.sessionId},${session.userSession.userId}")
                    MeisterApplication.closeProgressBar()
                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                }
            })
        }

    override fun onBackPressed() {
        if (state == 3) {
            registerView.visibility = View.GONE
            initialView.visibility = View.VISIBLE
            state = 0
        } else if (state == 2) {
            passwordView.visibility = View.GONE
            loginView.visibility = View.VISIBLE
            state = 1
        } else if (state == 1) {
            loginView.visibility = View.GONE
            initialView.visibility = View.VISIBLE
            state = 0
        } else {
            super.onBackPressed()
        }
    }

    internal fun resizeFacebookButton() {
        var drawable: Drawable
        try {
            drawable = ContextCompat.getDrawable(this@LoginActivity, com.facebook.R.drawable.com_facebook_button_icon)
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