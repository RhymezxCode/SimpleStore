package io.github.rhymezxcode.simplepreference

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import io.github.rhymezxcode.simplepreference.Constants.SIMPLE_PREFERENCE


class SharedPreference(
    private val context: Context,
    private val prefName: String? = null,
    private val encrypted: Boolean? = false
) {

    private fun getMasterKey(): MasterKey? {
        try {
            return MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "Error on getting master key", e)
        }
        return null
    }

    private fun getEncryptedSharedPreferences(): SharedPreferences? {
        try {
            return getMasterKey()?.let {
                EncryptedSharedPreferences.create(
                    context,
                    prefName ?: SIMPLE_PREFERENCE,
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

    private fun getSharedPreferences(): SharedPreferences? {
        try {
            return context.getSharedPreferences(
                prefName ?: SIMPLE_PREFERENCE,
                Context.MODE_PRIVATE
            )
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "Error on getting shared preferences", e)
        }
        return null
    }

    private fun getDefaultPreference(): SharedPreferences? {
        return when (encrypted) {
            true -> getEncryptedSharedPreferences()
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