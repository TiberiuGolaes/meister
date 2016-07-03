package com.nalatarate.meister.api.webservice

import com.nalatarate.meister.api.meta.MetaDate
import com.nalatarate.meister.api.meta.ResponseWithMeta
import com.nalatarate.meister.api.model.body.BodyRespondRequest
import com.nalatarate.meister.api.model.body.BodySendRequest
import com.nalatarate.meister.api.model.data.DataRequests
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import rx.Observable

/**
 * Created by tiberiugolaes on 02/07/16.
 */
internal interface FriendshipService {

    @POST("sendFriendRequest")
    fun sendRequest(@Body request: BodySendRequest): Observable<Void>

    @POST("respondFriendRequest")
    fun respondRequest(@Body request: BodyRespondRequest): Observable<Void>

    @GET("getRequests?userId={$P_USER_ID_VAR}")
    fun getRequests(@Path(P_USER_ID) userId: String): Observable<ResponseWithMeta<DataRequests, MetaDate>>

}