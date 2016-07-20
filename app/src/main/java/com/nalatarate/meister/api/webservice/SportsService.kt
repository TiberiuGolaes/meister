package com.nalatarate.meister.api.webservice

import com.nalatarate.meister.api.meta.Data
import com.nalatarate.meister.api.meta.MetaDate
import com.nalatarate.meister.api.meta.ResponseWithMeta
import com.nalatarate.meister.api.model.body.BodyUserSport
import com.nalatarate.meister.api.model.data.DataCreateSession
import com.nalatarate.meister.api.model.data.DataSports
import retrofit2.http.*
import rx.Observable

/**
 * Created by Tiberiu on 6/28/2016.
 */
const val P_USER_ID = "userId"
const val P_USER_ID_VAR = "{$P_USER_ID}"
const val P_SPORT_ID = "sportId"
const val P_SPORT_ID_VAR = "{$P_SPORT_ID}"
const val P_CONVERSATION_ID = "conversationId"
const val P_CONVERSATION_ID_VAR = "{$P_CONVERSATION_ID}"
const val P_OFFSET = "offset"
const val P_OFFSET_VAR = "{$P_OFFSET}"
const val P_LIMIT = "limit"
const val P_LIMIT_VAR = "{$P_LIMIT}"
const val P_RATED_USER = "ratedUserId"
const val P_RATED_USER_VAR = "{$P_RATED_USER}"

internal interface SportsService {

    @GET("getUserSports")
    fun getUserSports(@Query(P_USER_ID) userId: String): Observable<ResponseWithMeta<DataSports, MetaDate>>

    @GET("getSports")
    fun getSports(): Observable<ResponseWithMeta<DataSports, MetaDate>>

    @DELETE("removeUserSport")
    fun removeSport(@Query(P_USER_ID) userId: String, @Query(P_SPORT_ID) sportId: String): Observable<Void>

    @POST("addUserSport")
    fun addUserSport(@Body user: BodyUserSport): Observable<Void>
}