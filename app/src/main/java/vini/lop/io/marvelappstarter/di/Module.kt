package vini.lop.io.marvelappstarter.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import vini.lop.io.marvelappstarter.data.local.MarvelDatabase
import vini.lop.io.marvelappstarter.data.remote.ServiceApi
import vini.lop.io.marvelappstarter.util.Constants.BASE_URL
import vini.lop.io.marvelappstarter.util.Constants.DATABASE_NAME
import vini.lop.io.marvelappstarter.util.Constants.PARAM_API_KEY
import vini.lop.io.marvelappstarter.util.Constants.PARAM_HASH
import vini.lop.io.marvelappstarter.util.Constants.PARAM_TS
import vini.lop.io.marvelappstarter.util.Constants.PRIVATE_KEY
import vini.lop.io.marvelappstarter.util.Constants.PUBLIC_KEY
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient()
            .newBuilder()
            .addInterceptor { chain ->
                val currentTimestamp = System.currentTimeMillis()

                val newUrl = chain
                    .request()
                    .url
                    .newBuilder()
                    .addQueryParameter(
                        PARAM_TS,
                        currentTimestamp.toString()
                    )
                    .addQueryParameter(
                        PARAM_API_KEY,
                        PUBLIC_KEY
                    )
                    .addQueryParameter(
                        PARAM_HASH,
                        provideToMd5Hash(encrypted = "$currentTimestamp$PRIVATE_KEY$PUBLIC_KEY")
                    )
                    .build()

                val newRequest = chain
                    .request()
                    .newBuilder()
                    .url(newUrl)
                    .build()

                chain.proceed(newRequest)
            }
            .addInterceptor(logging)
            .build()
    }

    private fun provideToMd5Hash(encrypted: String): String {
        var pass = encrypted
        var encryptedString: String? = null
        val md5: MessageDigest

        try {
            md5 = MessageDigest.getInstance("MD5")
            md5.update(pass.toByteArray(), 0, pass.length)
            pass = BigInteger(1, md5.digest()).toString(16)
            while (pass.length < 32) {
                pass = "0$pass"
            }
            encryptedString = pass
        } catch (e1: NoSuchAlgorithmException) {
            e1.printStackTrace()
        }

        Timber.d("hash -> $encryptedString")

        return encryptedString ?: ""
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideServiceApi(retrofit: Retrofit): ServiceApi {
        return retrofit.create(ServiceApi::class.java)
    }

    @Singleton
    @Provides
    fun providerMarvelDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        MarvelDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun providerMarvelDao(marvelDatabase: MarvelDatabase) = marvelDatabase.marvelDao()
}