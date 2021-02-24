package com.example.sunnyweather.logic


import androidx.lifecycle.liveData
import com.example.sunnyweather.logic.dao.PlaceDao
import com.example.sunnyweather.logic.model.Place
import com.example.sunnyweather.logic.model.Weather
import com.example.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.Exception
import java.lang.RuntimeException


object Repository {
    fun searchPlaces(query:String) = liveData(Dispatchers.IO){
        val result = try {
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)//获得<data class>placeResponse
            if (placeResponse.status=="ok"){
                val places = placeResponse.places//获得<data class>placeResponse 封装成<List<place>> places对象
                Result.success(places)
            }else{
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        }catch (e:Exception){
            Result.failure<List<Place>>(e)
        }
        emit(result)//来通知数据变化
    }

    fun refreshWeather(lng:String,lat:String) = liveData(Dispatchers.IO){
        val result = try {
            coroutineScope {
                val deferredRealtime = async {
                    SunnyWeatherNetwork.getRealtimeWeather(lng,lat)
                }
                val deferredDaily = async {
                    SunnyWeatherNetwork.getDailyWeather(lng,lat)
                }
                val realtimeResponse = deferredRealtime.await()//<network>:获得realtimeResponse对象 network
                val dailyResponse = deferredDaily.await()
                if (realtimeResponse.status=="ok"&&dailyResponse.status=="ok"){//将realtime daily对象取出并封装
                    val weather = Weather(realtimeResponse.result.realtime,dailyResponse.result.daily)
                    Result.success(weather)
                }else{
                    Result.failure(
                        RuntimeException("realtime response status is ${realtimeResponse.status}"+
                        "daily response status is ${dailyResponse.status}")
                    )
                }
            }
        }catch (e:Exception){
            Result.failure<Weather>(e)
        }
        emit(result)
    }

    //PlaceDao 在仓库层进行一次封装
    //因为仓库层中这几个接口的内部没有开启线程，因此也不用借助livedata对象来观察数据变化
    fun savePlace(place: Place) = PlaceDao.savePlace(place)
    fun getSavedPlace() = PlaceDao.getSavePlace()
    fun isPlaceSaved()  = PlaceDao.isPlaceSaved()
}