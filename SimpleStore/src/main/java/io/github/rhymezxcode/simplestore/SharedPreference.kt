@file:Suppress("NewAPI")
package io.github.rhymezxcode.simplestore

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import io.github.rhymezxcode.simplestore.Constants.SIMPLE_STORE
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SharedPreference(
    private val context: Context,
    private val encrypted: Boolean? = false,
    private val dispatcher: CoroutineDispatcher? = null
) {

    private fun getSharedPreferences(): SharedPreferences? {
        try {
            return context.getSharedPreferences(
                SIMPLE_STORE,
                Context.MODE_PRIVATE
            )
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "Error on getting shared preferences", e)
        }
        return null
    }


    private fun getdefaultPreference(): SharedPreferences? {
        return when (encrypted) {
            true -> CryptoManager()
                .getEncryptedSharedPreferences(context)
            false -> getSharedPreferences()
            else -> getSharedPreferences()
        }
    }

    fun saveStringToStore(key: String?, value: String?) {
        CoroutineScope(dispatcher?: IO).launch {
            val sharedPreferences = getdefaultPreference()
            val editor = sharedPreferences?.edit()
            editor?.putString(key, value)
            editor?.apply()
        }
    }

    fun saveBooleanToStore(key: String?, value: Boolean?) {
        CoroutineScope(dispatcher?: IO).launch {
            val sharedPreferences = getdefaultPreference()
            val editor = sharedPreferences?.edit()
            if (value != null) {
                editor?.putBoolean(key, value)
                editor?.apply()
            }
        }
    }

    fun saveIntToStore(key: String?, value: Int?) {
        CoroutineScope(dispatcher?: IO).launch {
            val sharedPreferences = getdefaultPreference()
            val editor = sharedPreferences?.edit()
            if (value != null) {
                editor?.putInt(key, value)
                editor?.apply()
            }
        }
    }

    fun saveFloatToStore(key: String?, value: Float?) {
        CoroutineScope(dispatcher?: IO).launch {
            val sharedPreferences = getdefaultPreference()
            val editor = sharedPreferences?.edit()
            if (value != null) {
                editor?.putFloat(key, value)
                editor?.apply()
            }
        }
    }

    fun saveLongToStore(key: String?, value: Long?) {
        CoroutineScope(dispatcher?: IO).launch {
            val sharedPreferences = getdefaultPreference()
            val editor = sharedPreferences?.edit()
            if (value != null) {
                editor?.putLong(key, value)
                editor?.apply()
            }
        }
    }

    fun saveStringSetToStore(key: String?, value: Set<String>?) {
        CoroutineScope(dispatcher?: IO).launch {
            val sharedPreferences = getdefaultPreference()
            val editor = sharedPreferences?.edit()
            if (value != null) {
                editor?.putStringSet(key, value)
                editor?.apply()
            }
        }
    }

    suspend fun getIntFromStore(key: String?): Int? {
        return withContext(dispatcher?: IO){
            getdefaultPreference()?.getInt(key, 0)
        }
    }

    suspend fun getLongFromStore(key: String?): Long? {
        return withContext(dispatcher?: IO){
            getdefaultPreference()?.getLong(key, 0L)
        }
    }

    suspend fun getFloatFromStore(key: String?): Float? {
        return withContext(dispatcher?: IO){
            getdefaultPreference()?.getFloat(key, 0.0F)
        }
    }

    suspend fun getStringSetFromStore(key: String?): Set<String>? {
        return withContext(dispatcher?: IO){
            getdefaultPreference()?.getStringSet(key, setOf())
        }
    }

    suspend fun getStringFromStore(key: String?): String? {
        return withContext(dispatcher?: IO){
            getdefaultPreference()?.getString(key, null)
        }
    }

    suspend fun getBooleanFromStore(key: String?): Boolean? {
        return withContext(dispatcher?: IO) {
           getdefaultPreference()?.getBoolean(key, false)
        }
    }

    fun clearAllTheStore() {
        CoroutineScope(dispatcher?: IO).launch {
            val editor = getdefaultPreference()?.edit()
            editor?.clear()
            editor?.apply()
        }
    }
}