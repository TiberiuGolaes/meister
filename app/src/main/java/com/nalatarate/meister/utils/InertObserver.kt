package com.nalatarate.meister.utils

import rx.Observer

/**
 * Created by tiberiugolaes on 30/05/16.
 */
open class InertObserver<T> : Observer<T> {
    override fun onCompleted() {
    }

    override fun onNext(t: T) {
    }

    override fun onError(e: Throwable?) {
    }
}