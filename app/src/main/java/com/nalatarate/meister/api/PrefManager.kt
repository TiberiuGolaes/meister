package com.nalatarate.meister.api

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.text.TextUtils
import android.util.Log
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
private const val USER_DESCRIPTION = "user_description"

object PrefManager {

    lateinit private var mPreferences: SharedPreferences
    lateinit private var mEdit: SharedPreferences.Editor
    var file = "secure"
    lateinit var mContext : Context

    internal fun saveContext(context: Context){
        mContext = context
        mPreferences = mContext.getSharedPreferences(file, Context.MODE_PRIVATE)
        mEdit = mPreferences.edit()
    }

    internal fun saveSession(dataSession: DataCreateSession) =
            Observable.create<DataCreateSession> {
                if (it.isUnsubscribed) {
                    Log.d("STATUS", "UNSUBSCRIBED")
                    return@create
                }
                Log.d("STATUS", "SAVING SESSION")
                setSessionId(dataSession.userSession.sessionId)
                setUserId(dataSession.userSession.userId)
                Log.d("STATUS", "SAVED SESSION")
                it.onNext(dataSession)
                it.onCompleted()
            }


    val sessionId: String?
        get() = mPreferences.getString(SESSION, null)

    val  userId : String?
        get() =  mPreferences.getString(USER, null)

    fun setSessionId(id: String){
        mEdit.putString(SESSION, id).apply()
        mEdit.commit()
    }

    fun clearSession(){
        mEdit.remove(SESSION)
    }

    fun setUserId(id: String){
        mEdit.putString(USER, id).apply()
        mEdit.commit()
    }

    val description: String?
        get() = mPreferences.getString(USER_DESCRIPTION, "")

    fun setDescription(descr : String){
        mEdit.putString(USER_DESCRIPTION, descr)
        mEdit.commit()
    }


    fun generateInstallationID(): ByteString {
        val random = SecureRandom()
        val androidID = androidId
        val randomString = getRandomString(random)


        val buffer = Buffer().write(androidID).write(randomString)
        return buffer.readByteString()
    }

    val androidId: ByteString
        get() = ByteString.of(*Arrays.copyOf(Buffer().write(deviceId).readByteArray(), 64))


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
        if( mPreferences.getString(INSTALLATION_ID, null) != null){
            Log.d("STATUS", "NOT NULL")
            val installIDBase64 = mPreferences.getString(INSTALLATION_ID, null)
            if (!TextUtils.isEmpty(installIDBase64)) {
                return ByteString.decodeBase64(installIDBase64)
            }
            else return ByteString.EMPTY
        }
        else {
            Log.d("STATUS", "IS NULL")
            val installationID = generateInstallationID()
            Log.d("STATUS", installationID.base64())
            mEdit.putString(INSTALLATION_ID, installationID.base64()).apply()
            return installationID
        }
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