package com.nalatarate.meister.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import com.nalatarate.meister.MeisterApplication
import com.nalatarate.meister.R
import com.nalatarate.meister.api.model.data.DataSession
import com.nalatarate.meister.api.requester.SessionRequester
import com.nalatarate.meister.utils.InertObserver
import kotlinx.android.synthetic.main.activity_login.*
import rx.schedulers.Schedulers

/**
 * Created by Tiberiu on 6/23/2016.
 */

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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

        passwordLogin.setOnFocusChangeListener { view, motionEvent ->
            login_password_wrapper.error = null
        }
        emailLogin.setOnFocusChangeListener { view, b ->
            login_email_wrapper.error = null

        }
        btnForgotPassword.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
        }
    }

    fun login() {
        MeisterApplication.Companion.showProgressBar(this@LoginActivity)
        Log.d("STATUS","SHOWED PROGRESS BAR")
        SessionRequester.login(emailLogin.text.toString(), passwordLogin.text.toString())
                .subscribeOn(Schedulers.io()).subscribe(object : InertObserver<DataSession>() {


            override fun onError(e: Throwable?) {
                MeisterApplication.closeProgressBar()
                Snackbar.make(btnLoginConfirm, "${e?.message}", Snackbar.LENGTH_LONG)
            }

            override fun onNext(session: DataSession) {
                Log.d("Results", "${session.sessionId},${session.userId}")
                MeisterApplication.closeProgressBar()
                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            }
        })
    }

}