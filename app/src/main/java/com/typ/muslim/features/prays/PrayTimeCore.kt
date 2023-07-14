/*
 * This product is developed by TYP Software
 * Project Head : Ahmed Sleem
 * Programmer : Ahmed Sleem
 * Pre-Release Tester : Ahmed Sleem
 *
 * Copyright (c) TYP Electronics Corporation.  All Rights Reserved
 *//*
 *
 * This library is written by praytimes.org and modified by : Ahmed Sleem (ME)
 */
package com.typ.muslim.features.prays

import android.content.Context
import com.typ.muslim.R
import com.typ.muslim.features.prays.enums.CalculationMethod
import com.typ.muslim.features.prays.enums.HigherLatitudesMethod
import com.typ.muslim.features.prays.enums.PrayType
import com.typ.muslim.features.prays.enums.PrayType.Companion.valueOf
import com.typ.muslim.features.prays.models.Pray
import com.typ.muslim.features.prays.models.PrayTimes
import com.typ.muslim.features.prays.models.PrayTimes.Companion.newBuilder
import com.typ.muslim.managers.AMSettings
import com.typ.muslim.managers.AManager
import com.typ.muslim.managers.ResMan
import com.typ.muslim.models.Location
import com.typ.muslim.models.Time
import com.typ.muslim.models.Timestamp
import com.typ.muslim.utils.DateUtils
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.round
import kotlin.math.sin
import kotlin.math.tan

class PrayTimeCore private constructor(context: Context, private val currLocation: Location) {

    private val prayerNames: Array<String> // Prays names
    private val invalidTime = "--:-- --" // The string used for invalid times

    // ------------------- Calc Method Parameters --------------------
    private val methodParams = HashMap<Int, DoubleArray>()

    // ---------------------- Global Variables --------------------
    private var dhuhrMinutes = 0 // Minutes after mid-day for Dhuhr.
    private var jDate = 0.0 // Julian date
    private val offsets: IntArray // Prays offsets.

    // --------------------- Technical Settings --------------------
    private var numIterations = 5 // Number of iterations needed to compute times

    // ---------------------- Time-Zone Settings -----------------------
    private var useDefaultTimezone: Boolean = false
    private val defaultTimezone: Double
        // Compute base time-zone of the system
        get() = TimeZone.getDefault().rawOffset / 1000.0 / 3600

    // Detect daylight saving in a given date
    private fun detectDaylightSaving() = TimeZone.getDefault().dstSavings.toDouble()

