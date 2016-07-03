package com.nalatarate.meister.api.model.body

import com.google.gson.annotations.Expose

/**
 * Created by tiberiugolaes on 02/07/16.
 */
internal class BodySubmitRating(
        @Expose val raterUserId: String,
        @Expose val ratedUserId: String,
        @Expose val competitionId: String,
        @Expose val sportId: String,
        @Expose val rating: Double
)