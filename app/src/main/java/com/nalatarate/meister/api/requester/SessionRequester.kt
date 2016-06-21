package com.nalatarate.meister.api.requester

import com.nalatarate.meister.api.Api
import com.nalatarate.meister.api.PrefManager
import com.nalatarate.meister.api.model.body.BodyUserRegisterLogin
import com.nalatarate.meister.api.model.data.DataCreateSession
import com.nalatarate.meister.api.model.data.DataSession
import com.nalatarate.meister.api.webservice.SessionService
import rx.Observable
import rx.schedulers.Schedulers

/**
 * Created by Tiberiu on 6/20/2016.
 */
object SessionRequester {
    private val service by lazy { Api.create(SessionService::class.java) }

    @JvmStatic
    @JvmOverloads
    internal fun login(email: String, password: String): Observable<DataSession> =
            Observable.create(Observable.OnSubscribe<BodyUserRegisterLogin> { subscriber ->
                try {
                    subscriber.onNext(BodyUserRegisterLogin(email, password, "", PrefManager.getInstance(Api.mContext).getInstallationIDSync().base64()))
                    subscriber.onCompleted()
                } catch (e: Exception) {
                    subscriber.onError(e)
                }
            }).subscribeOn(Schedulers.io()).cache()
                    .flatMap { service.login(it) }
                    .flatMap{ PrefManager.getInstance(Api.mContext).saveSession(it.data)}
                    .map { it.userSession }


    @JvmStatic
    @JvmOverloads
    internal fun register(email: String, password: String): Observable<DataSession> =
            Observable.create(Observable.OnSubscribe<BodyUserRegisterLogin> { subscriber ->
                try {
                    subscriber.onNext(BodyUserRegisterLogin(email, password, "", PrefManager.getInstance(Api.mContext).getInstallationIDSync().base64()))
                    subscriber.onCompleted()
                } catch (e: Exception) {
                    subscriber.onError(e)
                }
            }).subscribeOn(Schedulers.io()).cache()
                    .flatMap { service.register(it) }
                    .flatMap{ PrefManager.getInstance(Api.mContext).saveSession(it.data)}
                    .map { it.userSession }
}