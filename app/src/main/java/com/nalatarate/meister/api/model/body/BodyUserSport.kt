package com.nalatarate.meister.api.model.body

import com.google.gson.annotations.Expose

/**
 * Created by tiberiugolaes on 02/07/16.
 */
internal class BodyUserSport(@Expose val userId: String,
                             @Expose val sportId: String,
                             @Expose val level: Double)