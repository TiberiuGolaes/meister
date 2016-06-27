package com.nalatarate.meister.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import com.nalatarate.meister.MeisterApplication
import com.nalatarate.meister.R
import com.nalatarate.meister.api.model.data.DataSession
import com.nalatarate.meister.api.requester.SessionRequester
import com.nalatarate.meister.utils.InertObserver
import kotlinx.android.synthetic.main.activity_register.*
import rx.schedulers.Schedulers

/**
 * Created by Tiberiu on 6/23/2016.
 */

class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)
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
        passwordRegister.setOnFocusChangeListener { view, motionEvent ->
            register_password_wrapper.error = null
        }
        passwordRegister2.setOnFocusChangeListener { view, motionEvent ->
            register_password2_wrapper.error = null
        }
    }

    fun register() {
        MeisterApplication.Companion.showProgressBar(this@RegisterActivity)
        SessionRequester.register(emailRegister.text.toString(), passwordRegister.text.toString())
                .subscribeOn(Schedulers.io()).subscribe(object : InertObserver<DataSession>() {

            override fun onError(e: Throwable?) {
                MeisterApplication.closeProgressBar()
                Snackbar.make(btnRegisterConfirm, "${e?.message}", Snackbar.LENGTH_LONG)
            }

            override fun onNext(session: DataSession){
                Log.d("Results", "${session.sessionId},${session.userId}")
                MeisterApplication.closeProgressBar()
                startActivity(Intent(this@RegisterActivity, HomeActivity::class.java))
            }
        })
    }
}