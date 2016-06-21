package com.nalatarate.meister.api

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.text.TextUtils
import com.nalatarate.meister.api.model.data.DataCreateSession
import okio.Buffer
import okio.ByteString
import rx.Observable
import rx.schedulers.Schedulers
import java.security.SecureRandom
import java.util.*

/**
 * Created by Tiberiu on 6/21/2016.
 */
private const val INSTALLATION_ID = "installation_id"
private const val SESSION = "session_id"
private const val USER = "user_id"

class PrefManager(context: Context) {

    constructor(context: Context, file: String = "secure") : this(context) {
        this.file = file
    }

    lateinit private var mPreferences: SharedPreferences
    lateinit private var mEdit: SharedPreferences.Editor
    var file = "secure"

    init {
        mPreferences = context.getSharedPreferences(file, Context.MODE_PRIVATE)
        mEdit = mPreferences.edit()
    }

    companion object {

        private var instance: PrefManager? = null

        @JvmStatic
        @Synchronized fun getInstance(context: Context): PrefManager {
            if (instance == null) {
                instance = PrefManager(context)
            }
            return instance!!
        }


    }

    internal fun saveSession(dataSession: DataCreateSession) =
            Observable.create<DataCreateSession> {
                if (it.isUnsubscribed) return@create
                setSessionId(dataSession.userSession.sessionId)
                setUserId(dataSession.userSession.userId)
                it.onNext(dataSession)
                it.onCompleted()
            }


    val sessionId: String?
        get() = mPreferences.getString(SESSION, null)

    val  userId : String?
        get() =  mPreferences.getString(USER, null)

    fun setSessionId(id: String){
        mEdit.putString(SESSION, id).apply()
    }

    fun setUserId(id: String){
        mEdit.putString(USER, id).apply()
    }


    fun generateInstallationID(): ByteString {
        val random = SecureRandom()
        val androidID = androidId
        val randomString = getRandomString(random)

        val buffer = Buffer().write(androidID).write(randomString)
        return buffer.readByteString()
    }

    val androidId: ByteString
        get() = ByteString.of(*Arrays.copyOf(androidIdBuffer.write(deviceId).readByteArray(), 64))


    val androidIdBuffer: Buffer
        get() {
            val androidId = mPreferences.getString(INSTALLATION_ID, null)
            val result: ByteString
            if (!TextUtils.isEmpty(androidId)) {
                result = ByteString.encodeUtf8(androidId.replace("android_id", ""))
            } else {
                result = ByteString.encodeUtf8(mPreferences.getString(INSTALLATION_ID, null).replace("android_id", ""))
            }

            return Buffer().write(result)
        }

    val deviceId: ByteString
        get() {
            if (TextUtils.isEmpty(Build.SERIAL)) {
                val zeroes = ByteArray(16)
                Arrays.fill(zeroes, 0.toByte())
                return ByteString.of(*zeroes)
            }
            return ByteString.of(*Arrays.copyOf(ByteString.encodeUtf8(Build.SERIAL).toByteArray(), 16))
        }

    private fun getRandomString(random: SecureRandom): ByteString {
        val randomByte = ByteArray(16)
        random.nextBytes(randomByte)
        return ByteString.of(*randomByte)
    }

    internal fun getInstallationIDSync(): ByteString {
        val installIDBase64 = mPreferences.getString(INSTALLATION_ID, null)
        if (!TextUtils.isEmpty(installIDBase64)) {
            return ByteString.decodeBase64(installIDBase64)
        }
        val installationID = generateInstallationID()
        mEdit.putString(INSTALLATION_ID, installationID.base64()).apply()
        return installationID
    }

    internal fun getInstallationID(): Observable<String> {
        return Observable.create(Observable.OnSubscribe<String> { subscriber ->
            try {
                subscriber.onNext(getInstallationIDSync().base64())
                subscriber.onCompleted()
            } catch (e: Exception) {
                subscriber.onError(e)
            }
        }).subscribeOn(Schedulers.io()).cache()
    }
}