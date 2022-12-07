package io.github.rhymezxcode.simeplestore

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

@RequiresApi(Build.VERSION_CODES.M)
object SharedPreferenceCryptoManager {

    private fun getMasterKey(context: Context): MasterKey? {
        try {
            return MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "Error on getting master key", e)
        }
        return null
    }

    fun getEncryptedSharedPreferences(context: Context, prefName: String?): SharedPreferences? {
        try {
            return getMasterKey(context)?.let {
                EncryptedSharedPreferences.create(
                    context,
                    prefName ?: Constants.SIMPLE_STORE,
                    it,  // calling the method above for creating MasterKey
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
            }
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "Error on getting encrypted shared preferences", e)
        }
        return null
    }

}