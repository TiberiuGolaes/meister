package com.nalatarate.meister.api.model.data

import com.google.gson.annotations.Expose
import java.util.*

/**
 * Created by tiberiugolaes on 02/07/16.
 */
internal class FriendSent(
        @Expose val senderId: String,
        @Expose val receivedId: String,
        @Expose val receiverName: String,
        @Expose val requestDate: Date
)