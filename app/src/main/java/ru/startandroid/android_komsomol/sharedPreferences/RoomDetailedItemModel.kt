package ru.startandroid.android_komsomol.sharedPreferences

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomDetailedItemModel(
        @PrimaryKey var id: String,
        var cityName: String,
        var date: String,
        var temp: String
) {
    fun mapToSimpleModel(): DetailedItemModel {
        val model = DetailedItemModel()
        model.id = id
        model.cityName = cityName
        model.date = date
        model.temp = temp

        return model
    }
}