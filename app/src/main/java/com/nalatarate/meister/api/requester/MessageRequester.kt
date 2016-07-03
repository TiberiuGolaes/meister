package com.nalatarate.meister.api.requester

import com.nalatarate.meister.api.Api
import com.nalatarate.meister.api.PrefManager
import com.nalatarate.meister.api.model.body.BodyCreateConversation
import com.nalatarate.meister.api.model.body.BodySendMessage
import com.nalatarate.meister.api.model.data.Conversation
import com.nalatarate.meister.api.model.data.Message
import com.nalatarate.meister.api.webservice.FriendshipService
import com.nalatarate.meister.api.webservice.MessageService
import rx.Observable

/**
 * Created by tiberiugolaes on 02/07/16.
 */
internal object MessageRequester {
    private val service by lazy { Api.create(MessageService::class.java) }

    @JvmStatic
    @JvmOverloads
    fun createConversation(receiverId: String, message: String): Observable<Void> =
            service.createConversation(BodyCreateConversation(PrefManager.userId.orEmpty(), receiverId, message))

    @JvmStatic
    @JvmOverloads
    fun sendMessage(message: String, conversationId: String): Observable<Void> =
            service.sendMessage(BodySendMessage(PrefManager.userId.orEmpty(), message, conversationId))

    @JvmStatic
    @JvmOverloads
    fun getConversations(): Observable<List<Conversation>> =
            service.getConversations(PrefManager.userId.orEmpty()).map {
                it.data.conversation
            }

    @JvmStatic
    @JvmOverloads
    fun getMessages(conversationId: String, limit: Int, offset: Int): Observable<List<Message>> =
            service.getMessages(PrefManager.userId.orEmpty(), conversationId, limit, offset).map {
                it.data.message
            }
}