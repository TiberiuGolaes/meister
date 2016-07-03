package com.nalatarate.meister.api.model.data

import com.google.gson.annotations.Expose

/**
 * Created by tiberiugolaes on 02/07/16.
 */
internal class Rating(
        @Expose val raterUserId: String,
        @Expose val competitionId: String,
        @Expose val rating: Double,
        @Expose val raterName: String
)