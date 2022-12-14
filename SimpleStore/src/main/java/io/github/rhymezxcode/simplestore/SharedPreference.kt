@file:Suppress("NewAPI")
package io.github.rhymezxcode.simplestore

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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


    private fun getDefaultPreference(): SharedPreferences? {
        return when (encrypted) {
            true -> CryptoManager()
                .getEncryptedSharedPreferences(context)
            false -> getSharedPreferences()
            else -> getSharedPreferences()
        }
    }

    suspend fun saveStringToStore(key: String?, value: String?) {
        CoroutineScope(dispatcher?: IO).launch {
            val sharedPreferences = getDefaultPreference()
            val editor = sharedPreferences?.edit()
            editor?.putString(key, value)
            editor?.apply()
        }
    }

    suspend fun saveBooleanToStore(key: String?, value: String?) {
        CoroutineScope(dispatcher?: IO).launch {
            val sharedPreferences = getDefaultPreference()
            val editor = sharedPreferences?.edit()
            editor?.putString(key, value)
            editor?.apply()
        }
    }

    suspend fun getStringFromStore(key: String?): String? {
        return withContext(dispatcher?: IO){
            getDefaultPreference()?.getString(key, null)
        }
    }

    suspend fun getBooleanFromStore(key: String?): Boolean? {
        return withContext(dispatcher?: IO) {
           getDefaultPreference()?.getBoolean(key, false)
        }
    }

    fun clearAllTheStore() {
        CoroutineScope(dispatcher?: IO).launch {
            val editor = getDefaultPreference()?.edit()
            editor?.clear()
            editor?.apply()
        }
    }
}