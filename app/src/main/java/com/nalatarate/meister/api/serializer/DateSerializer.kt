package com.nalatarate.meister.api.serializer

/**
 * Created by Tiberiu on 6/19/2016.
 */
import android.util.Log
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.text.DateFormat
import java.util.*

class DateSerializer(private val dateFormat: DateFormat) : JsonSerializer<Date> {

    override fun serialize(date: Date, type: Type, jsonSerializationContext: JsonSerializationContext): JsonElement? {
        try {
            val format = dateFormat.format(date)
            return JsonPrimitive(format)
        } catch (e: NullPointerException) {
            Log.e("DateSerializer", "Failed to parse null date", e)
        } catch (e: IllegalArgumentException) {
            Log.e("DateSerializer", "Invalid Date Format", e)
        }

        return null
    }
}