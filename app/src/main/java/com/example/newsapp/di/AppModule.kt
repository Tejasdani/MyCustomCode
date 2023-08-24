package com.example.newsapp.di

import android.content.Context
import com.example.newsapp.BuildConfig
import com.example.newsapp.api.ApiService
import com.example.newsapp.api.DashboardService
import com.example.newsapp.constants.Constants
import com.example.newsapp.db.EventDatabase
import com.example.newsapp.db.dao.EventsDao
import com.example.newsapp.utility.PreferenceStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.CertificateException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl() = Constants.BASE_URL

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(
                chain: Array<java.security.cert.X509Certificate>,
                authType: String
            ) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(
                chain: Array<java.security.cert.X509Certificate>,
                authType: String
            ) {
            }

            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                return arrayOf()
            }
        })

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        val sslSocketFactory = sslContext.socketFactory
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder().addInterceptor(loggingInterceptor)
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val requestBuilder: Request.Builder = chain.request().newBuilder()
                    requestBuilder.header("Content-Type", "application/json")
//                    requestBuilder.header("Authorization", "Bearer " + EasyPref(BaseApplication.appContext).token)
                    return chain.proceed(requestBuilder.build())
                }

            }).connectTimeout(3, TimeUnit.MINUTES).writeTimeout(3, TimeUnit.MINUTES)
            .readTimeout(3, TimeUnit.MINUTES)
            .protocols(Collections.singletonList(Protocol.HTTP_1_1))
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { hostname, session -> true }
            .build()
    } else {
        // SSL Pinning
        val hostname = "example.com"
        val sha256Pin = "sha256/PIN_OF_THE_CERTIFICATE"

        val certificatePinner: CertificatePinner = CertificatePinner.Builder()
            .add(hostname, sha256Pin)
            .build()

        OkHttpClient.Builder().addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val requestBuilder: Request.Builder = chain.request().newBuilder()
                requestBuilder.header("Authorization", "Bearer ${Constants.API_TOKEN}")
                return chain.proceed(requestBuilder.build())
            }

        }).certificatePinner(certificatePinner = certificatePinner)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit =
        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL)
            .client(okHttpClient).build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiLoginService(retrofit: Retrofit): DashboardService {
        return retrofit.create(DashboardService::class.java)
    }

    @Provides
    @Singleton
    fun dataStoreManager(@ApplicationContext appContext: Context): PreferenceStore {
        return PreferenceStore(appContext)
    }

    @Provides
    @Singleton
    fun provideDatabaseHelper(@ApplicationContext context: Context): EventDatabase {
        return EventDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideEventRepo(@ApplicationContext context: Context): EventsDao {
        return EventDatabase.getDatabase(context).getEventDao()
    }

}