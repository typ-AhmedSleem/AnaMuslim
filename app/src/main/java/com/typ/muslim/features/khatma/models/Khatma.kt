package com.typ.muslim.features.khatma.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.typ.muslim.features.khatma.utils.KhatmaConverters
import com.typ.muslim.features.quran.Quran
import com.typ.muslim.models.Timestamp
import java.io.Serializable
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Constructor used for constructing a khatma model loaded from database
 */
@Entity
@TypeConverters(KhatmaConverters::class)
class Khatma(
    @PrimaryKey(false)
    val id: String,

    var name: String? = null,

    @ColumnInfo(name = "duration")
    val plan: KhatmaPlan,

    @ColumnInfo(name = "startedIn")
    val createdIn: Long = Timestamp.NOW().toMillis(),

    @ColumnInfo()
    var reminder: ReminderPlan?,

    @ColumnInfo(name = "progress")
    var completedWerds: Int = 0,

    @ColumnInfo(name = "step")
    val werdLength: Int = Quran.QURAN_JUZ2S_COUNT / plan.duration
) : Serializable {

    val startedIn: Timestamp
        get() = Timestamp(createdIn)

    val expectedEnd: Timestamp
        get() = startedIn.clone().roll(Calendar.DATE, plan.duration)

    /**
     * Today number on this khatma
     */
    val todayNumber: Int
        get() {
            val now = Timestamp.NOW()
            if (now.isAfter(expectedEnd)) return -1
            // Calculate today
            return plan.duration - TimeUnit.MILLISECONDS.toDays(expectedEnd.toMillis() - now.toMillis()).toInt() + 1
        }

    /**
     * The current werd for this khatma
     */
    val currentWerd: KhatmaWerd
        get() {
            return KhatmaWerd(
                Quran.getJuz2((completedWerds * werdLength) + 1).start,
                Quran.getJuz2((completedWerds * werdLength) + werdLength).end
            )
        }

    /**
     * The next werd for this khatma (if available)
     */
    val nextWerd: KhatmaWerd?
        get() {
            return KhatmaWerd(
                Quran.getJuz2(((completedWerds + 1) * werdLength) + 1).start,
                Quran.getJuz2(((completedWerds + 1) * werdLength) + werdLength).end
            ).takeIf { hasRemainingWerds }
        }

    /**
     * `true` if khatma is still active, `false` otherwise
     */
    val isActive: Boolean
        get() = expectedEnd.isAfter(Timestamp.NOW()) and (completedWerds < plan.duration)

    /**
     * Completed werds as a percentage
     */
    val progressPercentage: Int
        get() = (completedWerds / plan.duration).coerceAtMost(100) // <= 100

    /**
     * `true` if there are remaining werds, `false` otherwise
     */
    val hasRemainingWerds: Boolean
        get() = plan.duration > completedWerds

    /**
     * Number of werds remaining
     */
    val remainingWerds: Int
        get() = (plan.duration - completedWerds).coerceAtLeast(0)

    /**
     * List that holds records of user progress in this khatma
     */
    @Ignore
    val history = mutableListOf<KhatmaHistoryRecord>()

    /**
     * List that holds the achievements unlocked by this user
     */
    @Ignore
    val achievements = mutableListOf<KhatmaAchievement>()

    /**
     * Returns `true` if khatma has a reminder set, `false` otherwise
     */
    fun hasReminder() = reminder != null

    /**
     * Add record to the history list
     */
    fun addHistoryRecord(record: KhatmaHistoryRecord) = history.add(record)

    /**
     * Add achievement to achievements list
     */
    fun addAchievement(achievement: KhatmaAchievement) = achievements.add(achievement)

    /**
     * Mark the current werd as done, calculate next werd then refresh Khatma runtime
     */
    fun saveProgress() {
        if (hasRemainingWerds) completedWerds++
    }

    /**
     * Changes the plan of this khatma to a new one.
     * [NOTE!!!] This method is synchronized and will be locked if called several times immediately to prevent heavy UI updating load.
     *
     * @param newPlan The new plan.
     */
    fun changePlan(newPlan: KhatmaPlan): Boolean {
        TODO("[WILL BE IMPLEMENTED IN FUTURE DEVELOPMENT].")
    }

}