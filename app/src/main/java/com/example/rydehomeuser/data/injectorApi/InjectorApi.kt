package com.app.passerby.data.injectorApi


import android.util.Log
import com.example.rydehomeuser.BuildConfig
import com.example.rydehomeuser.utils.GlobalHelper
import com.example.rydehomeuser.utils.SharedPrefUtil
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class InjectorApi {




    class ForbiddenInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
//            if (response.code() == 203) {
            //                SharedPrefUtil.getInstance().setLogin(false);
            //                App.tokenExpired();
            //                return null;
            //            } else {
            return chain.proceed(request)
            //            }
        }
    }



    companion object {

        var retrofitInstance : Retrofit? = null

        private fun provideRetrofit(baseUrl: String): Retrofit {


            if (retrofitInstance==null)
            {


                retrofitInstance=   Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(provideOkHttpClient())
                    .addConverterFactory(
                        GsonConverterFactory.create(
                            GsonBuilder()
                                .setLenient()
                                .create())).build()

                return retrofitInstance as Retrofit
            }
            return retrofitInstance as Retrofit

        }

        private fun provideOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(provideHttpLoggingInterceptor())
                .addInterceptor(provideHeaderInterceptor())
                .addInterceptor(ForbiddenInterceptor())
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(45, TimeUnit.SECONDS)
                .writeTimeout(45, TimeUnit.SECONDS)

                .build()
        }

        private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
            val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
                Log.d(
                    "Injector",
                    message
                )
            })
            httpLoggingInterceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            return httpLoggingInterceptor
        }

        fun provideApi(): InterfaceApi {
            return provideRetrofit(GlobalHelper.BASE_URL).create<InterfaceApi>(InterfaceApi::class.java!!)
        }

        private fun provideHeaderInterceptor(): Interceptor {
            return Interceptor { chain ->
              //  val helper = SharedPrefUtil.getInstance()
                val request: Request



                                if (!SharedPrefUtil.getInstance()?.getAuthToken().equals("") || SharedPrefUtil.getInstance()?.getAuthToken().equals("")) {
                                    request = chain.request().newBuilder()
                                        .header("authorization_key",SharedPrefUtil.getInstance()?.getAuthToken())
                                      //  .header("authorization_key", SharedPrefUtil.getInstance()?.getAuthToken())
                                           /* .header("Authorization", "Bearer " + helper.getAuthToken())
                                            .header("Accept", "application/json")
                                            .header("language", helper.getLanguage())*/
                                            .build()
                              } else {
                request = chain.request().newBuilder().build()
                               }
                chain.proceed(request)
                               }
            }
        }

}
