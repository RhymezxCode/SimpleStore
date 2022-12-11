@file:Suppress("NewAPI")
package io.github.rhymezxcode.simplestore

import androidx.datastore.core.Serializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

class SimpleStoreSerializer(
    private val cryptoManager: CryptoManager
) : Serializer<Store> {

    override val defaultValue: Store
        get() = Store()

    override suspend fun readFrom(input: InputStream): Store {
        val decryptedBytes = cryptoManager.decrypt(input)
        return try {
            Json.decodeFromString(
                deserializer = Store.serializer(),
                string = decryptedBytes.decodeToString()
            )
        } catch(e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: Store, output: OutputStream) {
        cryptoManager.encrypt(
            bytes = Json.encodeToString(
                serializer = Store.serializer(),
                value = t
            ).encodeToByteArray(),
            outputStream = output
        )
    }
}
