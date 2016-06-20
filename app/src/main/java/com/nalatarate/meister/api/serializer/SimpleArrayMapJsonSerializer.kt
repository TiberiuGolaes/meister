package com.nalatarate.meister.api.serializer
import android.support.v4.util.SimpleArrayMap

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken

import java.lang.reflect.Type

class SimpleArrayMapJsonSerializer : JsonSerializer<SimpleArrayMap<Any, Any>> {

    override fun serialize(src: SimpleArrayMap<Any, Any>?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement? {
        if (src == null) {
            return null
        }
        val jsonObject = JsonObject()
        val length = src.size()
        var k: Any
        var v: Any
        for (i in 0..length - 1) {
            k = src.keyAt(i)
            v = src.valueAt(i)
            jsonObject.add(k.toString(), context.serialize(v))
        }
        return jsonObject
    }

    companion object {
        @JvmField
        val TYPE = object : TypeToken<SimpleArrayMap<Any, Any>>() {
        }.type
    }
}
