package io.github.rhymezxcode.simplestore

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideStore(
        @ApplicationContext context: Context
    ) = SimpleStore.Builder()
        .context(context = context)
        .encryption(encrypted = false)
        .build()


}
