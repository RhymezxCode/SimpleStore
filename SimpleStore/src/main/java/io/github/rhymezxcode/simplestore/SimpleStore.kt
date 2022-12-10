package io.github.rhymezxcode.simplestore

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineDispatcher

@RequiresApi(Build.VERSION_CODES.M)
class SimpleStore(
    private val context: Context?,
    private val name: String?,
    private val encrypted: Boolean?,
    private val dispatcher: CoroutineDispatcher?
) {

    fun sharedPreference(): SharedPreference? {
        return context?.let {
            SharedPreference(
                context = it,
                prefName = name,
                encrypted = encrypted,
                dispatcher = dispatcher
            )
        }
    }

    fun datastorePreference(): DatastorePreference? {
        return context?.let {
            DatastorePreference(
                context = it,
                prefName = name,
                encrypted = encrypted
            )
        }
    }

    private constructor(builder: Builder) : this(
        builder.context, builder.name, builder.encrypted, builder.dispatcher
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

        fun storeName(name: String) = apply { this.name = name }

        fun dispatcher(dispatcher: CoroutineDispatcher) = apply { this.dispatcher = dispatcher }

        fun encryption(encrypted: Boolean) = apply { this.encrypted = encrypted }

        fun build() = SimpleStore(this)
    }

}