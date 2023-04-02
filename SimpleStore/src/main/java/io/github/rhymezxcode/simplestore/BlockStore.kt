@file:Suppress("NewAPI")

package io.github.rhymezxcode.simplestore

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.google.android.gms.auth.blockstore.Blockstore
import com.google.android.gms.auth.blockstore.BlockstoreClient
import com.google.android.gms.auth.blockstore.DeleteBytesRequest
import com.google.android.gms.auth.blockstore.RetrieveBytesRequest
import com.google.android.gms.auth.blockstore.RetrieveBytesResponse
import com.google.android.gms.auth.blockstore.StoreBytesData
import io.github.rhymezxcode.simplestore.Constants.TAG

class BlockStore(
    private val context: Context,
    private val enableCloud: Boolean
) {

    private fun getClient(): BlockstoreClient {
        return Blockstore.getClient(context)
    }

    fun saveByteArrayToStore(key: String?, value: ByteArray): Boolean {
        var status = false

        val storeRequest = StoreBytesData.Builder()
            .setBytes(value) // Call this method to set the key value with which the data should be associated with.
            .setKey(key ?: BlockstoreClient.DEFAULT_BYTES_DATA_KEY)
            .build()

        try {
            when (enableCloud) {
                true -> {
                    getClient().isEndToEndEncryptionAvailable
                        .addOnSuccessListener { isE2EEAvailable ->
                            if (isE2EEAvailable) {
                                if(storeRequest.shouldBackupToCloud()) {
                                    Log.d(
                                        TAG,
                                        "E2EE is available, enable backing up bytes to the cloud."
                                    )

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
                                }else{
                                    Log.d(
                                        TAG,
                                        "Failed to store bytes"
                                    )
                                    status = false
                                }

                            } else {
                                Log.d(
                                    TAG,
                                    "E2EE is not available, only store bytes for D2D restore."
                                )
                            }
                        }
                }

                else -> {
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
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return status
    }


    fun getByteArrayFromStore(key1: String?): ByteArray? {
        var total: ByteArray? = null
        val requestedKeys =
            listOf(key1 ?: BlockstoreClient.DEFAULT_BYTES_DATA_KEY) // Add keys to array

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

    fun checkIfEndToEndEncryptionIsAvailable(): Boolean{
        var status = false
        getClient().isEndToEndEncryptionAvailable
            .addOnSuccessListener { result ->
                Log.d(TAG, "Will Block Store cloud backup be end-to-end encrypted? $result")
                status = result
            }
        return status
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
                Log.d(
                    TAG,
                    "Any data found and deleted? $result"
                )
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
                Log.d(
                    TAG,
                    "Any data found and deleted? $result"
                )
                status = result
            }
        return status
    }
}