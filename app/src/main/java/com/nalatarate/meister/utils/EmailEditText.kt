package com.nalatarate.meister.utils

import android.R
import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.AppCompatAutoCompleteTextView
import android.text.InputType
import android.util.AttributeSet
import android.util.Log
import android.widget.ArrayAdapter
import com.nalatarate.meister.utils.EditTextExtensions.isValidEmail
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by tiberiugolaes on 30/05/16.
 */
class EmailEditText(context: Context, attrs: AttributeSet?) : AppCompatAutoCompleteTextView(context, attrs), Valid {

    private var mAttached: Boolean = false
    @Transient private var mEmailAddressesSubscription: Subscription? = null

    init {
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mAttached = true
        if (!isInEditMode) {
            updateEmailAddresses()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mAttached = false
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused && mAttached) {
            if (adapter != null && adapter.count > 0) showDropDown()
        }
    }

    override fun isValid(): Boolean {
        setText(text.toString().trim { it <= ' ' })
        return isValidEmail()
    }

    /**
     * Pulls the email addresses from the users email accounts.
     */
    private fun updateEmailAddresses() {
        unSubscribeUpdateEmailAddresses()
        mEmailAddressesSubscription = getEmailAddressAdapter(context)
                .subscribe(object : InertObserver<ArrayAdapter<String>>() {

                    override fun onNext(stringArrayAdapter: ArrayAdapter<String>) {
                        if (!mAttached) return
                        // We don't auto set the email for security reasons, we still auto complete
                        // addresses on the device tho
                        setAdapter(stringArrayAdapter)
                    }
                })
    }

    private fun unSubscribeUpdateEmailAddresses() {
        mEmailAddressesSubscription?.unsubscribe()
        mEmailAddressesSubscription = null
    }

    companion object {
        fun getEmailAddressAdapter(context: Context): Observable<ArrayAdapter<String>> =
                Observable.create<ArrayAdapter<String>> { subscriber ->
                    if (subscriber.isUnsubscribed) return@create

                    try {
                        val accountManager = AccountManager.get(context)
                        var accounts = arrayOfNulls<Account>(0)
                        if (accountManager != null) {
                            accounts = accountManager.getAccountsByType("com.google")
                        }
                        val addresses = arrayOfNulls<String>(accounts.size)
                        for (i in accounts.indices) {
                            addresses[i] = accounts[i]?.name
                        }

                        if (subscriber.isUnsubscribed) return@create
                        subscriber.onNext(ArrayAdapter<String>(context, R.layout.simple_dropdown_item_1line, addresses))
                        subscriber.onCompleted()

                    } catch (e: Exception) {
                        Log.w("Email", "Failed to get Account Info", e)
                        subscriber.onError(e)
                    }
                }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())

    }
}