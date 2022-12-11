package io.github.rhymezxcode.simplestore

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import io.github.rhymezxcode.simplestore.Constants.SIMPLE_STORE
import io.github.rhymezxcode.simplestore.CryptoManager.getEncryptedDatastorePreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences>
        by preferencesDataStore(SIMPLE_STORE)
private val Context.encryptedDatastore: DataStore<Preferences>?
    @RequiresApi(Build.VERSION_CODES.M)
    get() = getEncryptedDatastorePreferences(this)

@RequiresApi(Build.VERSION_CODES.M)
class DatastorePreference(
    private val context: Context,
    private val encrypted: Boolean? = false
) {

    private fun getDefaultPreference(): DataStore<Preferences>? {
        return when (encrypted) {
            true -> context.encryptedDatastore
            false -> context.dataStore
            else -> context.dataStore
        }
    }

    suspend fun saveStringToStore(key: String?, value: String?) {
        val dataStoreKey = stringPreferencesKey(key!!)
        getDefaultPreference()?.edit { settings ->
            settings[dataStoreKey] = value ?: ""
        }
    }

    suspend fun saveBooleanToStore(key: String?, value: Boolean?) {
        val dataStoreKey = booleanPreferencesKey(key!!)
        getDefaultPreference()?.edit { settings ->
            settings[dataStoreKey] = value ?: false
        }
    }

    fun getStringFromStore(key: String?): Flow<String>? {
        val dataStoreKey = stringPreferencesKey(key!!)
        return getDefaultPreference()?.data?.map {
            it[dataStoreKey] ?: ""
        }
    }

    fun getBooleanFromStore(key: String?): Flow<Boolean>? {
        val dataStoreKey = booleanPreferencesKey(key!!)
        return getDefaultPreference()?.data?.map {
            it[dataStoreKey] ?: false
        }
    }


    suspend fun clearAllTheStore() {
        getDefaultPreference()?.edit {
            it.clear()
        }
    }


}