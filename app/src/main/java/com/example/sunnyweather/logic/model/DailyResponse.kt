package com.example.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class DailyResponse(val status:String,val result: Result){
    data class Result(val daily:Daily)
    data class Daily(val temperature:List<Temperature>,val skycon:List<Skycon>
    ,@SerializedName("life_index")val lifeIndex:LifeIndex)

    data class Temperature(val max:Float,val min:Float)
    data class Skycon(val value:String,val date: Date)//date 不是 data

    data class LifeIndex(val coldRisk:List<LifeDescripton>,val carWashing:List<LifeDescripton>,
                         val ultraviolet:List<LifeDescripton>,val dressing:List<LifeDescripton>)

    data class LifeDescripton(val desc:String)

//    data class ColdRisk(val desc:String)
//    data class CarWashing(val desc:String)
//    data class Ultraviolet(val desc:String)
//    data class Dressing(val desc: String)
}