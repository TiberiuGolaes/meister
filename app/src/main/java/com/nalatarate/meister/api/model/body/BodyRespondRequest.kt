package com.nalatarate.meister.api.model.body

import com.google.gson.annotations.Expose

/**
 * Created by tiberiugolaes on 02/07/16.
 */
internal class BodyRespondRequest(@Expose val senderId : String,
                                  @Expose val receiverId: String,
                                  @Expose val response: Boolean)