    /*
     * this.methodParams[methodNum] = new Array(fa, ms, mv, is, iv);
     * <p>
     * fa : fajr angle ms : maghrib selector (0 = angle; 1 = minutes after
     * sunset) mv : maghrib parameter value (in angle or minutes) is : isha
     * selector (0 = angle; 1 = minutes after maghrib) iv : isha parameter value
     * (in angle or minutes)
     */
    init {
        // Initialize variables
        prayerNames = arrayOf(
            ResMan.getString(context, R.string.fajr_pray),
            ResMan.getString(context, R.string.sunrise),
            if (DateUtils.isTodayFriday()) ResMan.getString(context, R.string.jumaa_pray) else ResMan.getString(context, R.string.dhuhr_pray),
            ResMan.getString(context, R.string.asr_pray),
            ResMan.getString(context, R.string.maghrib_pray),
            ResMan.getString(context, R.string.isha_pray)
        )
        useDefaultTimezone = AMSettings.isUsingDefaultTimezone(context)
        // Get offsets for each pray (index 5 is for sunrise and always equals to maghrib offset)
        offsets = intArrayOf(
            AMSettings.getOffsetMinutesForPray(context, PrayType.FAJR),
            AMSettings.getOffsetMinutesForPray(context, PrayType.SUNRISE),
            AMSettings.getOffsetMinutesForPray(context, PrayType.DHUHR),
            AMSettings.getOffsetMinutesForPray(context, PrayType.ASR),
            0, // Sunset (unnecessary).
            AMSettings.getOffsetMinutesForPray(context, PrayType.MAGHRIB),
            AMSettings.getOffsetMinutesForPray(context, PrayType.ISHA),
        )
        // =============== Initialize PrayTimeCore instance ===============
        // Jafari
        methodParams[CalculationMethod.JAFARI.ordinal] = doubleArrayOf(16.0, 0.0, 4.0, 0.0, 14.0)
        // Karachi
        methodParams[CalculationMethod.KARACHI.ordinal] = doubleArrayOf(18.0, 1.0, 0.0, 0.0, 18.0)
        // ISNA
        methodParams[CalculationMethod.ISNA.ordinal] = doubleArrayOf(15.0, 1.0, 0.0, 0.0, 15.0)
        // MWL
        methodParams[CalculationMethod.MWL.ordinal] = doubleArrayOf(18.0, 1.0, 0.0, 0.0, 17.0)
        // Makkah
        methodParams[CalculationMethod.MAKKAH.ordinal] = doubleArrayOf(18.5, 1.0, 0.0, 1.0, 90.0)
        // Egypt
        methodParams[CalculationMethod.EGYPT.ordinal] = doubleArrayOf(19.5, 1.0, 0.0, 0.0, 17.5)
        // Tehran
        methodParams[CalculationMethod.TEHRAN.ordinal] = doubleArrayOf(17.7, 0.0, 4.5, 0.0, 14.0)
        // Custom
        methodParams[CalculationMethod.CUSTOM.ordinal] = doubleArrayOf(18.0, 1.0, 0.0, 0.0, 17.0)

        AManager.log(TAG, "====================== Created instance with configuration ==========================\n")
        AManager.log(TAG, "Offsets: $offsets")
        AManager.log(TAG, "Params: $methodParams")
        AManager.log(TAG, "Params: $methodParams")
        AManager.log(TAG, "=====================================================================================\n")
    }

    // ---------------------- Trigonometric Functions -----------------------

    /**
     * Range reduce angle in degrees.
     */
    private fun finAngle(a: Double): Double {
        var temp = a
        temp -= 360 * floor(temp / 360.0)
        temp = if (temp < 0) temp + 360 else temp
        return temp
    }

    /**
     * Range reduce hours to 0..23
     */
    private fun fixHour(a: Double): Double {
        var temp = a
        temp -= 24.0 * floor(temp / 24.0)
        temp = if (temp < 0) temp + 24 else temp
        return temp
    }

    /**
     * Convert Radians to Degrees
     */
    private fun rad2Deg(alpha: Double) = alpha * 180.0 / PI


    /**
     * Convert Degrees to Radians
     */
    private fun deg2Rad(alpha: Double) = alpha * PI / 180.0

    /**
     * Degree Sin
     */
    private fun dSin(d: Double) = sin(deg2Rad(d))

    /**
     * Degree Cos
     */
    private fun dCos(d: Double) = cos(deg2Rad(d))

    /**
     * Degree Tan
     */
    private fun dTan(d: Double) = tan(deg2Rad(d))

    /**
     * Degree ArcSin
     */
    private fun dArcSin(x: Double) = rad2Deg(asin(x))

    /**
     * Degree ArcCos
     */
    private fun dArcCos(x: Double) = rad2Deg(acos(x))

    /**
     * Degree ArcTan
     */
    private fun dArcTan(x: Double) = rad2Deg(atan(x))

    /**
     * Degree ArcTan2
     */
    private fun dArcTan2(y: Double, x: Double) = rad2Deg(atan2(y, x))


    /**
     * Degree ArcCot
     */
    private fun dArcCot(x: Double) = rad2Deg(atan2(1.0, x))

    // ---------------------- Julian Date Functions -----------------------
    // calculate julian date from a calendar date
    private fun julianDate(year: Int, month: Int, day: Int): Double {
        var tYear = year
        var tMonth = month
        if (tMonth <= 2) {
            tYear -= 1
            tMonth += 12
        }
        val A = floor(tYear / 100.0)
        val B = 2 - A + floor(A / 4.0)
        return floor(365.25 * (tYear + 4716)) + floor(30.6001 * (tMonth + 1)) + day + B - 1524.5
    }

