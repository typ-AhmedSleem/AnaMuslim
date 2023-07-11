/*
 * This product is developed by TYP Software
 * Project Head : Ahmed Sleem
 * Programmer : Ahmed Sleem
 * Pre-Release Tester : Ahmed Sleem & Ahmed Hafez
 *
 * Copyright (c) TYP Electronics Corporation.  All Rights Reserved
 */

/*
 *
 * This library is written by praytimes.org and modified by : Ahmed Sleem (ME)
 */

package com.typ.muslim.features.prays;

import android.content.Context;

import com.typ.muslim.R;
import com.typ.muslim.enums.FormatPatterns;
import com.typ.muslim.features.prays.enums.CalculationMethod;
import com.typ.muslim.features.prays.enums.HigherLatitudesMethod;
import com.typ.muslim.features.prays.enums.PrayType;
import com.typ.muslim.features.prays.models.Pray;
import com.typ.muslim.features.prays.models.PrayTimes;
import com.typ.muslim.libs.easyjava.data.EasyList;
import com.typ.muslim.managers.AMSettings;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.managers.ResMan;
import com.typ.muslim.models.Location;
import com.typ.muslim.models.Time;
import com.typ.muslim.models.Timestamp;
import com.typ.muslim.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class PrayTimeCore {

    // Static Singleton Instance of PrayTimeCore
    private static final String TAG = "PrayTimeCore";
    private static volatile PrayTimeCore singletonInstance;
    private final List<String> prayerNames = new ArrayList<>(); // Time Names
    private final String InvalidTime = "--:-- --"; // The string used for invalid times
    // ------------------- Calc Method Parameters --------------------
    private final HashMap<Integer, double[]> methodParams = new HashMap<>();
    // ---------------------- Global Variables --------------------
    private Location currentLocation;
    private int dhuhrMinutes; // minutes after mid-day for Dhuhr
    private double JDate; // Julian date
    private int[] offsets = new int[7];
    // --------------------- Technical Settings --------------------
    private int numIterations; // Number of iterations needed to compute times

    /*
     * this.methodParams[methodNum] = new Array(fa, ms, mv, is, iv);
     * <p>
     * fa : fajr angle ms : maghrib selector (0 = angle; 1 = minutes after
     * sunset) mv : maghrib parameter value (in angle or minutes) is : isha
     * selector (0 = angle; 1 = minutes after maghrib) iv : isha parameter value
     * (in angle or minutes)
     */

    private PrayTimeCore(Context context, Location currentLocation) {
        // Initialize variables
        this.setCurrentLocation(currentLocation).setDhuhrMinutes(0).setNumIterations(5); // Number of iterations needed to compute times
        prayerNames.add(ResMan.getString(context, R.string.fajr_pray));
        prayerNames.add(ResMan.getString(context, R.string.sunrise));
        prayerNames.add(DateUtils.isTodayFriday() ? ResMan.getString(context, R.string.jumaa_pray) : ResMan.getString(context, R.string.dhuhr_pray));
        prayerNames.add(ResMan.getString(context, R.string.asr_pray));
        //prayerNames.add(AMRes.getString(context, R.string.sunset));
        prayerNames.add(ResMan.getString(context, R.string.maghrib_pray));
        prayerNames.add(ResMan.getString(context, R.string.isha_pray));
        // =============== Initialize PrayTimeCore instance ===============
        // Jafari
        double[] Jvalues = {16, 0, 4, 0, 14};
        methodParams.put(CalculationMethod.JAFARI.ordinal(), Jvalues);

        // Karachi
        double[] Kvalues = {18, 1, 0, 0, 18};
        methodParams.put(CalculationMethod.KARACHI.ordinal(), Kvalues);

        // ISNA
        double[] Ivalues = {15, 1, 0, 0, 15};
        methodParams.put(CalculationMethod.ISNA.ordinal(), Ivalues);

        // MWL
        double[] MWvalues = {18, 1, 0, 0, 17};
        methodParams.put(CalculationMethod.MWL.ordinal(), MWvalues);

        // Makkah
        double[] MKvalues = {18.5, 1, 0, 1, 90};
        methodParams.put(CalculationMethod.MAKKAH.ordinal(), MKvalues);

        // Egypt
        double[] Evalues = {19.5, 1, 0, 0, 17.5};
        methodParams.put(CalculationMethod.EGYPT.ordinal(), Evalues);

        // Tehran
        double[] Tvalues = {17.7, 0, 4.5, 0, 14};
        methodParams.put(CalculationMethod.TEHRAN.ordinal(), Tvalues);

        // Custom
        double[] Cvalues = {18, 1, 0, 0, 17};
        methodParams.put(CalculationMethod.CUSTOM.ordinal(), Cvalues);
    }

    /**
     * Create a singleton instance of PrayTimeCore with given parameters if current instance wasn't found
     *
     * @param context Context to access Resources.
     * @return Initialized Singleton Instance of PrayTimeCore.
     */
    public static PrayTimeCore getSingletonInstance(Context context) {
        return getSingletonInstance(context, AMSettings.getCurrentLocation(context));
    }

    /**
     * Create a singleton instance of PrayTimeCore with given parameters if current instance wasn't found
     *
     * @param context  Context to access Resources.
     * @param location Location holds data used in calculation.
     * @return Initialized Singleton Instance of PrayTimeCore.
     */
    public static PrayTimeCore getSingletonInstance(Context context, Location location) {
        if (singletonInstance == null) {
            synchronized (PrayTimeCore.class) {
                if (singletonInstance == null) singletonInstance = new PrayTimeCore(context, location);
            }
        }
        return singletonInstance;
    }

    /**
     * Create a new instance of PrayTimeCore class with given parameters
     *
     * @param context  Context to access Resources.
     * @param location Location holds data used in calculation.
     * @return New Initialized Instance of PrayTimeCore.
     */
    public static PrayTimeCore getNewInstance(Context context, Location location) {
        return new PrayTimeCore(context, location);
    }

    // ---------------------- Trigonometric Functions -----------------------
    // range reduce angle in degrees.
    private double finAngle(double a) {
        a = a - (360 * (Math.floor(a / 360.0)));
        a = a < 0 ? (a + 360) : a;
        return a;
    }

    // range reduce hours to 0..23
    private double fixHour(double a) {
        a = a - 24.0 * Math.floor(a / 24.0);
        a = a < 0 ? (a + 24) : a;
        return a;
    }

    /**
     * Convert Radians to Degrees
     */
    private double radiansToDegrees(double alpha) {
        return ((alpha * 180.0) / Math.PI);
    }

    /**
     * Convert Degrees to Radians
     */
    private double DegreesToRadians(double alpha) {
        return ((alpha * Math.PI) / 180.0);
    }

    /**
     * Degree Sin
     */
    private double dSin(double d) {
        return (Math.sin(DegreesToRadians(d)));
    }

    /**
     * Degree Cos
     */
    private double dCos(double d) {
        return (Math.cos(DegreesToRadians(d)));
    }

    /**
     * Degree Tan
     */
    private double dTan(double d) {
        return (Math.tan(DegreesToRadians(d)));
    }

    /**
     * Degree ArcSin
     */
    private double dArcSin(double x) {
        double val = Math.asin(x);
        return radiansToDegrees(val);
    }

    /**
     * Degree ArcCos
     */
    private double dArcCos(double x) {
        double val = Math.acos(x);
        return radiansToDegrees(val);
    }

    /**
     * Degree ArcTan
     */
    private double dArcTan(double x) {
        double val = Math.atan(x);
        return radiansToDegrees(val);
    }

    /**
     * Degree ArcTan2
     */
    private double dArcTan2(double y, double x) {
        double val = Math.atan2(y, x);
        return radiansToDegrees(val);
    }

    /**
     * Degree ArcCot
     */
    private double dArcCot(double x) {
        double val = Math.atan2(1.0, x);
        return radiansToDegrees(val);
    }

    // ---------------------- Time-Zone Functions -----------------------
    // compute local time-zone for a specific date
    private double getTimeZone1() {
        return (TimeZone.getDefault().getRawOffset() / 1000.0) / 3600;
    }

    // compute base time-zone of the system
    private double getBaseTimeZone() {
        return (TimeZone.getDefault().getRawOffset() / 1000.0) / 3600;

    }

    // detect daylight saving in a given date
    private double detectDaylightSaving() {
        return TimeZone.getDefault().getDSTSavings();
    }

    // ---------------------- Julian Date Functions -----------------------
    // calculate julian date from a calendar date
    private double julianDate(int year, int month, int day) {
        if (month <= 2) {
            year -= 1;
            month += 12;
        }
        double A = Math.floor(year / 100.0);
        double B = 2 - A + Math.floor(A / 4.0);
        return Math.floor(365.25 * (year + 4716)) + Math.floor(30.6001 * (month + 1)) + day + B - 1524.5;
    }

    // convert a calendar date to julian date (second method)
    private double calcJD(int year, int month, int day) {
        double J1970 = 2440588.0;
        Date date = new Date(year, month - 1, day);
        double ms = date.getTime(); // # of milliseconds since midnight Jan 1,
        // 1970
        double days = Math.floor(ms / (1000.0 * 60.0 * 60.0 * 24.0));
        return J1970 + days - 0.5;
    }

    // ---------------------- Calculation Functions -----------------------
    // References:
    // http://www.ummah.net/astronomy/saltime
    // http://aa.usno.navy.mil/faq/docs/SunApprox.html
    // compute declination angle of sun and equation of time
    private double[] sunPosition(double jd) {

        double D = jd - 2451545;
        double g = finAngle(357.529 + 0.98560028 * D);
        double q = finAngle(280.459 + 0.98564736 * D);
        double L = finAngle(q + (1.915 * dSin(g)) + (0.020 * dSin(2 * g)));

        // double R = 1.00014 - 0.01671 * [self dcos:g] - 0.00014 * [self dcos:
        // (2*g)];
        double e = 23.439 - (0.00000036 * D);
        double d = dArcSin(dSin(e) * dSin(L));
        double RA = (dArcTan2((dCos(e) * dSin(L)), (dCos(L)))) / 15.0;
        RA = fixHour(RA);
        double EqT = q / 15.0 - RA;
        double[] sPosition = new double[2];
        sPosition[0] = d;
        sPosition[1] = EqT;

        return sPosition;
    }

    // compute equation of time
    private double equationOfTime(double jd) {
        return sunPosition(jd)[1];
    }

    // compute declination angle of sun
    private double sunDeclination(double jd) {
        return sunPosition(jd)[0];
    }

    // compute mid-day (Dhuhr, Zawal) time
    private double computeMidDay(double t) {
        double T = equationOfTime(this.getJDate() + t);
        return fixHour(12 - T);
    }

    // compute time for a given angle G
    private double computeTime(double G, double t) {

        double D = sunDeclination(this.getJDate() + t);
        double Z = computeMidDay(t);
        double Beg = -dSin(G) - dSin(D) * dSin(this.getCurrentLocation().getLatitude());
        double Mid = dCos(D) * dCos(this.getCurrentLocation().getLatitude());
        double V = dArcCos(Beg / Mid) / 15.0;

        return Z + (G > 90 ? -V : V);
    }

    // compute the time of Asr
    // Shafii: step=1, Hanafi: step=2
    private double computeAsr(double step, double t) {
        double D = sunDeclination(this.getJDate() + t);
        double G = -dArcCot(step + dTan(Math.abs(this.getCurrentLocation().getLatitude() - D)));
        return computeTime(G, t);
    }

    // ---------------------- Misc Functions -----------------------
    // compute the difference between two times
    private double timeDiff(double time1, double time2) {
        return fixHour(time2 - time1);
    }

    // -------------------- Interface Functions --------------------

    /**
     * Get computed PrayTimes
     *
     * @return Returns the computed PrayTimes for the given Location and Julian Date
     */
    public PrayTimes getPrayTimes(int rollDays) {
        // Do Calculations
        Timestamp timestamp = Timestamp.NOW();
        timestamp.roll(Calendar.DATE, rollDays);
        this.setJDate(julianDate(timestamp.getYear(), timestamp.getMonth(), timestamp.getDay()));
//		this.setJDate(julianDate(timestamp.get(Calendar.YEAR), timestamp.get(Calendar.MONTH) + 1, timestamp.get(Calendar.DATE)));
        double lonDiff = getCurrentLocation().getLongitude() / (15.0 * 24.0);
        this.setJDate(this.getJDate() - lonDiff);
        // Put result in array
        Time[] computedPrayTimes = computeDayTimes();
        PrayTimes.Builder prayTimesBuilder = PrayTimes.newBuilder();
        for (int index = 0; index < computedPrayTimes.length; index++) {
            timestamp.set(Calendar.HOUR_OF_DAY, computedPrayTimes[index].getHours());
            timestamp.set(Calendar.MINUTE, computedPrayTimes[index].getMinutes());
            timestamp.set(Calendar.SECOND, 0);
            prayTimesBuilder.add(new Pray(PrayType.valueOf(index), getPrayerNames().get(index), timestamp.toMillis()));
        }
        return prayTimesBuilder.build();
    }

    /**
     * Get computed PrayTimes
     *
     * @return Returns the computed PrayTimes for the given Location and Julian Date
     */
    public PrayTimes getPrayTimes(Timestamp in) {
        // Do Calculations
        this.setJDate(julianDate(in.getYear(), in.getMonth(), in.getDay()));
//		this.setJDate(julianDate(in.get(Calendar.YEAR), in.get(Calendar.MONTH) + 1, in.get(Calendar.DATE)));
        double lonDiff = getCurrentLocation().getLongitude() / (15.0 * 24.0);
        this.setJDate(this.getJDate() - lonDiff);
        // Put result in array
        Time[] computedPrayTimes = computeDayTimes();
        PrayTimes.Builder prayTimesBuilder = PrayTimes.newBuilder();
        for (int index = 0; index < computedPrayTimes.length; index++) {
            in.set(Calendar.HOUR_OF_DAY, computedPrayTimes[index].getHours());
            in.set(Calendar.MINUTE, computedPrayTimes[index].getMinutes());
            in.set(Calendar.SECOND, 0);
            prayTimesBuilder.add(new Pray(PrayType.valueOf(index), getPrayerNames().get(index), in.toMillis()));
        }
        return prayTimesBuilder.build();
    }

    /**
     * Get computed PrayTimes
     *
     * @return Returns the computed PrayTimes for the given Location and Julian Date
     */
    public EasyList<Pray> getUpcomingPrays() {
        // Do Calculations
        Timestamp timestamp = Timestamp.NOW();
        this.setJDate(julianDate(timestamp.getYear(), timestamp.getMonth(), timestamp.getDay()));
        double lonDiff = getCurrentLocation().getLongitude() / (15.0 * 24.0);
        this.setJDate(this.getJDate() - lonDiff);
        // Put result in array
        Time[] computedPrayTimes = computeDayTimes();
        EasyList<Pray> prayTimes = new EasyList<>();
        for (int index = 0; index < computedPrayTimes.length; index++) {
            timestamp.set(Calendar.HOUR_OF_DAY, computedPrayTimes[index].getHours());
            timestamp.set(Calendar.MINUTE, computedPrayTimes[index].getMinutes());
            timestamp.set(Calendar.SECOND, 0);
            if (timestamp.isBefore(System.currentTimeMillis())) continue;
            prayTimes.add(new Pray(PrayType.valueOf(index), getPrayerNames().get(index), timestamp));
        }
        AManager.log(TAG, "getUpcomingPrays: In[%s] | PrayTimes[%s]",
                timestamp.getFormatted(FormatPatterns.DATETIME_NORMAL),
                prayTimes);
        return prayTimes;
    }

    // set custom values for calculation parameters
    private void setCustomParams(double[] params) {
        for (int i = 0; i < 5; i++) {
            if (params[i] == -1) {
                params[i] = methodParams.get(this.getCurrentLocation().getConfig().getCalculationMethod().ordinal())[i];
                methodParams.put(CalculationMethod.CUSTOM.ordinal(), params);
            } else methodParams.get(CalculationMethod.CUSTOM.ordinal())[i] = params[i];
        }
        this.getCurrentLocation().getConfig().setCalculationMethod(CalculationMethod.CUSTOM);
    }

    // Set the angle for calculating Fajr
    public void setFajrAngle(double angle) {
        double[] params = {angle, -1, -1, -1, -1};
        setCustomParams(params);
    }

    // Set the angle for calculating Maghrib
    public void setMaghribAngle(double angle) {
        double[] params = {-1, 0, angle, -1, -1};
        setCustomParams(params);

    }

    // Set the angle for calculating Isha
    public void setIshaAngle(double angle) {
        double[] params = {-1, -1, -1, 0, angle};
        setCustomParams(params);

    }

    // Set the minutes after Sunset for calculating Maghrib
    public void setMaghribMinutes(double minutes) {
        double[] params = {-1, 1, minutes, -1, -1};
        setCustomParams(params);

    }

    // Set the minutes after Maghrib for calculating Isha
    public void setIshaMinutes(double minutes) {
        double[] params = {-1, -1, -1, 1, minutes};
        setCustomParams(params);

    }

    // Convert double hours to 24h format
    public String floatToTime24(double time) {
        String result;
        if (Double.isNaN(time)) {
            return InvalidTime;
        }
        // Fix hours and minutes
        time = fixHour(time + 0.5 / 60.0); // add 0.5 minutes to round
        int hours = (int) Math.floor(time);
        double minutes = Math.floor((time - hours) * 60.0);

        // String format
        if ((hours >= 0 && hours <= 9) && (minutes >= 0 && minutes <= 9)) result = "0" + hours + ":0" + Math.round(minutes);
        else if ((hours >= 0 && hours <= 9)) result = "0" + hours + ":" + Math.round(minutes);
        else if ((minutes >= 0 && minutes <= 9)) result = hours + ":0" + Math.round(minutes);
        else result = hours + ":" + Math.round(minutes);
        return result;
    }

    // convert double hours to 12h format
    public String floatToTime12(double time, boolean noSuffix) {

        if (Double.isNaN(time)) {
            return InvalidTime;
        }

        time = fixHour(time + 0.5 / 60); // add 0.5 minutes to round
        int hours = (int) Math.floor(time);
        double minutes = Math.floor((time - hours) * 60);
        String suffix, result;
        if (hours >= 12) {
            suffix = "pm";
        } else {
            suffix = "am";
        }
        hours = ((((hours + 12) - 1) % (12)) + 1);
        /*hours = (hours + 12) - 1;
        int hrs = (int) hours % 12;
        hrs += 1;*/
        if (!noSuffix) {
            if ((hours >= 0 && hours <= 9) && (minutes >= 0 && minutes <= 9)) result = "0" + hours + ":0" + Math.round(minutes) + " " + suffix;
            else if ((hours >= 0 && hours <= 9)) result = "0" + hours + ":" + Math.round(minutes) + " " + suffix;
            else if ((minutes >= 0 && minutes <= 9)) result = hours + ":0" + Math.round(minutes) + " " + suffix;
            else result = hours + ":" + Math.round(minutes) + " " + suffix;
        } else {
            if ((hours >= 0 && hours <= 9) && (minutes >= 0 && minutes <= 9)) result = "0" + hours + ":0" + Math.round(minutes);
            else if ((hours >= 0 && hours <= 9)) result = "0" + hours + ":" + Math.round(minutes);
            else if ((minutes >= 0 && minutes <= 9)) result = hours + ":0" + Math.round(minutes);
            else result = hours + ":" + Math.round(minutes);
        }
        return result;

    }

    // convert double hours to 12h format with no suffix
    public String floatToTime12NS(double time) {
        return floatToTime12(time, true);
    }

    // ---------------------- Compute Prayer Times -----------------------
    // Compute prayer times at given julian date
    private double[] computeTimes(double[] times) {
        double[] t = dayPortion(times);
        double Fajr = this.computeTime(180 - methodParams.get(this.getCurrentLocation().getConfig().getCalculationMethod().ordinal())[0], t[0]);
        double Sunrise = this.computeTime(180 - 0.833, t[1]);
        double Dhuhr = this.computeMidDay(t[2]);
        double Asr = this.computeAsr(1 + this.getCurrentLocation().getConfig().getAsrMethod().ordinal(), t[3]);
        double Sunset = this.computeTime(0.833, t[4]);
        double Maghrib = this.computeTime(methodParams.get(this.getCurrentLocation().getConfig().getCalculationMethod().ordinal())[2], t[5]);
        double Isha = this.computeTime(methodParams.get(this.getCurrentLocation().getConfig().getCalculationMethod().ordinal())[4], t[6]);
        return new double[]{Fajr, Sunrise, Dhuhr, Asr, Sunset, Maghrib, Isha};
    }

    // compute prayer times at given julian date
    private Time[] computeDayTimes() {
        double[] times = {5, 6, 12, 13, 18, 18, 18}; // default times
        for (int i = 1; i <= this.getNumIterations(); i++) times = computeTimes(times);
        adjustTimes(times);
        tuneTimes(times);
        return adjustTimesFormat(times);
    }

    // adjust times in a prayer time array
    private void adjustTimes(double[] times) {
        for (int i = 0; i < times.length; i++) times[i] += this.getCurrentLocation().getTimezone() - this.getCurrentLocation().getLongitude() / 15;
        times[2] += this.dhuhrMinutes / 60f; // Dhuhr
        if (methodParams.get(this.getCurrentLocation().getConfig().getCalculationMethod().ordinal())[1] == 1) times[5] = times[4] + methodParams.get(this.getCurrentLocation().getConfig().getCalculationMethod().ordinal())[2] / 60; // Maghrib
        if (methodParams.get(this.getCurrentLocation().getConfig().getCalculationMethod().ordinal())[3] == 1) times[6] = times[5] + methodParams.get(this.getCurrentLocation().getConfig().getCalculationMethod().ordinal())[4] / 60; // Isha
        if (this.getCurrentLocation().getConfig().getHigherLatitude() != HigherLatitudesMethod.NONE) adjustHighLatTimes(times);
    }

    // Convert times array to long timestamp
    private Time[] adjustTimesFormat(double[] times) {
        Time[] calculatedPrayTimes = new Time[6];
        for (int i = 0; i < calculatedPrayTimes.length; i++) {
            calculatedPrayTimes[i] = floatToTime(times[i == 4 || i == 5 ? i + 1 : i]);
        }
        return calculatedPrayTimes;
    }

    private Time floatToTime(double time) {
        if (Double.isNaN(time)) return new Time(0, 0, 0); // Invalid time
        // Fix hours and minutes
        time = fixHour(time + 0.5 / 60.0); // add 0.5 minutes to round
        int hours = (int) Math.floor(time);
        double minutes = Math.floor((time - hours) * 60.0);
        return new Time(hours, (int) minutes, 0);
    }

    // adjust Fajr, Isha and Maghrib for locations in higher latitudes
    private void adjustHighLatTimes(double[] times) {
        double nightTime = timeDiff(times[4], times[1]); // sunset to sunrise

        // Adjust Fajr
        double FajrDiff = nightPortion(methodParams.get(this.getCurrentLocation().getConfig().getCalculationMethod().ordinal())[0]) * nightTime;

        if (Double.isNaN(times[0]) || timeDiff(times[0], times[1]) > FajrDiff) {
            times[0] = times[1] - FajrDiff;
        }

        // Adjust Isha
        double IshaAngle = (methodParams.get(this.getCurrentLocation().getConfig().getCalculationMethod().ordinal())[3] == 0) ? methodParams.get(this.getCurrentLocation().getConfig().getCalculationMethod().ordinal())[4] : 18;
        double IshaDiff = this.nightPortion(IshaAngle) * nightTime;
        if (Double.isNaN(times[6]) || this.timeDiff(times[4], times[6]) > IshaDiff) {
            times[6] = times[4] + IshaDiff;
        }

        // Adjust Maghrib
        double MaghribAngle = (methodParams.get(this.getCurrentLocation().getConfig().getCalculationMethod().ordinal())[1] == 0) ? methodParams.get(this.getCurrentLocation().getConfig().getCalculationMethod().ordinal())[2] : 4;
        double MaghribDiff = nightPortion(MaghribAngle) * nightTime;
        if (Double.isNaN(times[5]) || this.timeDiff(times[4], times[5]) > MaghribDiff) times[5] = times[4] + MaghribDiff;
    }

    // The night portion used for adjusting times in higher latitudes
    private double nightPortion(double angle) {
        double calc = 0;
        if (this.getCurrentLocation().getConfig().getHigherLatitude() == HigherLatitudesMethod.ANGLEBASED) calc = (angle) / 60.0;
        else if (this.getCurrentLocation().getConfig().getHigherLatitude() == HigherLatitudesMethod.MIDNIGHT) calc = 0.5;
        else if (this.getCurrentLocation().getConfig().getHigherLatitude() == HigherLatitudesMethod.ONESEVENTH) calc = 0.14286;
        return calc;
    }

    // convert hours to day portions
    private double[] dayPortion(double[] times) {
        for (int i = 0; i < 7; i++) times[i] /= 24;
        return times;
    }

    // Tune timings for adjustments
    // Set time offsets
    public void tune(int[] offsetTimes) {
        // offsetTimes length
        // should be 7 in order
        // of Fajr, Sunrise,
        // Dhuhr, Asr, Sunset,
        // Maghrib, Isha
        System.arraycopy(offsetTimes, 0, this.offsets, 0, offsetTimes.length);
    }

    private void tuneTimes(double[] times) {
        for (int i = 0; i < times.length; i++) times[i] = times[i] + this.offsets[i] / 60.0;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public PrayTimeCore setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
        return this;
    }

    public int[] getOffsets() {
        return offsets;
    }

    public PrayTimeCore setOffsets(int[] offsets) {
        this.offsets = offsets;
        return this;
    }

    public double getJDate() {
        return JDate;
    }

    public void setJDate(double jDate) {
        JDate = jDate;
    }

    private int getNumIterations() {
        return numIterations;
    }

    private void setNumIterations(int numIterations) {
        this.numIterations = numIterations;
    }

    public int getDhuhrMinutes() {
        return dhuhrMinutes;
    }

    public PrayTimeCore setDhuhrMinutes(int dhuhrMinutes) {
        this.dhuhrMinutes = dhuhrMinutes;
        return this;
    }

    public List<String> getPrayerNames() {
        return prayerNames;
    }
}
