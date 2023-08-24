package com.example.newsapp.utility

import android.content.Context
import androidx.datastore.preferences.PreferencesProto
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

private const val DATA_STORE_NAME = "APP_NAME_DATA_STORE_NAME"

class PreferenceStore @Inject constructor(@ApplicationContext val context: Context) {

    private val Context.dataStore by preferencesDataStore(name = DATA_STORE_NAME)

    /*private fun sharedPreferencesMigration(context: Context): List<SharedPreferencesMigration<Preferences>> {
        return listOf(SharedPreferencesMigration(context, context.getString(R.string.app_name), setOf(SharedPref.labOrder, SharedPref.touchId, SharedPref.loginPin)))
    } */


    /**** Start Saving Functions*/
    fun saveString(key: String, value: String) {
        getScope {
            val dataStoreKey = stringPreferencesKey(key)
            context.dataStore.edit { settings ->
                settings[dataStoreKey] = value
            }
        }
    }

    suspend fun saveSuspendString(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        context.dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }


    fun saveInt(key: String, value: Int) {
        getScope {
            val dataStoreKey = intPreferencesKey(key)
            context.dataStore.edit { settings ->
                settings[dataStoreKey] = value
            }
        }
    }

    fun saveLong(key: String, value: Long) {
        getScope {
            val dataStoreKey = longPreferencesKey(key)
            context.dataStore.edit { settings ->
                settings[dataStoreKey] = value
            }
        }

    }

    fun saveFloat(key: String, value: Float) {
        getScope {
            val dataStoreKey = floatPreferencesKey(key)
            context.dataStore.edit { settings ->
                settings[dataStoreKey] = value
            }
        }
    }

    fun saveDouble(key: String, value: Double) {
        getScope {
            val dataStoreKey = doublePreferencesKey(key)
            context.dataStore.edit { settings ->
                settings[dataStoreKey] = value
            }
        }

    }

    fun saveBoolean(key: String, value: Boolean) {
        getScope {
            val dataStoreKey = booleanPreferencesKey(key)
            context.dataStore.edit { settings ->
                settings[dataStoreKey] = value
            }
        }
    }

    /**** End Saving Functions*/

    fun <T : Any> delete(
        key: String,
        type: Class<T>,
        coroutineContext: CoroutineContext = Dispatchers.Main,
        deleteStatus: (T?) -> Unit = {}
    ) {
        getScope {
            val preferences = when (type) {
                Int::class.java -> intPreferencesKey(key)
                Long::class.java -> longPreferencesKey(key)
                Float::class.java -> floatPreferencesKey(key)
                Double::class.java -> doublePreferencesKey(key)
                Boolean::class.java -> booleanPreferencesKey(key)
                String::class.java -> stringPreferencesKey(key)
                PreferencesProto.StringSet::class -> stringSetPreferencesKey(key)
                else -> null
            }
            var delete = Any()
            context.dataStore.edit { settings ->
                preferences?.let {
                    delete = settings.remove(it)
                }
            }
            withContext(coroutineContext) {
                deleteStatus(delete as? T?)
            }
        }
    }

    fun <T> contains(
        key: String,
        type: Class<T>,
        coroutineContext: CoroutineContext = Dispatchers.Main,
        res: (Boolean) -> Unit
    ) {
        getScope {
            val preferences = when (type) {
                Int::class.java -> intPreferencesKey(key)
                Long::class.java -> longPreferencesKey(key)
                Float::class.java -> floatPreferencesKey(key)
                Double::class.java -> doublePreferencesKey(key)
                Boolean::class.java -> booleanPreferencesKey(key)
                String::class.java -> stringPreferencesKey(key)
                PreferencesProto.StringSet::class -> stringSetPreferencesKey(key)
                else -> null
            }

            var contains = false
            context.dataStore.edit {
                contains = it.contains(preferences!!)
            }

            withContext(coroutineContext) {
                res(contains)
            }
        }
    }

    /**** Start Get Functions*/

    suspend fun getSuspendString(key: String): String {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        return (preferences[dataStoreKey]).orEmpty()
    }

    fun getString(
        key: String,
        coroutineContext: CoroutineContext = Dispatchers.Main,
        res: suspend (String?) -> Unit
    ) {
        getScope {
            val dataStoreKey = stringPreferencesKey(key)
            val preferences = context.dataStore.data.first()
            withContext(coroutineContext) {
                res(preferences[dataStoreKey])
            }
        }
    }

    fun getStringArgs(
        vararg keys: String,
        coroutineContext: CoroutineContext = Dispatchers.Main,
        res: (List<String?>) -> Unit
    ) {
        getScope {
            val list: List<Deferred<String?>> = keys.map {
                getAsyncScope {
                    val dataStoreKey = stringPreferencesKey(it)
                    val preferences = context.dataStore.data.first()
                    preferences[dataStoreKey]
                }
            }

            withContext(coroutineContext) {
                res(list.awaitAll())
            }
        }
    }

    fun getInt(
        key: String,
        coroutineContext: CoroutineContext = Dispatchers.Main,
        res: (Int?) -> Unit
    ) {
        getScope {
            val dataStoreKey = intPreferencesKey(key)
            val preferences = context.dataStore.data.first()
            withContext(coroutineContext) {
                res(preferences[dataStoreKey])
            }
        }

    }

    suspend fun getSuspendInt(key: String): Int? {
        val dataStoreKey = intPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        return preferences[dataStoreKey]

    }

    fun getLong(
        key: String,
        coroutineContext: CoroutineContext = Dispatchers.Main,
        res: (Long?) -> Unit
    ) {
        getScope {
            val dataStoreKey = longPreferencesKey(key)
            val preferences = context.dataStore.data.first()
            withContext(coroutineContext) {
                res(preferences[dataStoreKey])
            }
        }
    }

    fun getFloat(
        key: String,
        coroutineContext: CoroutineContext = Dispatchers.Main,
        res: (Float?) -> Unit
    ) {
        getScope {
            val dataStoreKey = floatPreferencesKey(key)
            val preferences = context.dataStore.data.first()
            withContext(coroutineContext) {
                res(preferences[dataStoreKey])
            }
        }
    }

    fun getDouble(
        key: String,
        coroutineContext: CoroutineContext = Dispatchers.Main,
        res: (Double?) -> Unit
    ) {
        getScope {
            val dataStoreKey = doublePreferencesKey(key)
            val preferences = context.dataStore.data.first()
            withContext(coroutineContext) {
                res(preferences[dataStoreKey])
            }
        }
    }

    fun getBoolean(
        key: String,
        coroutineContext: CoroutineContext = Dispatchers.Main,
        res: (Boolean?) -> Unit
    ) {
        getScope {
            val dataStoreKey = booleanPreferencesKey(key)
            val preferences = context.dataStore.data.first()
            withContext(coroutineContext) {
                res(preferences[dataStoreKey])
            }
        }
    }

    private fun getScope(scope: suspend () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            scope()
        }
    }

    private fun <T> getAsyncScope(scope: suspend () -> T): Deferred<T> {
        return CoroutineScope(Dispatchers.IO).async {
            scope()
        }
    }

    suspend fun getSetStringList(key: String): Set<String>? {
        val dataStoreKey = stringSetPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        return preferences[dataStoreKey]
    }

    suspend fun setSetStringList(key: String, value: Set<String>) {
        val dataStoreKey = stringSetPreferencesKey(key)
        context.dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    /**** End Get Functions*/

    companion object {
        //Write keys here
        const val PIN_COUNT = "PIN_COUNT"
        const val LOGIN_PIN = "loginPin"
    }
}