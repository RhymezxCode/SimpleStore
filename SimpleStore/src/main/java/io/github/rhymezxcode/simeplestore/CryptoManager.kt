package io.github.rhymezxcode.simeplestore

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import io.github.osipxd.security.crypto.createEncrypted

@RequiresApi(Build.VERSION_CODES.M)
object CryptoManager {

    private fun getMasterKey(context: Context): MasterKey? {
        try {
            return MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "Error on getting master key", e)
        }
        return null
    }

    fun getEncryptedSharedPreferences(context: Context, prefName: String?): SharedPreferences? {
        try {
            return getMasterKey(context)?.let {
                EncryptedSharedPreferences.create(
                    context,
                    prefName ?: Constants.SIMPLE_STORE,
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

    fun getEncryptedDatastorePreferences(
        context: Context,
        prefName: String?
    ): DataStore<Preferences>? {
        try {
            val aead = AndroidKeysetManager.Builder()
                .withSharedPref(context, "master_keyset",
                    "master_key_preference")
                .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
                .withMasterKeyUri("android-keystore://master_key")
                .build()
                .keysetHandle
                .getPrimitive(Aead::class.java)

            val dataStore = PreferenceDataStoreFactory.createEncrypted(
                encryptionOptions = {
                    // Specify fallback Aead to make it possible to decrypt data encrypted with it
                    fallbackAead = aead
                }
            ) {
                getMasterKey(context)?.let {
                    EncryptedFile(
                        context = context,
                        // The file should have extension .preferences_pb
                        file = context.dataStoreFile(prefName!!),
                        masterKey = it
                    )
                }!!
            }

            return dataStore
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "Error on getting encrypted data store preferences", e)
        }
        return null
    }

}