@file:Suppress("NewAPI")

package io.github.rhymezxcode.simplestore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import io.github.rhymezxcode.simplestore.Constants.SIMPLE_STORE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File


private val Context.dataStore: DataStore<Preferences>
        by preferencesDataStore(SIMPLE_STORE)

private val Context.encryptedDatastore by dataStore(
    fileName = "${SIMPLE_STORE}.json",
    serializer = SimpleStoreSerializer(CryptoManager())
)

class DatastorePreference(
    val name: String? = null,
    private val context: Context,
    private val encrypted: Boolean? = false
) {
    init {
        SIMPLE_STORE = name?:"SimpleStore"
    }

    private val exception = Exception(
        "You have to state if your .encryption() " +
                "is either true or false in your SimpleStore.builder()"
    )

    private inline fun <reified T> getdefaultPreference(): T {
        return when (encrypted) {
//            true -> context.encryptedDatastore as T
            true -> throw Exception("Encrypted datastore feature is not ready for now, it is still on development.")
            false -> context.dataStore as T
            else -> throw exception
        }
    }

    suspend fun saveStringToStore(key: String?, value: String?) {
        when (encrypted) {
            true -> {
                getdefaultPreference<DataStore<Store>>().updateData {
                    Store(
                        key = key,
                        value = value
                    )
                }
            }

            false -> {
                getdefaultPreference<DataStore<Preferences>>().edit { settings ->
                    val dataStoreKey = stringPreferencesKey(key!!)
                    settings[dataStoreKey] = value ?: ""
                }
            }

            else -> throw exception
        }
    }

    suspend fun saveIntToStore(key: String?, value: Int?) {
        when (encrypted) {
//            true -> {
//                getdefaultPreference<DataStore<Store>>().updateData {
//                    Store(
//                        key = key,
//                        value = value
//                    )
//                }
//            }

            false -> {
                getdefaultPreference<DataStore<Preferences>>().edit { settings ->
                    val dataStoreKey = intPreferencesKey(key!!)
                    settings[dataStoreKey] = value ?: 0
                }
            }

            else -> throw exception
        }
    }

    suspend fun saveFloatToStore(key: String?, value: Float?) {
        when (encrypted) {
//            true -> {
//                getdefaultPreference<DataStore<Store>>().updateData {
//                    Store(
//                        key = key,
//                        value = value
//                    )
//                }
//            }

            false -> {
                getdefaultPreference<DataStore<Preferences>>().edit { settings ->
                    val dataStoreKey = floatPreferencesKey(key!!)
                    settings[dataStoreKey] = value ?: 0.0F
                }
            }

            else -> throw exception
        }
    }

    suspend fun saveLongToStore(key: String?, value: Long?) {
        when (encrypted) {
//            true -> {
//                getdefaultPreference<DataStore<Store>>().updateData {
//                    Store(
//                        key = key,
//                        value = value
//                    )
//                }
//            }

            false -> {
                getdefaultPreference<DataStore<Preferences>>().edit { settings ->
                    val dataStoreKey = longPreferencesKey(key!!)
                    settings[dataStoreKey] = value ?: 0L
                }
            }

            else -> throw exception
        }
    }

    suspend fun saveDoubleSetToStore(key: String?, value: Double?) {
        when (encrypted) {
//            true -> {
//                getdefaultPreference<DataStore<Store>>().updateData {
//                    Store(
//                        key = key,
//                        value = value
//                    )
//                }
//            }

            false -> {
                getdefaultPreference<DataStore<Preferences>>().edit { settings ->
                    val dataStoreKey = doublePreferencesKey(key!!)
                    settings[dataStoreKey] = value ?: 0.0
                }
            }

            else -> throw exception
        }
    }

    suspend fun saveStringSetToStore(key: String?, value: Set<String>?) {
        when (encrypted) {
//            true -> {
//                getdefaultPreference<DataStore<Store>>().updateData {
//                    Store(
//                        key = key,
//                        value = value
//                    )
//                }
//            }

            false -> {
                getdefaultPreference<DataStore<Preferences>>().edit { settings ->
                    val dataStoreKey = stringSetPreferencesKey(key!!)
                    settings[dataStoreKey] = value ?: setOf()
                }
            }

            else -> throw exception
        }
    }

    suspend fun saveBooleanToStore(key: String?, value: Boolean?) {
        when (encrypted) {
//            true -> {
//                getdefaultPreference<DataStore<Store>>().updateData {
//                    Store(
//                        key = key,
//                        bool = value
//                    )
//                }
//            }

            false -> {
                getdefaultPreference<DataStore<Preferences>>().edit { settings ->
                    val dataStoreKey = booleanPreferencesKey(key!!)
                    settings[dataStoreKey] = value ?: false
                }
            }

            else -> throw exception
        }

    }

    fun getDoubleFromStore(key: String?): Flow<Double> {
        return when (encrypted) {
//            true -> {
//                getdefaultPreference<DataStore<Store>>().data.map {
//                    it.value ?: ""
//                }
//            }

            false -> {
                val dataStoreKey = doublePreferencesKey(key!!)
                getdefaultPreference<DataStore<Preferences>>().data.map {
                    it[dataStoreKey] ?: 0.0
                }
            }

            else -> throw exception
        }
    }

    fun getIntFromStore(key: String?): Flow<Int> {
        return when (encrypted) {
//            true -> {
//                getdefaultPreference<DataStore<Store>>().data.map {
//                    it.value ?: ""
//                }
//            }

            false -> {
                val dataStoreKey = intPreferencesKey(key!!)
                getdefaultPreference<DataStore<Preferences>>().data.map {
                    it[dataStoreKey] ?: 0
                }
            }

            else -> throw exception
        }
    }

    fun getLongFromStore(key: String?): Flow<Long> {
        return when (encrypted) {
//            true -> {
//                getdefaultPreference<DataStore<Store>>().data.map {
//                    it.value ?: ""
//                }
//            }

            false -> {
                val dataStoreKey = longPreferencesKey(key!!)
                getdefaultPreference<DataStore<Preferences>>().data.map {
                    it[dataStoreKey] ?: 0L
                }
            }

            else -> throw exception
        }
    }

    fun getFloatFromStore(key: String?): Flow<Float> {
        return when (encrypted) {
//            true -> {
//                getdefaultPreference<DataStore<Store>>().data.map {
//                    it.value ?: ""
//                }
//            }

            false -> {
                val dataStoreKey = floatPreferencesKey(key!!)
                getdefaultPreference<DataStore<Preferences>>().data.map {
                    it[dataStoreKey] ?: 0.0F
                }
            }

            else -> throw exception
        }
    }

    fun getStringSetFromStore(key: String?): Flow<Set<String>> {
        return when (encrypted) {
//            true -> {
//                getdefaultPreference<DataStore<Store>>().data.map {
//                    it.value ?: ""
//                }
//            }

            false -> {
                val dataStoreKey = stringSetPreferencesKey(key!!)
                getdefaultPreference<DataStore<Preferences>>().data.map {
                    it[dataStoreKey] ?: setOf()
                }
            }

            else -> throw exception
        }
    }

    fun getStringFromStore(key: String?): Flow<String> {
        return when (encrypted) {
//            true -> {
//                getdefaultPreference<DataStore<Store>>().data.map {
//                    it.value ?: ""
//                }
//            }

            false -> {
                val dataStoreKey = stringPreferencesKey(key!!)
                getdefaultPreference<DataStore<Preferences>>().data.map {
                    it[dataStoreKey] ?: ""
                }
            }

            else -> throw exception
        }
    }

    fun getBooleanFromStore(key: String?): Flow<Boolean> {
        return when (encrypted) {
//            true -> {
//                getdefaultPreference<DataStore<Store>>().data.map {
//                    it.bool ?: false
//                }
//            }

            false -> {
                val dataStoreKey = booleanPreferencesKey(key!!)
                getdefaultPreference<DataStore<Preferences>>().data.map {
                    it[dataStoreKey] ?: false
                }
            }

            else -> throw exception
        }
    }


    suspend fun clearAllTheStore() {
        when (encrypted) {
//            true -> {
//                deleteDatastoreFile()
//            }

            false -> {
                getdefaultPreference<DataStore<Preferences>>().edit {
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