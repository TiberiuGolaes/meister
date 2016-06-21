package com.nalatarate.meister.api.model.data

import com.google.gson.annotations.Expose
import com.nalatarate.meister.api.meta.Data

/**
 * Created by tiberiugolaes on 21/06/16.
 */
internal class DataSession(
        @Expose val userId: String,
        @Expose val sessionId: String)