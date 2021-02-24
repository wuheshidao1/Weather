package com.example.sunnyweather.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.sunnyweather.logic.Repository
import com.example.sunnyweather.logic.model.Place

class PlaceViewModel:ViewModel() {
    private val searchLiveData = MutableLiveData<String>()

    val placeList = ArrayList<Place>()

    val placeLiveData = Transformations.switchMap(searchLiveData){//通过观察
        query->Repository.searchPlaces(query)//返回liveData
    }

    fun searchPlaces(query:String) {
        searchLiveData.value = query
    }


    //因为仓库层中这几个接口的内部没有开启线程，因此也不用借助livedata对象来观察数据变化 直接调用就行
    //object place sharepreference 在进行一次封装 因为几个接口和业务逻辑和placeViewModel相关
    fun savePlace(place: Place) = Repository.savePlace(place)
    fun getSavedPlace() = Repository.getSavedPlace()
    fun isPlaceSaved() = Repository.isPlaceSaved()
}