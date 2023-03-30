package com.typ.muslim.features.khatma

import android.content.Context
import com.typ.muslim.db.LocalDatabase
import com.typ.muslim.features.khatma.interfaces.KhatmaManagerCallback
import com.typ.muslim.features.khatma.models.Khatma
import com.typ.muslim.models.Timestamp


/**
 * Holds all necessary code that manages the process of khatma management
 */
class KhatmaManager private constructor(
    private val context: Context,
    val khatma: Khatma?,
    val callback: KhatmaManagerCallback
) {

    private var managing = false
    val holdsKhatma: Boolean
        get() = khatma != null

    val holdsActiveKhatma: Boolean
        get() = khatma?.isActive == true

    fun putKhatmaUnderManagement() {
        if (!managing) {
            callback.onPrepareManager()
            if (holdsKhatma) {
                // NOTE: khatma will always be non-null in this scope
                if (holdsActiveKhatma) callback.onManageKhatma(khatma!!)
                else callback.onFinishKhatma()
            } else callback.onHaveNoKhatma()
            managing = true
        }
    }

    fun release() {
        if (managing) managing = false
        // todo: Call onReleaseKhatma
    }

    /**
     * Deletes this khatma from DB
     *
     * @return `true`  if deleted successfully. `false` if failed.
     */
    fun deleteKhatma() {
        khatma?.let { if (deleteKhatma(context, it)) callback.onHaveNoKhatma() }
    }

    /**
     * Mark the current werd as done, calculate next werd then refresh Khatma runtime
     */
    fun saveProgress() {
        khatma?.let {
            val nextWerd = it.nextWerd // Calculate next werd (if available).
            if (nextWerd != null) {
                it.saveProgress() // Save progress.
                if (updateKhatma(context, it)) callback.onProgressUpdated(nextWerd) // Notify UI about progress update.
            } else callback.onFinishKhatma() // Khatma has finished.
        }
    }

    companion object {

        /**
         * Creates a new KhatmaManager instance and put given khatma under management.
         *
         * @param khatma Khatma to put under management.
         * @param callback Callback to async send updates to UI.
         */
        @JvmStatic
        fun newInstance(
            context: Context,
            khatma: Khatma?,
            callback: KhatmaManagerCallback
        ) = KhatmaManager(context, khatma, callback)


        /**
         * Creates new khatma in local database and add it as latest created one.
         *
         * @param khatma The Khatma model to be added in local database.
         */
        @JvmStatic
        fun createKhatma(
            context: Context,
            khatma: Khatma
        ) = LocalDatabase.getInstance(context)?.getKhatmaDao()?.createKhatma(khatma) != null

        /**
         * @return All of [Khatma] user has started since he started using app to now.
         */
        @JvmStatic
        fun getAllKhatmas(context: Context) = LocalDatabase.getInstance(context)?.getKhatmaDao()?.getAll() ?: emptyArray()

        /**
         * Query the DB searching for any khatma its id matches given [id].
         *
         * @param id Id of khatma to search lookup in DB.
         *
         * @return [Khatma] data model returned from query or `null` if not found.
         */
        @JvmStatic
        fun getKhatmaById(
            context: Context,
            id: String
        ) = LocalDatabase.getInstance(context)?.getKhatmaDao()?.getKhatmaById(id)

        /**
         *
         */
        @JvmStatic
        fun getKhatmasAtRange(
            context: Context,
            start: Timestamp,
            end: Timestamp
        ) = LocalDatabase.getInstance(context)?.getKhatmaDao()?.getWithinRange(start.toMillis(), end.toMillis()) ?: emptyArray()

        /**
         * @return The most recent active khatma found on DB.
         */
        @JvmStatic
        fun getLastActiveKhatma(context: Context) = LocalDatabase.getInstance(context)!!.getKhatmaDao().getLastActiveKhatma()

        @JvmStatic
        fun updateKhatma(context: Context, khatma: Khatma) = LocalDatabase.getInstance(context)?.getKhatmaDao()?.updateKhatma(khatma) != null

        @JvmStatic
        fun deleteKhatma(context: Context, khatma: Khatma): Boolean = LocalDatabase.getInstance(context)?.getKhatmaDao()?.deleteKhatma(khatma) != null

    }

}