    // convert a calendar date to julian date (second method)
    private fun calcJD(year: Int, month: Int, day: Int): Double {
        val j1970 = 2440588.0
        val date = Date(year, month - 1, day)
        val ms = date.time.toDouble() // # of milliseconds since midnight Jan 1,
        // 1970
        val days = floor(ms / (1000.0 * 60.0 * 60.0 * 24.0))
        return j1970 + days - 0.5
    }

    // ---------------------- Calculation Functions -----------------------
    // References:
    // http://www.ummah.net/astronomy/saltime
    // http://aa.usno.navy.mil/faq/docs/SunApprox.html
    // compute declination angle of sun and equation of time
    private fun sunPosition(jd: Double): DoubleArray {
        val D = jd - 2451545
        val g = finAngle(357.529 + 0.98560028 * D)
        val q = finAngle(280.459 + 0.98564736 * D)
        val L = finAngle(q + 1.915 * dSin(g) + 0.020 * dSin(2 * g))

        // double R = 1.00014 - 0.01671 * [self dcos:g] - 0.00014 * [self dcos:
        // (2*g)];
        val e = 23.439 - 0.00000036 * D
        val d = dArcSin(dSin(e) * dSin(L))
        var RA = dArcTan2(dCos(e) * dSin(L), dCos(L)) / 15.0
        RA = fixHour(RA)
        val EqT = q / 15.0 - RA
        val sPosition = DoubleArray(2)
        sPosition[0] = d
        sPosition[1] = EqT
        return sPosition
    }

    // compute equation of time
    private fun equationOfTime(jd: Double): Double {
        return sunPosition(jd)[1]
    }

    // compute declination angle of sun
    private fun sunDeclination(jd: Double): Double {
        return sunPosition(jd)[0]
    }

    // compute mid-day (Dhuhr, Zawal) time
    private fun computeMidDay(t: Double): Double {
        val T = equationOfTime(jDate + t)
        return fixHour(12 - T)
    }

    // compute time for a given angle G
    private fun computeTime(G: Double, t: Double): Double {
        val D = sunDeclination(jDate + t)
        val Z = computeMidDay(t)
        val Beg = -dSin(G) - dSin(D) * dSin(currLocation.latitude)
        val Mid = dCos(D) * dCos(currLocation.latitude)
        val V = dArcCos(Beg / Mid) / 15.0
        return Z + if (G > 90) -V else V
    }

    // compute the time of Asr
    // Shafii: step=1, Hanafi: step=2
    private fun computeAsr(step: Double, t: Double): Double {
        val D = sunDeclination(jDate + t)
        val G = -dArcCot(step + dTan(abs(currLocation.latitude - D)))
        return computeTime(G, t)
    }

    // ---------------------- Misc Functions -----------------------
    // compute the difference between two times
    private fun timeDiff(time1: Double, time2: Double): Double {
        return fixHour(time2 - time1)
    }
    // -------------------- Interface Functions --------------------
    /**
     * Get computed PrayTimes
     *
     * @return Returns the computed PrayTimes for the given Location and Julian Date
     */
    fun getPrayTimes(rollDays: Int): PrayTimes {
        // Do Calculations
        val timestamp = Timestamp.NOW()
        timestamp.roll(Calendar.DATE, rollDays)
        jDate = julianDate(timestamp.year, timestamp.month, timestamp.day)
        val lonDiff = currLocation.longitude / (15.0 * 24.0)
        jDate -= lonDiff
        // Put result in array
        val computedPrayTimes = computeDayTimes()
        val prayTimesBuilder = newBuilder()
        for (index in computedPrayTimes.indices) {
            timestamp[Calendar.HOUR_OF_DAY] = computedPrayTimes[index].hours
            timestamp[Calendar.MINUTE] = computedPrayTimes[index].minutes
            timestamp[Calendar.SECOND] = 0
            prayTimesBuilder.add(Pray(valueOf(index), prayerNames[index], timestamp.toMillis()))
        }
        return prayTimesBuilder.build()
    }

