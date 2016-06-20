package com.nalatarate.meister.api.meta

import com.google.gson.annotations.Expose

/**
 * Created by Tiberiu on 6/19/2016.
 */
open class Response<D : Data>(@Expose val status: String, @Expose val data: D)