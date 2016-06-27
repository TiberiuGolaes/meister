package com.nalatarate.meister.api.requester

import android.util.Log
import com.nalatarate.meister.MeisterApplication
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
    internal fun login(email: String, password: String): Observable<DataSession>
            = PrefManager.getInstallationID().map {
                devId -> BodyUserRegisterLogin(email, password, "", devId) }
                    .flatMap { service.login(it) }
                    .flatMap { PrefManager.saveSession(it.data) }
                    .map { it.userSession }


    @JvmStatic
    @JvmOverloads
    internal fun register(email: String, password: String): Observable<DataSession> =
            PrefManager.getInstallationID().map {
                devId -> BodyUserRegisterLogin(email, password, "", devId) }
                    .flatMap { service.register(it) }
                    .flatMap {PrefManager.saveSession(it.data) }
                    .map { it.userSession }
}