    /**
     * Get computed PrayTimes
     *
     * @return Returns the computed PrayTimes for the given Location and Julian Date
     */
    fun getPrayTimes(day: Timestamp): PrayTimes {
        // Do Calculations
        jDate = julianDate(day.year, day.month, day.day)
        //		this.setJDate(julianDate(in.get(Calendar.YEAR), in.get(Calendar.MONTH) + 1, in.get(Calendar.DATE)));
        val lonDiff = currLocation.longitude / (15.0 * 24.0)
        jDate -= lonDiff
        // Put result in array
        val computedPrayTimes = computeDayTimes()
        val prayTimesBuilder = newBuilder()
        for (index in computedPrayTimes.indices) {
            day[Calendar.HOUR_OF_DAY] = computedPrayTimes[index].hours
            day[Calendar.MINUTE] = computedPrayTimes[index].minutes
            day[Calendar.SECOND] = 0
            prayTimesBuilder.add(Pray(valueOf(index), prayerNames[index], day.toMillis()))
        }
        return prayTimesBuilder.build()
    }

    // set custom values for calculation parameters
    private fun setCustomParams(params: DoubleArray) {
        for (i in 0..4) {
            if (params[i] == -1.0) {
                params[i] = methodParams[currLocation.config.calculationMethod.ordinal]!![i]
                methodParams[CalculationMethod.CUSTOM.ordinal] = params
            } else methodParams[CalculationMethod.CUSTOM.ordinal]!![i] = params[i]
        }
        currLocation.config.setCalculationMethod(CalculationMethod.CUSTOM)
    }

    // Set the angle for calculating Fajr
    fun setFajrAngle(angle: Double) {
        val params = doubleArrayOf(angle, -1.0, -1.0, -1.0, -1.0)
        setCustomParams(params)
    }

    // Set the angle for calculating Maghrib
    fun setMaghribAngle(angle: Double) {
        val params = doubleArrayOf(-1.0, 0.0, angle, -1.0, -1.0)
        setCustomParams(params)
    }

    // Set the angle for calculating Isha
    fun setIshaAngle(angle: Double) {
        val params = doubleArrayOf(-1.0, -1.0, -1.0, 0.0, angle)
        setCustomParams(params)
    }

    // Set the minutes after Sunset for calculating Maghrib
    fun setMaghribMinutes(minutes: Double) {
        val params = doubleArrayOf(-1.0, 1.0, minutes, -1.0, -1.0)
        setCustomParams(params)
    }

    // Set the minutes after Maghrib for calculating Isha
    fun setIshaMinutes(minutes: Double) {
        val params = doubleArrayOf(-1.0, -1.0, -1.0, 1.0, minutes)
        setCustomParams(params)
    }

    // Convert double hours to 24h format
    private fun floatToTime24(time: Double): String {
        var temp = time
        val result: String
        if (temp.isNaN()) return invalidTime
        // Fix hours and minutes
        temp = fixHour(temp + 0.5 / 60.0) // add 0.5 minutes to round
        val hours = floor(temp).toInt()
        val minutes = floor((temp - hours) * 60.0)
        // String format
        result = when {
            hours in 0..9 && minutes in 0.0..9.0 -> "0" + hours + ":0" + round(minutes)
            hours in 0..9 -> "0" + hours + ":" + round(minutes)
            minutes in 0.0..9.0 -> hours.toString() + ":0" + round(minutes)
            else -> "$hours:" + round(minutes)
        }
        return result
    }

