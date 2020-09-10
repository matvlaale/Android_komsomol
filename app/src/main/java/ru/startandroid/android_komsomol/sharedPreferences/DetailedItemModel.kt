package ru.startandroid.android_komsomol.sharedPreferences

import com.google.gson.annotations.SerializedName

open class DetailedItemModel {
    @SerializedName("id")
    var id = ""

    @SerializedName("cityName")
    var cityName = ""

    @SerializedName("date")
    var date = ""

    @SerializedName("temp")
    var temp = ""
}