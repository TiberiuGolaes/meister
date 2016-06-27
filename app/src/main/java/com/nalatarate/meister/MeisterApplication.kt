package com.nalatarate.meister

import android.app.Application
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.nalatarate.meister.api.Api
import com.nalatarate.meister.api.PrefManager
import com.nalatarate.meister.utils.ProgressDialogFragment

/**
 * Created by tiberiugolaes on 26/05/16.
 */
const val PROGRESS_FRAGMENT_TAG = "progress"

open class MeisterApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        FacebookSdk.sdkInitialize(applicationContext)
        PrefManager.saveContext(applicationContext)
        Api.with(applicationContext, true)
    }

    companion object {


        var progressBar: ProgressDialogFragment? = null

        fun isLoggedIn(): Boolean {
            if (PrefManager.sessionId != null) {
                return true
            } else {
                return false
            }
        }

        fun FBLogout() {
            if (FacebookSdk.isInitialized()) {
                var accessToken = AccessToken.getCurrentAccessToken();
                if (accessToken != null) {
                    LoginManager.getInstance().logOut();
                }

            }
        }

        fun showProgressBar(fragment: Fragment) {
            if (progressBar != null && progressBar!!.isAdded) return
            progressBar = ProgressDialogFragment()
            val ft = fragment.activity.supportFragmentManager.beginTransaction()
            ft.add(progressBar, PROGRESS_FRAGMENT_TAG)
            ft.commitAllowingStateLoss()
        }

        fun showProgressBar(activity: AppCompatActivity) {
            if (progressBar != null && progressBar!!.isAdded) return
            progressBar = ProgressDialogFragment()
            val ft = activity.supportFragmentManager.beginTransaction()
            ft.add(progressBar, PROGRESS_FRAGMENT_TAG)
            ft.commitAllowingStateLoss()
        }

        fun closeProgressBar() {
            progressBar?.dismiss()
            progressBar = null
        }

    }
}