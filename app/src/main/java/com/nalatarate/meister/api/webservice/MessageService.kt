package com.nalatarate.meister.api.webservice

import com.nalatarate.meister.api.meta.Meta
import com.nalatarate.meister.api.meta.ResponseWithMeta
import com.nalatarate.meister.api.model.body.BodyCreateConversation
import com.nalatarate.meister.api.model.data.DataConversations
import com.nalatarate.meister.api.model.body.BodySendMessage
import com.nalatarate.meister.api.model.data.DataMessage
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import rx.Observable

/**
 * Created by tiberiugolaes on 02/07/16.
 */
internal interface MessageService {

    @POST("sendMessage")
    fun sendMessage(@Body messageBodySendMessage: BodySendMessage): Observable<Void>

    @POST("sendMessage")
    fun createConversation(@Body conversationBodyCreateConversation: BodyCreateConversation): Observable<Void>

    @GET("getConversations?userId={$P_USER_ID_VAR}")
    fun getConversations(@Path(P_USER_ID) userId: String): Observable<ResponseWithMeta<DataConversations, Meta>>

    @GET("getMessages?userId={$P_USER_ID_VAR}&&conversationId={$P_CONVERSATION_ID_VAR}&&limit={$P_LIMIT_VAR}&&offset={$P_OFFSET_VAR}")
    fun getMessages(@Path(P_USER_ID) userId: String, @Path(P_CONVERSATION_ID) conversationId: String, @Path(P_LIMIT) limit: Int, @Path(P_OFFSET) offset: Int):
            Observable<ResponseWithMeta<DataMessage, Meta>>
}