package com.nalatarate.meister.api.model.data

import com.google.gson.annotations.Expose
import java.util.*

/**
 * Created by tiberiugolaes on 02/07/16.
 */
internal class FriendReceived(
        @Expose val senderId: String,
        @Expose val receivedId: String,
        @Expose val senderName: String,
        @Expose val motivation: String,
        @Expose val requestDate: Date
)