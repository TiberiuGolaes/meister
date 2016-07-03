package com.nalatarate.meister.api.model.data

import com.google.gson.annotations.Expose
import com.nalatarate.meister.api.meta.Data

/**
 * Created by tiberiugolaes on 02/07/16.
 */
internal class DataConversations(
        @Expose val conversation: List<Conversation>
) : Data