    // convert double hours to 12h format
    private fun floatToTime12(time: Double, noSuffix: Boolean = false): String {
        var temp = time
        if (java.lang.Double.isNaN(temp)) {
            return invalidTime
        }
        temp = fixHour(temp + 0.5 / 60) // add 0.5 minutes to round
        var hours = floor(temp).toInt()
        val minutes = floor((temp - hours) * 60)
        val suffix: String
        val result: String
        suffix = if (hours >= 12) {
            "pm"
        } else {
            "am"
        }
        hours = (hours + 12 - 1) % 12 + 1 /*hours = (hours + 12) - 1;
        int hrs = (int) hours % 12;
        hrs += 1;*/
        result = when {
            !noSuffix -> {
                if (hours in 0..9 && minutes in 0.0..9.0) {
                    "0" + hours + ":0" + round(minutes) + " " + suffix
                } else if (hours in 0..9) {
                    "0" + hours + ":" + round(minutes) + " " + suffix
                } else if (minutes in 0.0..9.0) {
                    "$hours:0" + round(minutes) + " " + suffix
                } else {
                    "$hours:" + round(minutes) + " " + suffix
                }
            }

            else -> {
                if (hours in 0..9 && minutes in 0.0..9.0) {
                    "0" + hours + ":0" + round(minutes)
                } else if (hours in 0..9) {
                    "0" + hours + ":" + round(minutes)
                } else if (minutes in 0.0..9.0) {
                    hours.toString() + ":0" + round(minutes)
                } else {
                    "$hours:" + round(minutes)
                }
            }
        }
        return result
    }

    /**
     * Convert double hours to 12h format with no suffix
     */
    private fun floatToTime12NS(time: Double) = floatToTime12(time, true)


    // ---------------------- Compute Prayer Times -----------------------
    // Compute prayer times at given julian date
    private fun computeTimes(times: DoubleArray): DoubleArray {
        val t = dayPortion(times)
        val fajr = computeTime(180 - methodParams[currLocation.config.calculationMethod.ordinal]!![0], t[0])
        val sunrise = computeTime(180 - 0.833, t[1])
        val dhuhr = computeMidDay(t[2])
        val asr = computeAsr((1 + currLocation.config.asrMethod.ordinal).toDouble(), t[3])
        val sunset = computeTime(0.833, t[4])
        val maghrib = computeTime(methodParams[currLocation.config.calculationMethod.ordinal]!![2], t[5])
        val isha = computeTime(methodParams[currLocation.config.calculationMethod.ordinal]!![4], t[6])
        return doubleArrayOf(fajr, sunrise, dhuhr, asr, sunset, maghrib, isha)
    }

    // compute prayer times at given julian date
    private fun computeDayTimes(): Array<Time> {
        var times = doubleArrayOf(5.0, 6.0, 12.0, 13.0, 18.0, 18.0, 18.0) // default times
        for (i in 1..numIterations) times = computeTimes(times)
        adjustTimes(times)
        tuneTimes(times)
        return adjustTimesFormat(times)
    }

    // adjust times in a prayer time array
    private fun adjustTimes(times: DoubleArray) {
        val timezone = if (useDefaultTimezone) defaultTimezone else currLocation.timezone
        for (i in times.indices) times[i] += timezone - currLocation.longitude / 15 // fixme: replace defaultTimezone with currLocation.timezone
        times[2] += (dhuhrMinutes / 60f).toDouble() // Dhuhr
        if (methodParams[currLocation.config.calculationMethod.ordinal]!![1] == 1.0) times[5] = times[4] + methodParams[currLocation.config.calculationMethod.ordinal]!![2] / 60 // Maghrib
        if (methodParams[currLocation.config.calculationMethod.ordinal]!![3] == 1.0) times[6] = times[5] + methodParams[currLocation.config.calculationMethod.ordinal]!![4] / 60 // Isha
        if (currLocation.config.higherLatitude !== HigherLatitudesMethod.NONE) adjustHighLatTimes(times)
    }

    // Convert times array to long timestamp
    private fun adjustTimesFormat(times: DoubleArray): Array<Time> {
        // The array 'times' contains 7 items (sunset at index 5) so, we consume all indices except 5
        return arrayOf(
            floatToTime(times[0]), // Fajr
            floatToTime(times[1]), // Sunrise
            floatToTime(times[2]), // Dhuhr
            floatToTime(times[3]), // Asr
            floatToTime(times[5]), // Maghrib
            floatToTime(times[6]) // Isha
        )
//        val calculatedPrayTimes = arrayOfNulls<Time>(6)
//        for (i in calculatedPrayTimes.indices) {
//            calculatedPrayTimes[i] = floatToTime(times[if (i == 4 || i == 5) i + 1 else i])
//        }
    }

