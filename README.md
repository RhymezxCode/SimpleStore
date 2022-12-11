<div align="center">
<h1>SimpleStore Android Library</h1>

<a href="https://android-arsenal.com/api?level=23" target="blank">
    <img src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat" alt="SimpleStore Android Library least API level" />
</a>
<a href="https://jitpack.io/#RhymezxCode/SimpleStore" target="blank">
    <img src="https://jitpack.io/v/RhymezxCode/SimpleStore.svg" alt="SimpleStore Android Library on jitpack.io" />
</a>
<a href="https://github.com/RhymezxCode/SimpleStore/blob/main/LICENSE" target="blank">
    <img src="https://img.shields.io/github/license/RhymezxCode/SimpleStore" alt="SimpleStore Android Library License." />
</a>
<a href="https://github.com/RhymezxCode/SimpleStore/stargazers" target="blank">
    <img src="https://img.shields.io/github/stars/RhymezxCode/SimpleStore" alt="SimpleStore Android Library Stars"/>
</a>
<a href="https://github.com/RhymezxCode/SimpleStore/fork" target="blank">
    <img src="https://img.shields.io/github/forks/RhymezxCode/SimpleStore" alt="SimpleStore Android Library Forks"/>
</a>
<a href="https://github.com/RhymezxCode/SimpleStore/issues" target="blank">
    <img src="https://img.shields.io/github/issues/RhymezxCode/SimpleStore" alt="SimpleStore Android Library Issues"/>
</a>
<a href="https://github.com/RhymezxCode/SimpleStore/commits?author=RhymezxCode" target="blank">
    <img src="https://img.shields.io/github/last-commit/RhymezxCode/SimpleStore" alt="SimpleStore Android Library Issues"/>
</a>
<a href="https://bettercodehub.com/edge/badge/RhymezxCode/SimpleStore?branch=main" target="blank">
  <img src='https://bettercodehub.com/edge/badge/RhymezxCode/SimpleStore?branch=main'>
</a>
</div>
<br />

## SimpleStore Android Library
A library to create either a shared preference or data store. it has the ability to encrypt on a go, by just signifying on the builder class.

### 1. Adding SimpleStore to your project

* Include jitpack in your root `settings.gradle` file.

```gradle
pluginManagement {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

* And add it's dependency to your app level `build.gradle` file:

```gradle
dependencies {
    implementation 'com.github.RhymezxCode:SimpleStore:1.0.3'
}
```

#### Sync your project, and :scream: boom :fire: you have added SimpleStore successfully. :exclamation:

### 2. Usage

##  Using datastore
* First initialize the builder class:

```kt
     val store = SimpleStore.Builder()
        .context(context = this)
        .storeName("AnyName of your choice")
        .encryption(encrypted = false)
        .build()
```
##  Using encrypted datastore
* First initialize the builder class:

```kt
     val store = SimpleStore.Builder()
        .context(context = this)
        .storeName("AnyName of your choice")
        .encryption(encrypted = true)
        .build()
```

* To save a string:
```kt
       lifecycleScope.launch {
                store.getType<DatastorePreference>().saveStringToStore(
                    "name",
                    "My name"
                )
            }
```

* To save a boolean:
```kt
       lifecycleScope.launch {
                store.getType<DatastorePreference>().saveBooleanToStore(
                    "default",
                    true
                )
            }
```

* Get a string that you saved:
```kt
        lifecycleScope.launchWhenCreated {

                store.getType<DatastorePreference>()
                    .getStringFromStore("name").collectLatest{
                        binding.sharedPreferenceValue.text = it
                    }
            }
```

* Get a boolean that you saved:
```kt
       val default = false
       lifecycleScope.launchWhenCreated {
                store.getType<DatastorePreference>()
                    .getBooleanFromStore("default").collectLatest{
                        default = it
                    }
            }
```

##  Using shared preference
* First initialize the builder class:

```kt
     val store = SimpleStore.Builder()
        .context(context = this)
        .storeName("AnyName of your choice")
        .encryption(encrypted = false)
        .build()
```
##  Using encrypted shared preference
* First initialize the builder class:

```kt
     val store = SimpleStore.Builder()
        .context(context = this)
        .storeName("AnyName of your choice")
        .encryption(encrypted = true)
        .build()
```

* To save a string:
```kt
       lifecycleScope.launch {
                store.getType<SharedPreference>().saveStringToStore(
                    "name",
                    "My name"
                )
            }
```

* To save a boolean:
```kt
       lifecycleScope.launch {
                store.getType<SharedPreference>().saveBooleanToStore(
                    "default",
                    true
                )
            }
```

* Get a string that you saved:
```kt
        lifecycleScope.launchWhenCreated {
          binding.sharedPreferenceValue.text = store.getType<SharedPreference>()
                    .getStringFromStore("name")
            }
```

* Get a boolean that you saved:
```kt
       lifecycleScope.launchWhenCreated {
        val default = store.getType<DatastorePreference>()
                    .getBooleanFromStore("default")
            }
```
    
### 3. You can also inject SimpleStore, and use it everywhere in your app with Hilt :syringe: :

* Create an object for the NetworkStateModule in your di package:

```kt
@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideStore(
        @ApplicationContext context: Context
    ) = SimpleStore.Builder()
        .context(context = context)
        .storeName("AnyName")
        .encryption(encrypted = true)
        .build()

}
```

* Declare the variable in your class either a fragment or activity, it works in both:

```kt
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


        binding.store.setOnClickListener {
            lifecycleScope.launch {
                store.getType<DatastorePreference>().saveStringToStore(
                    "name",
                    binding.editTextTextPersonName.text.toString()
                )
            }

            lifecycleScope.launchWhenCreated {

                store.getType<DatastorePreference>()
                    .getStringFromStore("name").collectLatest{
                        binding.sharedPreferenceValue.text = it
                    }
            }
        }
    }

}
 ```

:pushpin: Please, feel free to give me a star :star2:, I also love sparkles :sparkles: :relaxed:
<div align="center">
    <sub>Developed with :sparkling_heart: by
        <a href="https://github.com/RhymezxCode">Awodire Babajide Samuel</a>
    </sub>
</div>

