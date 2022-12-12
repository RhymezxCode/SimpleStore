package io.github.rhymezxcode.simplestore

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.rhymezxcode.simplestore.example.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

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

        }
        binding.store2.setOnClickListener {
            lifecycleScope.launch {
                store.getType<DatastorePreference>().saveStringToStore(
                    "yes",
                    binding.editTextTextPersonName2.text.toString()
                )
            }

        }
        binding.store3.setOnClickListener {
            lifecycleScope.launch {
                store.getType<DatastorePreference>().saveStringToStore(
                    "no",
                    binding.editTextTextPersonName3.text.toString()
                )
            }

        }

        lifecycleScope.launchWhenCreated {

            binding.dataOneValue.text = store.getType<DatastorePreference>()
                .getStringFromStore("name").first()

            binding.dataTwoValue.text = store.getType<DatastorePreference>()
                .getStringFromStore("yes").first()

            binding.dataThreeValue.text = store.getType<DatastorePreference>()
                .getStringFromStore("no").first()

        }
    }

}
