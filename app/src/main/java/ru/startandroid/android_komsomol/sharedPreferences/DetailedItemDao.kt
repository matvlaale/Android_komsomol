package ru.startandroid.android_komsomol.sharedPreferences

import androidx.room.*

@Dao
interface DetailedItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(shortItem: RoomDetailedItemModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg shortItem: RoomDetailedItemModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(shortItems: List<RoomDetailedItemModel>)

    @Update
    fun update(shortItem: RoomDetailedItemModel)

    @Update
    fun update(vararg shortItem: RoomDetailedItemModel)

    @Update
    fun update(shortItems: List<RoomDetailedItemModel>)

    @Delete
    fun delete(shortItem: RoomDetailedItemModel)

    @Delete
    fun delete(vararg shortItem: RoomDetailedItemModel)

    @Delete
    fun delete(shortItems: List<RoomDetailedItemModel>)

    @Query("SELECT * FROM RoomDetailedItemModel")
    fun getAll(): List<RoomDetailedItemModel>

    @Query("SELECT * FROM RoomDetailedItemModel WHERE id = :id LIMIT 1")
    fun getById(id: String): RoomDetailedItemModel?
}