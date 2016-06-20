package com.nalatarate.meister.api.meta

import com.google.gson.annotations.Expose

/**
 * Created by Tiberiu on 6/19/2016.
 */
class ResponseWithMeta<D : Data, M : Meta>(status: String, data: D, @Expose val meta: M) : Response<D>(status, data)