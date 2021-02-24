package com.example.sunnyweather.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.ln

object SunnyWeatherNetwork {
    private val placeService = ServiceCreator.create(PlaceService::class.java)

    suspend fun searchPlaces(query:String) = placeService.searchPlaces(query).await()

    private suspend fun <T> Call<T>.await():T{//获得reponse
        return suspendCoroutine { continuation ->
            enqueue(object :Callback<T>{
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()//callback回调返回值 赋值给body并返回
                    if (body!=null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body id null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

    private val weatherService = ServiceCreator.create<WeatherService>()

    suspend fun getDailyWeather(lng:String,lat:String) = weatherService.getDailyWeather(lng,lat).await()

    suspend fun getRealtimeWeather(lng: String,lat: String)= weatherService.getRealtimeWeather(lng,lat).await()
}