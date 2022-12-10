package io.github.rhymezxcode.networkstateobserver.network

import android.app.Activity
import android.content.Context
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Build
import androidx.annotation.RequiresApi
import io.github.rhymezxcode.simeplestore.DatastorePreference
import io.github.rhymezxcode.simeplestore.SharedPreference

@RequiresApi(Build.VERSION_CODES.M)
class SimpleStore(
    private val context: Context?,
    private val name: String?,
    private val encrypted: Boolean
) {

    fun createSharedPreference(): SharedPreference? {
        return context?.let {
            SharedPreference(
                context = it,
                prefName = name,
                encrypted = encrypted
            )
        }
    }

    fun createDatastorePreference(): DatastorePreference? {
        return context?.let {
            DatastorePreference(
                context = it,
                prefName = name,
                encrypted = encrypted
            )
        }
    }

    private constructor(builder: Builder) : this(
        builder.context, builder.name, builder.encrypted == false
    )

    class Builder {

        var context: Context? = null
            private set
        var name: String? = null
            private set
        var encrypted: Boolean? = null
            private set

        fun getApplicationContext(context: Context) = apply { this.context = context }

        fun getStoreName(name: String) = apply { this.name = name }

        fun getEncryptionBoolean(encrypted: Boolean) = apply { this.encrypted = encrypted }

        fun build() = SimpleStore(this)
    }

}