package ru.startandroid.android_komsomol.sharedPreferences

class RoomDetailedItemCache(private val database: Database) {
    fun saveDetailedItemToDb(detailedItem: DetailedItemModel) {
        val itemToInsert = RoomDetailedItemModel(
                detailedItem.id,
                detailedItem.cityName,
                detailedItem.date,
                detailedItem.temp
        )
        database.detailedItemDao().insert(itemToInsert)
    }

    fun getDetailedItemsFromDb(detailedItemId: String): DetailedItemModel? {
        return database.detailedItemDao().getById(detailedItemId)?.mapToSimpleModel()
    }
}