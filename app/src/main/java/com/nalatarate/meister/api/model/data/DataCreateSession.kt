package com.nalatarate.meister.api.model.data

import com.google.gson.annotations.Expose
import com.nalatarate.meister.api.meta.Data

/**
 * Created by Tiberiu on 6/20/2016.
 */
internal class DataCreateSession(@Expose val userSession: DataSession) : Data
