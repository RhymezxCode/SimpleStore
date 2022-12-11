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
import io.github.rhymezxcode.simplestore.Constants.SIMPLE_STORE_ENCRYPTED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SIMPLE_STORE)
private val Context.encryptedDatastore: DataStore<Preferences>?
    @RequiresApi(Build.VERSION_CODES.M)
    get() = CryptoManager
        .getEncryptedDatastorePreferences(this, SIMPLE_STORE_ENCRYPTED)

@RequiresApi(Build.VERSION_CODES.M)
class DatastorePreference(
    private val context: Context,
    private val prefName: String? = null,
    private val encrypted: Boolean? = false
) {
    private val Context._dataStore: DataStore<Preferences>
            by preferencesDataStore(prefName ?: SIMPLE_STORE)

    private val dataStore: DataStore<Preferences> = context._dataStore


    private fun getDefaultPreference(): DataStore<Preferences>? {
        return when (encrypted) {
            true -> CryptoManager
                .getEncryptedDatastorePreferences(context, prefName ?: SIMPLE_STORE)
            false -> dataStore
            else -> dataStore
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