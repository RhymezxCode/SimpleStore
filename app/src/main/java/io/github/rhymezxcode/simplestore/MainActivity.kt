package io.github.rhymezxcode.simplestore

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.rhymezxcode.simplestore.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

//@RequiresApi(Build.VERSION_CODES.M)
@RequiresApi(Build.VERSION_CODES.M)
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var store: SimpleStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.store.setOnClickListener {
            lifecycleScope.launch {
                store.getType<DatastorePreference>().saveStringToStore(
                    "name",
                    binding.editTextTextPersonName.text.toString()
                )
            }

            lifecycleScope.launchWhenCreated {

                store.getType<DatastorePreference>()
                    .getStringFromStore("name")?.collectLatest{
                        binding.sharedPreferenceValue.text = it
                    }
            }
        }
    }

}
