@file:Suppress("NewAPI")

package io.github.rhymezxcode.simplestore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import io.github.rhymezxcode.simplestore.Constants.SIMPLE_STORE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File

private val Context.dataStore: DataStore<Preferences>
        by preferencesDataStore(SIMPLE_STORE)

private val Context.encryptedDatastore by dataStore(
    fileName = "$SIMPLE_STORE.json",
    serializer = SimpleStoreSerializer(CryptoManager())
)

class DatastorePreference(
    private val context: Context,
    private val encrypted: Boolean? = false
) {
    private val exception = Exception(
        "You have to state if your .encryption() " +
                "is either true or false in your SimpleStore.builder()"
    )

    private inline fun <reified T> getDefaultPreference(): T {
        return when (encrypted) {
            true -> context.encryptedDatastore as T
            false -> context.dataStore as T
            else -> throw exception
        }
    }

    suspend fun saveStringToStore(key: String?, value: String?) {
        when (encrypted) {
            true -> {
                getDefaultPreference<DataStore<Store>>().updateData {
                    Store(
                        key = key,
                        value = value
                    )
                }
            }

            false -> {
                getDefaultPreference<DataStore<Preferences>>().edit { settings ->
                    val dataStoreKey = stringPreferencesKey(key!!)
                    settings[dataStoreKey] = value ?: ""
                }
            }

            else -> throw exception
        }
    }

    suspend fun saveBooleanToStore(key: String?, value: Boolean?) {
        when (encrypted) {
            true -> {
                getDefaultPreference<DataStore<Store>>().updateData {
                    Store(
                        key = key,
                        bool = value
                    )
                }
            }

            false -> {
                getDefaultPreference<DataStore<Preferences>>().edit { settings ->
                    val dataStoreKey = booleanPreferencesKey(key!!)
                    settings[dataStoreKey] = value ?: false
                }
            }

            else -> throw exception
        }

    }

    fun getStringFromStore(key: String?): Flow<String> {
        return when (encrypted) {
            true -> {
                getDefaultPreference<DataStore<Store>>().data.map {
                    it.value ?: ""
                }
            }

            false -> {
                val dataStoreKey = stringPreferencesKey(key!!)
                getDefaultPreference<DataStore<Preferences>>().data.map {
                    it[dataStoreKey] ?: ""
                }
            }

            else -> throw exception
        }
    }

    fun getBooleanFromStore(key: String?): Flow<Boolean> {
        return when (encrypted) {
            true -> {
                getDefaultPreference<DataStore<Store>>().data.map {
                    it.bool ?: false
                }
            }

            false -> {
                val dataStoreKey = booleanPreferencesKey(key!!)
                getDefaultPreference<DataStore<Preferences>>().data.map {
                    it[dataStoreKey] ?: false
                }
            }

            else -> throw exception
        }
    }


    suspend fun clearAllTheStore() {
        when (encrypted) {
            true -> {
                deleteDatastoreFile()
            }

            false -> {
                getDefaultPreference<DataStore<Preferences>>().edit {
                    it.clear()
                }
            }

            else -> throw exception
        }
    }

    private fun deleteDatastoreFile() {
        val file = File(
            context.filesDir,
            "$DATASTORE_PATH$SIMPLE_STORE$PREFERENCE_EXTENSION"
        )
        file.delete()
    }

    companion object {
        private const val DATASTORE_PATH = "datastore/"
        private const val PREFERENCE_EXTENSION = ".json"
    }


}