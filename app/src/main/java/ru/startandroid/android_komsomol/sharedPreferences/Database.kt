package ru.startandroid.android_komsomol.sharedPreferences

import androidx.room.RoomDatabase

@androidx.room.Database(
        entities = [RoomDetailedItemModel::class],
        version = 1,
        exportSchema = false
)

abstract class Database : RoomDatabase() {
    abstract fun detailedItemDao(): DetailedItemDao

    companion object {
        const val DB_NAME = "database.db"
    }
}