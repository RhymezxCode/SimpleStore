package io.github.rhymezxcode.simeplestore

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import io.github.rhymezxcode.simplepreference.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.M)
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val store = SimpleStore.Builder()
            .context(context = this)
            .storeName(name = "MyStore")
            .encryption(encrypted = false)
            .dispatcher(dispatcher = Default)
            .build()

        lifecycleScope.launch {
            store.sharedPreference()?.saveStringToStore("name", "jide")
        }

        lifecycleScope.launchWhenCreated {
            store.sharedPreference()?.getStringFromStore("name")
        }
    }


}
