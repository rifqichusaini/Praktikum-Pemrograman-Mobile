package com.kiki.p5

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BarangDao {

    @Insert
    fun insert(barang: Barang)

    @Query("SELECT * FROM barang")
    fun getAllBarang(): LiveData<List<Barang>>

    @Query("SELECT * FROM barang WHERE id = :id LIMIT 1")
    fun getBarangById(id: Int): Barang?

}
