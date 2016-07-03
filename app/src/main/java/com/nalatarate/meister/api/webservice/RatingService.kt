package com.nalatarate.meister.api.webservice

import com.nalatarate.meister.api.meta.Meta
import com.nalatarate.meister.api.meta.ResponseWithMeta
import com.nalatarate.meister.api.model.body.BodySubmitRating
import com.nalatarate.meister.api.model.data.DataRatings
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import rx.Observable

/**
 * Created by tiberiugolaes on 02/07/16.
 */
internal interface RatingService {
    @POST("submitRating")
    fun submitRating(@Body body: BodySubmitRating): Observable<Void>

    @GET("getAllRatings?ratedUserId={$P_RATED_USER_VAR}&&sportId={$P_SPORT_ID_VAR}")
    fun getAllRatings(@Path(P_RATED_USER) userId: String, @Path(P_SPORT_ID) sportId: String): Observable<ResponseWithMeta<DataRatings, Meta>>
}