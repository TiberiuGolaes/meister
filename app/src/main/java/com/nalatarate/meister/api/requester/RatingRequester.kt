package com.nalatarate.meister.api.requester

import com.nalatarate.meister.api.Api
import com.nalatarate.meister.api.PrefManager
import com.nalatarate.meister.api.model.body.BodySubmitRating
import com.nalatarate.meister.api.model.data.Rating
import com.nalatarate.meister.api.webservice.MessageService
import com.nalatarate.meister.api.webservice.RatingService
import rx.Observable

/**
 * Created by tiberiugolaes on 02/07/16.
 */
object RatingRequester {
    private val service by lazy { Api.create(RatingService::class.java) }

    @JvmStatic
    @JvmOverloads
    internal fun submitRating(ratedId: String, rating: Double, sportId: String, competitionId: String): Observable<Void> =
            service.submitRating(BodySubmitRating(PrefManager.userId.orEmpty(), ratedId, competitionId, sportId, rating))

    @JvmStatic
    @JvmOverloads
    internal fun getAllRatings(userId: String, sportId: String): Observable<List<Rating>> =
            service.getAllRatings(userId, sportId).map {
                it.data.rating
            }

    @JvmStatic
    @JvmOverloads
    internal fun getMyRatings(sportId: String): Observable<List<Rating>> =
            service.getAllRatings(PrefManager.userId.orEmpty(), sportId).map {
                it.data.rating
            }
}