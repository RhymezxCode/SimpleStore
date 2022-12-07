package io.github.rhymezxcode.simeplestore

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
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
            true -> SharedPreferenceCryptoManager
                .getEncryptedSharedPreferences(context, prefName ?: SIMPLE_STORE)

            false -> getSharedPreferences()
            else -> getSharedPreferences()
        }
    }

    fun saveStringToStore(key: String?, value: String?) {
        val sharedPreferences = getDefaultPreference()
        val editor = sharedPreferences?.edit()
        editor?.putString(key, value)
        editor?.apply()
    }

    fun saveBooleanToStore(key: String?, value: String?) {
        val sharedPreferences = getDefaultPreference()
        val editor = sharedPreferences?.edit()
        editor?.putString(key, value)
        editor?.apply()
    }

    fun getStringFromStore(key: String?): String? {
        return getDefaultPreference()?.getString(key, null)
    }

    fun getBooleanFromStore(key: String?): Boolean? {
        return getDefaultPreference()?.getBoolean(key, false)
    }

    fun clearAllTheStore() {
        val editor = getDefaultPreference()?.edit()
        editor?.clear()
        editor?.apply()
    }
}