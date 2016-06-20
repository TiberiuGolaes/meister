package com.nalatarate.meister.api.requester

import com.nalatarate.meister.api.Api
import com.nalatarate.meister.api.model.body.BodyUserRegisterLogin
import com.nalatarate.meister.api.model.data.DataCreateSession
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
    internal fun login(email: String, password: String, deviceId: String?): Observable<DataCreateSession> =
            Observable.create(Observable.OnSubscribe<BodyUserRegisterLogin> { subscriber ->
                try {
                    subscriber.onNext(BodyUserRegisterLogin(email, password, "", deviceId))
                    subscriber.onCompleted()
                } catch (e: Exception) {
                    subscriber.onError(e)
                }
            }).subscribeOn(Schedulers.io()).cache()
                    .flatMap { service.login(it) }
                    .map { it.data }

    @JvmStatic
    @JvmOverloads
    internal fun register(email: String, password: String, deviceId: String?): Observable<DataCreateSession> =
            Observable.create(Observable.OnSubscribe<BodyUserRegisterLogin> { subscriber ->
                try {
                    subscriber.onNext(BodyUserRegisterLogin(email, password, "", deviceId))
                    subscriber.onCompleted()
                } catch (e: Exception) {
                    subscriber.onError(e)
                }
            }).subscribeOn(Schedulers.io()).cache()
                    .flatMap { service.register(it) }
                    .map { it.data }
}