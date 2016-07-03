package com.nalatarate.meister.api.model.data

import com.google.gson.annotations.Expose

/**
 * Created by tiberiugolaes on 02/07/16.
 */
internal class Conversation (@Expose val userId1 : String,
                             @Expose val userId2: String,
                             @Expose val name: String,
                             @Expose val id: String)
