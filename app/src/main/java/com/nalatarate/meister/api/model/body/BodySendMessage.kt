package com.nalatarate.meister.api.model.body

import com.google.gson.annotations.Expose

/**
 * Created by tiberiugolaes on 02/07/16.
 */
internal class BodySendMessage(
        @Expose val senderId: String,
        @Expose val message: String,
        @Expose val conversationId: String
)
