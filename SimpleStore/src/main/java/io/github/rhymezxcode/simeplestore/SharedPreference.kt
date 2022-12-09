package io.github.rhymezxcode.simeplestore

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import io.github.rhymezxcode.simeplestore.Constants.SIMPLE_STORE

@RequiresApi(Build.VERSION_CODES.M)
class SharedPreference(
    private val context: Context,
    private val prefName: String? = null,
    private val encrypted: Boolean? = false
) {

    private fun getSharedPreferences(): SharedPreferences? {
        try {
            return context.getSharedPreferences(
                prefName ?: SIMPLE_STORE,
                Context.MODE_PRIVATE
            )
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "Error on getting shared preferences", e)
        }
        return null
    }


    private fun getDefaultPreference(): SharedPreferences? {
        return when (encrypted) {
            true -> CryptoManager
                .getEncryptedSharedPreferences(context, prefName ?: SIMPLE_STORE)
            false -> getSharedPreferences()
            else -> getSharedPreferences()
        }
    }

    suspend fun saveStringToStore(key: String?, value: String?) {
        val sharedPreferences = getDefaultPreference()
        val editor = sharedPreferences?.edit()
        editor?.putString(key, value)
        editor?.apply()
    }

    suspend fun saveBooleanToStore(key: String?, value: String?) {
        val sharedPreferences = getDefaultPreference()
        val editor = sharedPreferences?.edit()
        editor?.putString(key, value)
        editor?.apply()
    }

    suspend fun getStringFromStore(key: String?): String? {
        return getDefaultPreference()?.getString(key, null)
    }

    suspend fun getBooleanFromStore(key: String?): Boolean? {
        return getDefaultPreference()?.getBoolean(key, false)
    }

    suspend fun clearAllTheStore() {
        val editor = getDefaultPreference()?.edit()
        editor?.clear()
        editor?.apply()
    }
}