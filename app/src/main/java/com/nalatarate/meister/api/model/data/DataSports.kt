package com.nalatarate.meister.api.model.data

import com.google.gson.annotations.Expose
import com.nalatarate.meister.api.meta.Data

/**
 * Created by tiberiugolaes on 02/07/16.
 */
internal class DataSports(@Expose val sport: List<Sport>) : Data
