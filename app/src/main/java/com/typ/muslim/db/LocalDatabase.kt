package com.typ.muslim.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.typ.muslim.features.khatma.dao.KhatmaDAO
import com.typ.muslim.features.khatma.models.Khatma

@Database(
    version = 1,
    entities = [
        Khatma::class
    ],
    exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun getKhatmaDao(): KhatmaDAO

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getInstance(context: Context): LocalDatabase? {
            if (INSTANCE == null) {
                synchronized(LocalDatabase::class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            LocalDatabase::class.java,
                            "LocalDatabase"
                        ).allowMainThreadQueries().build()
                    }
                }
            }
            return INSTANCE
        }

    }

}
