package com.base.hilt.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.base.hilt.utils.Constants.DataStore.SECURED_DATA
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KClass


/**
 * Created By Richa Shah on 2/08/2022 at 2:00 PM
 * Data store Extension Class
 * @param  dataStore Dats Store Object
 * @param  security SecurityUtil for adding security on Dats Store Object
 * @property secureMap() Method to  secureMap extension method fetches encrypted data from DataStore, decrypts it and deserializes it into respective data type
 * @property secureEdit() Method to serializes data, encrypts it and stores it in DataStore
 * @property catchAndHandleError() Method to Catch and handle Exceptions automatically.
 * NOTE: always use these predefined methods for usage of data store in project
 */
@Singleton
open class DataStoreUtil @Inject constructor(
    val dataStore: DataStore<Preferences>,
    val security: SecurityUtil
) {
    val securityKeyAlias = "data-store"
    val bytesToStringSeparator = "|"

 /*   fun getData() = dataStore.data.catchAndHandleError()
        .map { preferences ->
            preferences[DATA] ?: "empty value"
        }

    suspend fun setData(value: String) {
        dataStore.edit {
            it[DATA] = value
        }
    }
*/
    fun getSecuredData() = dataStore.data.catchAndHandleError()
        .secureMap<String> { preferences ->
            preferences[SECURED_DATA] ?: "empty value"
        }.catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                DebugLog.e("empty ==> " + exception.message.toString())
                emit("empty value")
            } else {
                emit("empty value")
                DebugLog.e("empty ==> " + exception.message.toString())
            }
        }


    @OptIn(ExperimentalSerializationApi::class)
    suspend fun setSecuredData(value: String) {
        dataStore.secureEdit(value) { prefs, encryptedValue ->
            prefs[SECURED_DATA] = encryptedValue
        }
    }

    suspend fun hasKey(key: Preferences.Key<*>) = dataStore.edit { it.contains(key) }

    suspend fun clearDataStore() {
        dataStore.edit {
            it.clear()
        }
    }

    /*

    */
    @OptIn(ExperimentalSerializationApi::class)
    inline fun <reified T> Flow<Preferences>.secureMap(crossinline fetchValue: (value: Preferences) -> String): Flow<T?> {
        return map {
            val decryptedValue = security.decryptData(
                securityKeyAlias,
                fetchValue(it).split(bytesToStringSeparator).map { it.toByte() }.toByteArray()
            )
            val json = Json { encodeDefaults = true }
            json.decodeFromString(decryptedValue)
        }
    }


    @ExperimentalSerializationApi
    suspend inline fun <reified T> DataStore<Preferences>.secureEdit(
        value: T,
        crossinline editStore: (MutablePreferences, String) -> Unit
    ) {
        edit {
            val encryptedValue = security.encryptData(securityKeyAlias, Json.encodeToString(value))
            editStore.invoke(it, encryptedValue.joinToString(bytesToStringSeparator))
        }
    }

    suspend inline fun <reified T : Any> retrieveValue(
        key: String,
        clazz: KClass<T>
    ): Flow<T?> {
        return dataStore.data
            .catchAndHandleError()
            // Get our show completed value, returns default value if not set:
            .map { preferences ->
                when (clazz) {
                    Int::class -> preferences[intPreferencesKey(key)] ?: 0
                    Boolean::class -> preferences[booleanPreferencesKey(key)] ?: false
                    Long::class -> preferences[longPreferencesKey(key)] ?: 0L
                    String::class -> preferences[stringPreferencesKey(key)] ?: ""
                    Double::class -> preferences[doublePreferencesKey(key)] ?: 0.0
                    Float::class -> preferences[floatPreferencesKey(key)] ?: 0f
                    else -> preferences[booleanPreferencesKey(key)] ?: false
                } as T
            }
    }

    suspend inline fun <reified T : Any> retrieveValue(
        key: String,
        clazz: KClass<T>,
        crossinline callback: (T) -> Unit
    ) {
        retrieveValue(key, clazz).collect { value ->
            if (value != null) {
                callback(value)
            }
        }
    }

    open suspend fun addValue(
        key: String,
        value: Any
    ) {
        dataStore.edit { settings ->
            when (value) {
                is Int -> settings[intPreferencesKey(key)] = value
                is Boolean -> settings[booleanPreferencesKey(key)] = value
                is Long -> settings[longPreferencesKey(key)] = value
                is String -> settings[stringPreferencesKey(key)] = value
                is Double -> settings[doublePreferencesKey(key)] = value
                is Float -> settings[floatPreferencesKey(key)] = value
            }
        }
    }

    /*
        catch and handle IOExceptions automatically.
    */
    fun Flow<Preferences>.catchAndHandleError(): Flow<Preferences> {
        this.catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                DebugLog.e("catchAndHandleError ==> " + exception.message.toString())
                emit(emptyPreferences())
            } else {
                DebugLog.e("catchAndHandleError ==> " + exception.message.toString())
                throw exception
            }
        }
        return this@catchAndHandleError
    }
}