    private fun floatToTime(time: Double): Time {
        var temp = time
        if (temp.isNaN()) return Time(0, 0, 0) // Invalid time
        // Fix hours and minutes
        temp = fixHour(temp + 0.5 / 60.0) // add 0.5 minutes to round
        val hours = floor(temp).toInt()
        val minutes = floor((temp - hours) * 60.0)
        return Time(hours, minutes.toInt(), 0)
    }

    // adjust Fajr, Isha and Maghrib for locations in higher latitudes
    private fun adjustHighLatTimes(times: DoubleArray) {
        val nightTime = timeDiff(times[4], times[1]) // sunset to sunrise

        // Adjust Fajr
        val FajrDiff = nightPortion(methodParams[currLocation.config.calculationMethod.ordinal]!![0]) * nightTime
        if (java.lang.Double.isNaN(times[0]) || timeDiff(times[0], times[1]) > FajrDiff) {
            times[0] = times[1] - FajrDiff
        }

        // Adjust Isha
        val IshaAngle: Double = if (methodParams[currLocation.config.calculationMethod.ordinal]!![3] == 0.0) methodParams[currLocation.config.calculationMethod.ordinal]!![4] else 18.0
        val IshaDiff = nightPortion(IshaAngle) * nightTime
        if (java.lang.Double.isNaN(times[6]) || timeDiff(times[4], times[6]) > IshaDiff) {
            times[6] = times[4] + IshaDiff
        }

        // Adjust Maghrib
        val MaghribAngle: Double = if (methodParams[currLocation.config.calculationMethod.ordinal]!![1] == 0.0) methodParams[currLocation.config.calculationMethod.ordinal]!![2] else 4.0
        val MaghribDiff = nightPortion(MaghribAngle) * nightTime
        if (java.lang.Double.isNaN(times[5]) || timeDiff(times[4], times[5]) > MaghribDiff) times[5] = times[4] + MaghribDiff
    }

    // The night portion used for adjusting times in higher latitudes
    private fun nightPortion(angle: Double): Double {
        var calc = 0.0
        if (currLocation.config.higherLatitude === HigherLatitudesMethod.ANGLEBASED) calc = angle / 60.0 else if (currLocation.config.higherLatitude === HigherLatitudesMethod.MIDNIGHT) calc =
            0.5 else if (currLocation.config.higherLatitude === HigherLatitudesMethod.ONESEVENTH) calc = 0.14286
        return calc
    }

    // convert hours to day portions
    private fun dayPortion(times: DoubleArray): DoubleArray {
        for (i in 0..6) times[i] /= 24.0
        return times
    }

    // Tune timings for adjustments
    // Set time offsets
    fun tune(offsetTimes: IntArray) {
        // offsetTimes length
        // should be 7 in order
        // of Fajr, Sunrise,
        // Dhuhr, Asr, Sunset,
        // Maghrib, Isha
        System.arraycopy(offsetTimes, 0, offsets, 0, offsetTimes.size)
    }

    private fun tuneTimes(times: DoubleArray) {
        for (i in times.indices) times[i] += offsets[i] / 60.0
    }

    companion object {
        @Volatile
        private var singletonInstance: PrayTimeCore? = null

        private const val TAG = "PrayTimeCore"

        /**
         * Create a singleton instance of PrayTimeCore with given parameters if current instance wasn't found
         *
         * @param context Context to access Resources.
         * @return Initialized Singleton Instance of PrayTimeCore.
         */
        @JvmOverloads
        fun singletonInstance(context: Context, location: Location = AMSettings.getCurrentLocation(context)): PrayTimeCore {
            if (singletonInstance == null) {
                synchronized(PrayTimeCore::class.java) {
                    if (singletonInstance == null) singletonInstance = PrayTimeCore(context, location)
                }
            }
            return singletonInstance!!
        }

        /**
         * Create a new instance of PrayTimeCore class with given parameters
         *
         * @param context  Context to access resources.
         * @param location Location holds data used in calculation.
         * @return New Initialized Instance of PrayTimeCore.
         */
        @JvmStatic
        fun newInstance(context: Context, location: Location) = PrayTimeCore(context, location)
    }
}
