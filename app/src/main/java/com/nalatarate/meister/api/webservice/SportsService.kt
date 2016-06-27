package com.nalatarate.meister.api.webservice

import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Tiberiu on 6/28/2016.
 */
internal interface SportsService {

    @GET("getUserSports")
    fun login( @Path user: String): Observable<ResponseWithMeta<DataCreateSession, MetaDate>>

}