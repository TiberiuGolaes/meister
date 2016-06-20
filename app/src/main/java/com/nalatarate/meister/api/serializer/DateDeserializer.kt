package com.nalatarate.meister.api.serializer

/**
 * Created by Tiberiu on 6/19/2016.
 */
import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.text.DateFormat
import java.text.ParseException
import java.util.*

class DateDeserializer(private val dateFormat: DateFormat, private val dateFormatBackup: DateFormat?) : JsonDeserializer<Date> {

    @Throws(JsonParseException::class)
    override fun deserialize(element: JsonElement, arg1: Type, arg2: JsonDeserializationContext): Date? {
        val date = element.asString
        try {
            return dateFormat.parse(date)
        } catch (e: NullPointerException) {
            Log.e("DateDeserializer", "Failed to parse null date:", e)
        } catch (e: ParseException) {
            Log.e("DateDeserializer", "Failed to parse Date due to:", e)
            try {
                return dateFormatBackup?.parse(date)
            } catch (e1: ParseException) {
                Log.e("DateDeserializer", "Failed to parse Date due to:", e)
            }
        }
        return null
    }

}