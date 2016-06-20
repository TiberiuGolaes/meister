package com.nalatarate.meister.api

import android.content.Context
import android.os.Build
import android.support.annotation.VisibleForTesting
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.nalatarate.meister.BuildConfig
import com.nalatarate.meister.api.serializer.DateDeserializer
import com.nalatarate.meister.api.serializer.DateSerializer
import com.nalatarate.meister.api.serializer.SimpleArrayMapJsonSerializer

import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Tiberiu on 6/19/2016.
 */
object Api {

    private const val ZULU_DATE = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    private const val ZULU_DATE_MILLIS = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    private val SIMPLE_DATE_FORMAT = SimpleDateFormat(ZULU_DATE, Locale.UK).apply { timeZone = TimeZone.getTimeZone("UTC") }
    private val SIMPLE_DATE_FORMAT_BACKUP = SimpleDateFormat(ZULU_DATE_MILLIS, Locale.UK).apply { timeZone = TimeZone.getTimeZone("UTC") }

    internal val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .registerTypeAdapter(SimpleArrayMapJsonSerializer.TYPE, SimpleArrayMapJsonSerializer())
            .registerTypeAdapter(Date::class.java, DateSerializer(SIMPLE_DATE_FORMAT))
            .registerTypeAdapter(Date::class.java, DateDeserializer(SIMPLE_DATE_FORMAT, SIMPLE_DATE_FORMAT_BACKUP))
            .create()

    @JvmStatic
    internal lateinit var retrofit: Retrofit

    internal fun with( debug: Boolean = false) {
        val factory = DefaultRetrofitFactory()
        val client = factory.setupClient(debug)
        retrofit = factory.createRetrofit(client)
    }

    @VisibleForTesting
    interface RetrofitFactory {
        fun createRetrofit(client: OkHttpClient): Retrofit
        fun setupClient( debug: Boolean): OkHttpClient
    }
    //: String = "Android/${Build.VERSION.RELEASE}/${BuildConfig.VERSION_NAME}/${BuildConfig.VERSION_CODE}/${BuildConfig.BUILD_TYPE}"
    @VisibleForTesting
    fun with(retrofitFactory: RetrofitFactory, clientHeader: String = "Android/${Build.VERSION.RELEASE}/${BuildConfig.VERSION_NAME}/${BuildConfig.VERSION_CODE}/${BuildConfig.BUILD_TYPE}", debug: Boolean = false): Api {
        val client = retrofitFactory.setupClient( debug)
        retrofit = retrofitFactory.createRetrofit(client)
        return this
    }


    private class DefaultRetrofitFactory constructor() : RetrofitFactory {

        override fun setupClient( debug: Boolean): OkHttpClient {
            val hostname = "http://86.105.187.122:8080/SportsNLT/"

            val certificatePinner = CertificatePinner.Builder().add(hostname)

            return OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply { level = if (debug) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE })
                    .certificatePinner(certificatePinner.build())
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build()
        }

        override fun createRetrofit(client: OkHttpClient): Retrofit =
                Retrofit.Builder()
                        .baseUrl("http://86.105.187.122:8080/SportsNLT/")
                        .client(client)
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
    }


    @JvmStatic
    @JvmOverloads
    fun <T> create(service: Class<T>, retrofit: Retrofit = this.retrofit) = retrofit.create(service)
}