package com.kiki.myapplication

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface WargaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(warga: Warga)

    @Update
    fun update(warga: Warga)

    @Delete
    fun delete(warga: Warga)

    @Query("SELECT * from warga ORDER BY id ASC")
    fun getAllWarga(): LiveData<List<Warga>>

    @Query("SELECT * FROM warga WHERE id = :wargaId")
    fun getWargaById(wargaId: Int): Warga
}