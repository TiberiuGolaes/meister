package com.nalatarate.meister.api.requester

import com.nalatarate.meister.api.Api
import com.nalatarate.meister.api.PrefManager
import com.nalatarate.meister.api.model.body.BodyUserSport
import com.nalatarate.meister.api.model.data.Sport
import com.nalatarate.meister.api.webservice.SportsService
import rx.Observable

/**
 * Created by tiberiugolaes on 02/07/16.
 */
object SportsRequester {
    private val service by lazy { Api.create(SportsService::class.java) }


    @JvmStatic
    @JvmOverloads
    internal fun getAllSports(): Observable<List<Sport>> =
            service.getSports().map {
                it.data.sports
            }

    @JvmStatic
    @JvmOverloads
    internal fun getUserSports(): Observable<List<Sport>> =
            service.getUserSports(PrefManager.userId.orEmpty()).map {
                it.data.sports
            }

    @JvmStatic
    @JvmOverloads
    internal fun removeUserSport(sportId: String): Observable<Void> =
            service.removeSport(PrefManager.userId.orEmpty(), sportId)

    @JvmStatic
    @JvmOverloads
    internal fun addUserSport(sportId : String, level: Double): Observable<Void> =
            service.addUserSport(BodyUserSport(PrefManager.userId.orEmpty(), sportId, level))

}