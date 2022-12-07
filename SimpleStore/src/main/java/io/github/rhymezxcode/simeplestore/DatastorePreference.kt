package io.github.rhymezxcode.simeplestore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import io.github.rhymezxcode.simeplestore.Constants.SIMPLE_STORE
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


class DatastorePreference(
    context: Context,
    prefName: String? = null,
    private val encrypted: Boolean? = false
) {

    private val Context._dataStore: DataStore<Preferences>
            by preferencesDataStore(prefName?:SIMPLE_STORE)

    private val dataStore: DataStore<Preferences> = context._dataStore

    suspend fun saveStringToStore(key: String?, value: String?) {
        val dataStoreKey = stringPreferencesKey(key!!)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value!!
        }
    }

    suspend fun saveBooleanToStore(key: String?, value: Boolean?) {
        val dataStoreKey = booleanPreferencesKey(key!!)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value!!
        }
    }

    suspend fun getStringFromStore(key: String?): String {
        val dataStoreKey = stringPreferencesKey(key!!)
        return dataStore.data.map {
            it[dataStoreKey] ?: ""
        }.first()
    }

    suspend fun getBooleanFromStore(key: String?): Boolean {
        val dataStoreKey = booleanPreferencesKey(key!!)
        return dataStore.data.map {
            it[dataStoreKey] ?: false
        }.first()
    }


    suspend fun clearAllTheStore() {
        dataStore.edit {
            it.clear()
        }
    }


}