package com.base.hilt.di

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.base.hilt.BuildConfig
import com.base.hilt.utils.MyPreference
import com.base.hilt.utils.PrefKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        preferencesDataStore(name = BuildConfig.APPLICATION_ID/*, produceMigrations = {
            listOf(SharedPreferencesMigration(context,  PrefKey.PREFERENCE_NAME))
        }*/).getValue(context, String::javaClass)

    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        try {
            // this is equivalent to using deprecated MasterKeys.AES256_GCM_SPEC
            val spec = KeyGenParameterSpec.Builder(
                MasterKey.DEFAULT_MASTER_KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE)
                .build()
            val masterKey = MasterKey.Builder(context)
                .setKeyGenParameterSpec(spec)
                .build()
            //Old Deprecated code removed
            /*     EncryptedSharedPreferences.create(
                     PrefKey.PREFERENCE_NAME,
                     MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                     context,
                     EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                     EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                 )*/

            EncryptedSharedPreferences.create(
                context,
                PrefKey.PREFERENCE_NAME,
                masterKey, // masterKey created above
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }


    @Singleton
    @Provides
    fun provideMyPreference(mSharedPreferences: SharedPreferences) =
        MyPreference(mSharedPreferences)

}