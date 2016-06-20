package com.nalatarate.meister.api.model.body

import com.google.gson.annotations.Expose

/**
 * Created by Tiberiu on 6/20/2016.
 */
internal class BodyUserRegisterLogin(@Expose val email: String,
                                     @Expose val password: String,
                                     @Expose val pushToken: String,
                                     @Expose val deviceID: String?)