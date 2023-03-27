package com.typ.muslim.features.khatma.dao

import androidx.room.*
import com.typ.muslim.features.khatma.models.Khatma

@Dao
interface KhatmaDAO {

    @Insert(entity = Khatma::class, onConflict = OnConflictStrategy.IGNORE)
    fun createKhatma(khatma: Khatma)

    @Query("SELECT DISTINCT * FROM Khatma ORDER BY startedIn DESC LIMIT 1")
    fun getLastActiveKhatma(): Khatma

    @Query("SELECT DISTINCT * FROM Khatma where id = :khatmaId")
    fun getKhatmaById(khatmaId: String): Khatma

    @Query("SELECT DISTINCT * FROM Khatma")
    fun getAll(): Array<Khatma>

    @Delete(entity = Khatma::class)
    fun deleteKhatma(khatma: Khatma)

    @Update(entity = Khatma::class, onConflict = OnConflictStrategy.IGNORE)
    fun updateKhatma(khatma: Khatma)

    @Query("SELECT DISTINCT * FROM Khatma WHERE startedIn BETWEEN :start AND :end")
    fun getWithinRange(start: Long, end: Long): Array<Khatma>

}