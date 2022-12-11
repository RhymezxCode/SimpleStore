package io.github.rhymezxcode.simplestore

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
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

private val Context.dataStore: DataStore<Preferences>
        by preferencesDataStore(SIMPLE_STORE)

@RequiresApi(Build.VERSION_CODES.M)
class DatastorePreference(
    private val context: Context,
    private val encrypted: Boolean? = false
) {

    private val Context.encryptedDatastore by dataStore(
        fileName = "$SIMPLE_STORE.json",
        serializer = SimpleStoreSerializer(CryptoManager())
    )

    private inline fun <reified T> getDefaultPreference(): T {
        return when (encrypted) {
            true -> context.encryptedDatastore as T
            false -> context.dataStore as T
            else -> throw Exception("Always state if your .encryption() is either true or false")
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

            else -> throw Exception("Always state if your .encryption() is either true or false")
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

            else -> throw Exception("Always state if your .encryption() is either true or false")
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

            else -> throw Exception("Always state if your .encryption() is either true or false")
        }
    }

    fun getBooleanFromStore(key: String?): Flow<Boolean>? {
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

            else -> throw Exception("Always state if your .encryption() is either true or false")
        }
    }


    suspend fun clearAllTheStore() {
        when (encrypted) {
            true -> {
                getDefaultPreference<DataStore<Store>>().updateData {
                    Store(
                        null,
                        null,
                        null
                    )
                }
            }

            false -> {
                getDefaultPreference<DataStore<Preferences>>().edit {
                    it.clear()
                }
            }

            else -> throw Exception("Always state if your .encryption() is either true or false")
        }
    }


}