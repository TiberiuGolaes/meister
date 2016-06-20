package com.nalatarate.meister.api.webservice

import com.nalatarate.meister.api.meta.MetaDate
import com.nalatarate.meister.api.meta.ResponseWithMeta
import com.nalatarate.meister.api.model.body.BodyUserRegisterLogin
import com.nalatarate.meister.api.model.data.DataCreateSession
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import rx.Observable

/**
 * Created by Tiberiu on 6/19/2016.
 */


internal interface SessionService {

    @POST("loginEmail")
    fun login( @Body user: BodyUserRegisterLogin): Observable<ResponseWithMeta<DataCreateSession, MetaDate>>

    @POST("registerEmail")
    fun register( @Body user: BodyUserRegisterLogin): Observable<ResponseWithMeta<DataCreateSession, MetaDate>>

}