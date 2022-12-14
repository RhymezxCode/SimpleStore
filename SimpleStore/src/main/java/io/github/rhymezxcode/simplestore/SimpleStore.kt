@file:Suppress("NewAPI")
package io.github.rhymezxcode.simplestore

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineDispatcher

class SimpleStore(
    private val context: Context?,
    private val encrypted: Boolean?,
    private val dispatcher: CoroutineDispatcher?
) {

    @Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")
    inline fun <reified T> getType(): T {
        return when (T::class.java) {
            SharedPreference::class.java -> sharedPreference() as T
            DatastorePreference::class.java -> datastorePreference() as T
            else -> throw Exception("Unhandled return type for preference")
        }
    }

    private fun sharedPreference(): SharedPreference? {
        return context?.let {
            SharedPreference(
                context = it,
                encrypted = encrypted,
                dispatcher = dispatcher
            )
        }
    }

    private fun datastorePreference(): DatastorePreference? {
        return context?.let {
            DatastorePreference(
                context = it,
                encrypted = encrypted
            )
        }
    }

    private constructor(builder: Builder) : this(
        builder.context, builder.encrypted, builder.dispatcher
    )

    class Builder {
        var context: Context? = null
            private set
        var name: String? = null
            private set
        var encrypted: Boolean? = null
            private set

        var dispatcher: CoroutineDispatcher? = null
            private set

        fun context(context: Context) = apply { this.context = context }

        fun storeName(name: String) = apply { Constants.SIMPLE_STORE = name }

        fun dispatcher(dispatcher: CoroutineDispatcher) = apply { this.dispatcher = dispatcher }

        fun encryption(encrypted: Boolean) = apply { this.encrypted = encrypted }

        fun build() = SimpleStore(this)
    }

}