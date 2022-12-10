package io.github.rhymezxcode.simplestore

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import io.github.rhymezxcode.simplestore.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.M)
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        //share preference builder
        val storeOne = SimpleStore.Builder()
            .context(context = this)
            .storeName(name = "MyStorePreference")
            .encryption(encrypted = false)
            .dispatcher(dispatcher = Default)
            .build()

        //Encrypted share preference builder
        val storeTwo = SimpleStore.Builder()
            .context(context = this)
            .storeName(name = "MyStoreEncryptedPreference")
            .encryption(encrypted = true)
            .dispatcher(dispatcher = Default)
            .build()

        //datastore preference builder
        val storeThree = SimpleStore.Builder()
            .context(context = this)
            .storeName(name = "MyDataStorePreference")
            .encryption(encrypted = false)
            .build()

        //datastore preference builder
        val storeFour = SimpleStore.Builder()
            .context(context = this)
            .storeName(name = "MyDataStoreEncryptedPreference")
            .encryption(encrypted = true)
            .build()

        binding.store.setOnClickListener {
            lifecycleScope.launch {
                storeOne.sharedPreference()
                    ?.saveStringToStore("name",
                        binding.editTextTextPersonName.text.toString())
            }

            lifecycleScope.launchWhenCreated {
                binding.sharedPreferenceValue.text =
                    storeOne.sharedPreference()?.getStringFromStore("name")
            }

            lifecycleScope.launch {
                storeTwo.sharedPreference()
                    ?.saveStringToStore("name",
                        binding.editTextTextPersonName.text.toString())
            }

            lifecycleScope.launchWhenCreated {
                binding.sharedPreferenceEncryptedValue.text =
                    storeOne.sharedPreference()?.getStringFromStore("name")
            }

            lifecycleScope.launch {
                storeThree.datastorePreference()
                    ?.saveStringToStore("name",
                        binding.editTextTextPersonName.text.toString())
            }

            lifecycleScope.launchWhenCreated {
                storeOne.datastorePreference()?.
                getStringFromStore("name")?.collectLatest {
                    binding.datastorePreferenceValue.text = it
                }
            }

            lifecycleScope.launch {
                storeFour.datastorePreference()
                    ?.saveStringToStore("name",
                        binding.editTextTextPersonName.text.toString())
            }

            lifecycleScope.launchWhenCreated {
                storeFour.datastorePreference()?.
                getStringFromStore("name")?.collectLatest {
                    binding.datastorePreferencEncryptedValue.text = it
                }
            }
        }


    }


}
