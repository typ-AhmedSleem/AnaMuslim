package com.typ.muslim.features.khatma

import android.content.Context
import com.typ.muslim.db.LocalDatabase
import com.typ.muslim.features.khatma.interfaces.KhatmaManagerCallback
import com.typ.muslim.features.khatma.models.Khatma
import com.typ.muslim.models.Timestamp
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import java.util.*


/**
 * Holds all necessary code that manages the process of khatma management
 */
class KhatmaManager private constructor(
    private val context: Context,
    val khatma: Khatma,
    val callback: KhatmaManagerCallback
) {

    init {
        callback.onPrepareManager()
        LocalDatabase.getInstance(context)
        if (khatma.isActive) callback.onManageKhatma(khatma)
        else callback.onFinishKhatma()
    }

    @OptIn(InternalCoroutinesApi::class)
    private var db: LocalDatabase? = null
        get() {
            if (field == null) {
                synchronized(KhatmaManager::class.java) {
                    if (field == null) {
                        field = LocalDatabase.getInstance(context)
                    }
                }
            }
            return field
        }
        set(value) {
            throw UnsupportedOperationException("Can't change db runtime instance because it's a singleton instance.")
        }

    /**
     * Deletes this khatma from DB
     *
     * @return {@code true} if deleted successfully. {@code false} if failed.
     */
    fun delete() {
        TODO("Not yet implemented.")
    }

    /**
     * Mark the current werd as done, calculate next werd then refresh Khatma runtime
     */
    fun saveProgress() {
        khatma.saveProgress() // Save progress.
        val nextWerd = khatma.nextWerd // Calculate next werd (if available).
        if (nextWerd != null) callback.onProgressUpdated(nextWerd) // Notify UI about progress update.
        else callback.onFinishKhatma() // Khatma has finished.
    }

    companion object {

        /**
         * Creates a new KhatmaManager instance and put given khatma under management.
         *
         * @param khatma Khatma to put under management.
         * @param callback Callback to async send updates to UI.
         */
        fun manageKhatma(context: Context, khatma: Khatma, callback: KhatmaManagerCallback) {
            KhatmaManager(context, khatma, callback)
        }

        /**
         * Creates new khatma in local database and add it as latest created one.
         *
         * @param khatma The Khatma model to be added in local database.
         */
        fun createKhatma(context: Context, khatma: Khatma) {
            TODO("Not yet implemented.")
        }

        /**
         * @return All of [Khatma] user has started since he started using app to now.
         */
        fun getAllKhatmas(context: Context): List<Khatma> {
            TODO("Not yet implemented.")
        }

        /**
         * Query the DB searching for any khatma its id matches given [id].
         *
         * @param id Id of khatma to search lookup in DB.
         *
         * @return [Khatma] data model returned from query or `null` if not found.
         */
        fun getKhatmaById(id: String): Khatma? {
            TODO("Not yet implemented.")
        }

        /**
         *
         */
        fun getKhatmasAtRange(start: Timestamp, end: Timestamp): Array<Khatma> {
            val range = start.toMillis()..end.toMillis()
            TODO("Not yet implemented.")
        }

        /**
         * @return The most recent active khatma found on DB.
         */
        fun getLastActiveKhatma() {
            TODO("Not yet implemented.")
        }

    }

}