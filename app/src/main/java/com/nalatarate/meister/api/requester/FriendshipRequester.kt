package com.nalatarate.meister.api.requester

import com.nalatarate.meister.api.Api
import com.nalatarate.meister.api.PrefManager
import com.nalatarate.meister.api.model.body.BodyRespondRequest
import com.nalatarate.meister.api.model.body.BodySendRequest
import com.nalatarate.meister.api.model.body.BodyUserSport
import com.nalatarate.meister.api.model.data.DataRequests
import com.nalatarate.meister.api.model.data.Sport
import com.nalatarate.meister.api.webservice.FriendshipService
import com.nalatarate.meister.api.webservice.SportsService
import rx.Observable

/**
 * Created by tiberiugolaes on 02/07/16.
 */
object FriendshipRequester {
    private val service by lazy { Api.create(FriendshipService::class.java) }


    @JvmStatic
    @JvmOverloads
    internal fun getRequests(): Observable<DataRequests> =
            service.getRequests(PrefManager.userId.orEmpty()).map {
                it.data
            }

    @JvmStatic
    @JvmOverloads
    internal fun sendFriendRequest(receiverId: String, motivation: String): Observable<Void> =
            service.sendRequest(BodySendRequest(PrefManager.userId.orEmpty(), receiverId, motivation))


    @JvmStatic
    @JvmOverloads
    internal fun respondFriendRequest(receiverId: String, response: Boolean): Observable<Void> =
            service.respondRequest(BodyRespondRequest(PrefManager.userId.orEmpty(), receiverId, response))
}