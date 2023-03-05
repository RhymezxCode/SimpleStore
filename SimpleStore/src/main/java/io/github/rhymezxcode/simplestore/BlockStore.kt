@file:Suppress("NewAPI")
package io.github.rhymezxcode.simplestore

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.auth.blockstore.Blockstore
import com.google.android.gms.auth.blockstore.BlockstoreClient
import com.google.android.gms.auth.blockstore.DeleteBytesRequest
import com.google.android.gms.auth.blockstore.RetrieveBytesRequest
import com.google.android.gms.auth.blockstore.RetrieveBytesResponse
import com.google.android.gms.auth.blockstore.StoreBytesData
import io.github.rhymezxcode.simplestore.Constants.SIMPLE_STORE
import io.github.rhymezxcode.simplestore.Constants.TAG
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Arrays

class BlockStore(
    private val context: Context
) {

    private fun getClient(): BlockstoreClient {
        return Blockstore.getClient(context)
    }

    fun saveByteArrayToStore(key: String?, value: ByteArray): Boolean {
        var status = false
        try {
            val storeRequest = StoreBytesData.Builder()
                .setBytes(value) // Call this method to set the key value with which the data should be associated with.
                .setKey(key ?: BlockstoreClient.DEFAULT_BYTES_DATA_KEY)
                .build()
            getClient().storeBytes(storeRequest)
                .addOnSuccessListener { result: Int ->
                    Log.d(
                        TAG,
                        "Stored $result bytes"
                    )
                    status = true
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Failed to store bytes", e)
                    status = false
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return status
    }


    fun getByteArrayFromStore(key: String?): ByteArray? {
        var total: ByteArray? = null
        val requestedKeys =
            listOf(key ?: BlockstoreClient.DEFAULT_BYTES_DATA_KEY) // Add keys to array

        val retrieveRequest = RetrieveBytesRequest.Builder()
            .setKeys(requestedKeys)
            .build()

        getClient().retrieveBytes(retrieveRequest)
            .addOnSuccessListener { result: RetrieveBytesResponse ->
                val blockStoreDataMap =
                    result.blockstoreDataMap
                for ((key, value) in blockStoreDataMap) {
                    Log.d(
                        ContentValues.TAG, String.format(
                            "Retrieved bytes %s associated with key %s.",
                            String(value.bytes), key
                        )
                    )
                    total = value.bytes
                }
            }
            .addOnFailureListener { e: Exception? ->
                Log.e(
                    ContentValues.TAG,
                    "Failed to store bytes",
                    e
                )
                total = null
            }
        return total
    }

    fun getEveryThingFromStore(): ByteArray? {
        var total: ByteArray? = null

        val retrieveRequest = RetrieveBytesRequest.Builder()
            .setRetrieveAll(true)
            .build()

        getClient().retrieveBytes(retrieveRequest)
            .addOnSuccessListener { result: RetrieveBytesResponse ->
                val blockStoreDataMap =
                    result.blockstoreDataMap
                for ((key, value) in blockStoreDataMap) {
                    Log.d(
                        ContentValues.TAG, String.format(
                            "Retrieved bytes %s associated with key %s.",
                            String(value.bytes), key
                        )
                    )
                    total = value.bytes
                }
            }
            .addOnFailureListener { e: Exception? ->
                Log.e(
                    ContentValues.TAG,
                    "Failed to store bytes",
                    e
                )
                total = null
            }
        return total
    }

    fun deleteByKey(key: String): Boolean {
        var status = false
        val requestedKeys = listOf(key) // Add keys to array

        val retrieveRequest = DeleteBytesRequest.Builder()
            .setKeys(requestedKeys)
            .build()

        getClient().deleteBytes(retrieveRequest)
            .addOnSuccessListener { result: Boolean ->
                Log.d(TAG,
                    "Any data found and deleted? $result")
                status = result
            }
        return status
    }

    fun clearAllTheStore(): Boolean {
        var status = false
        val deleteAllRequest = DeleteBytesRequest.Builder()
            .setDeleteAll(true)
            .build()
        getClient().deleteBytes(deleteAllRequest)
            .addOnSuccessListener { result: Boolean ->
                Log.d(TAG,
                    "Any data found and deleted? $result")
                status = result
            }
        return status
    }
}