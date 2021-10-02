/**
 * Copyright (C) 2015 Fikrul Arif
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.typ.muslim.libs.iclib;

/*
 * Convention for internal/basic formula:
 * - short param name, mid-length method name
 * - no structure/class, only basic/primitives known in majority of languages
 * - procedural
 * - constants only for very limited choices (asr shadow ratio)
 * - no param (type, value, range, etc) checking
 * - no exception raising/throwing
 * - use decimal separator for number in floating-point-aware division
 * - no method overloading
 * - casing follows language convention
 * Note:
 * - all angles are "currently" in degrees instead of radians
 * - all length (actually only h) are in meters
 * - all times are in hours
 * - month, day, and weekday start from 1 (Muharram, January, Ahad/Sunday)
 */

/**
 * Basic formula of calculation.
 *
 * <p>Prayer time calculation functions return numbers in hours, 0 means midnight,
 * 13.5 means 13:30, etc. They have some of these parameters, each of them is
 * number:
 * <ul>
 *   <li>lat - latitude in degrees
 *   <li>lng - longitude in degrees
 *   <li>h - observer's altitude/height from sealevel in meters
 *   <li>tz - timezone
 *   <li>et - Equation of Time (eq_time function)
 *   <li>ds - declination of the Sun (decl_sun function)
 *   <li>tZuhr - Zuhr time in hours (zuhr function)
 *   <li>fajrAngle - angle for Fajr time
 *   <li>ishaAngle - angle for Isha time
 * </ul>

 * @author fikr4n
 *
 */
public class Formula {

    public static final int ASR_RATIO_MAJORITY = 1;
    public static final int ASR_RATIO_HANAFI = 2;

    public static double zuhr(double lng, double tz, double et) {
        return 12 + tz - lng / 15.0 - et;
    }

    public static double asr(double tZuhr, double lat, double ds, double asrRatio) {
        double alt = acotDeg(asrRatio + tanDeg(Math.abs(ds - lat))); // altitude of the sun
        return tZuhr + hourAngle(lat, alt, ds) / 15.0;
    }

    public static double maghrib(double tZuhr, double lat, double ds, double h) {
        double alt = -0.8333 - 0.0347 * Math.sqrt(h);
        return tZuhr + hourAngle(lat, alt, ds) / 15.0;
    }

    public static double isha(double tZuhr, double lat, double ds, double ishaAngle) {
        double alt = -ishaAngle;
        return tZuhr + hourAngle(lat, alt, ds) / 15.0;
    }

    public static double fajr(double tZuhr, double lat, double ds, double fajrAngle) {
        double alt = -fajrAngle;
        return tZuhr - hourAngle(lat, alt, ds) / 15.0;
    }

    public static double sunrise(double tZuhr, double lat, double ds, double h) {
        double alt = -0.8333 - 0.0347 * Math.sqrt(h); // equals to maghrib
        return tZuhr - hourAngle(lat, alt, ds) / 15.0;
    }

    /**
     * Return hour angle in degrees, return positive or negative infinity if calculation
     * cannot be performed.
     *
     * <p>Negative and positive infinity has special meaning.
     */
    public static double hourAngle(double lat, double alt, double ds) {
        double cosHa = ((sinDeg(alt) - sinDeg(lat) * sinDeg(ds))
                / (cosDeg(lat) * cosDeg(ds)));
        if (cosHa < -1)
            return Double.NEGATIVE_INFINITY;
        else if (cosHa > 1)
            return Double.POSITIVE_INFINITY;
        else
            return acosDeg(cosHa);
    }

    /**
     * Return Equation of Time in hours.
     */
    public static double eqTime(double jd) {
        double u = (jd - 2451545) / 36525.0;
        double l0 = 280.46607 + 36000.7698 * u; // average longitude of the sun in degrees
        return (
                -(1789 + 237 * u) * sinDeg(l0)
                        - (7146 - 62 * u) * cosDeg(l0)
                        + (9934 - 14 * u) * sinDeg(2 * l0)
                        - (29 + 5 * u) * cosDeg(2 * l0)
                        + (74 + 10 * u) * sinDeg(3 * l0)
                        + (320 - 4 * u) * cosDeg(3 * l0)
                        - 212 * sinDeg(4 * l0)) / 60000.0;
    }

    /**
     * Return declination of the Sun in degrees.
     */
    public static double declSun(double jd) {
        double t = 2 * Math.PI * (jd - 2451545) / 365.25; // angle of date
        return (0.37877
                + 23.264 * sinDeg(57.297 * t - 79.547)
                + 0.3812 * sinDeg(2 * 57.297 * t - 82.682)
                + 0.17132 * sinDeg(3 * 57.297 * t - 59.722));
    }

    /**
     * Return Julian Day of a Gregorian or Julian date.
     *
     * <p>Negative Julian Day (i.e. y < -4712 or 4713 BC) is not supported.
     *
     * @param y year
     * @param m month [1..12]
     * @param d day [1..31]
     */
    public static double gregorianToJd(int y, int m, int d) {
        if (y < -4712) throw new IllegalArgumentException("year (" + y + ") < -4712");

        if (m <= 2) {
            m += 12;
            y -= 1;
        }
        double b;
        if (y > 1582 || (y == 1582 && (m > 10 || (m == 10 && d >= 15)))) {
            // first gregorian is 15-oct-1582
            double a = Math.floor(y / 100.0);
            b = 2 + Math.floor(a / 4.0) - a;
        } else { // invalid dates (5-14) are also considered as julian
            b = 0;
        }
        double abs_jd = (1720994.5 + Math.floor(365.25 * y) + Math.floor(30.6001 * (m + 1))
                + d + b);
        return abs_jd;
    }

    /**
     * Return Gregorian or Julian date of Julian Day.
     *
     * <p>Negative Julian Day < -0.5 is not supported.
     *
     * @return {year, month [1..12], day [1..31]}
     */
    public static int[] jdToGregorian(double jd) {
        if (jd < -0.5) throw new IllegalArgumentException("Julian Day (" + jd + ") < -0.5");

        double jd1 = jd + 0.5;
        double z = Math.floor(jd1);
        double f = jd1 - z;
        double a;
        if (z < 2299161) {
            a = z;
        } else {
            double aa = Math.floor((z - 1867216.25) / 36524.25);
            a = z + 1 + aa - Math.floor(aa / 4.0);
        }
        double b = a + 1524;
        double c = Math.floor((b - 122.1) / 365.25);
        double d = Math.floor(365.25 * c);
        double e = Math.floor((b - d) / 30.6001);
        int day = (int) (b - d - Math.floor(30.6001 * e) + f);
        int month = (int) (e < 14 ? e - 1 : e - 13);
        int year = (int) (month <= 2 ? c - 4715 : c - 4716);
        return new int[]{year, month, day};
    }

    /**
     * Return weekday of Julian Day
     *
     * @return weekday [1..7] where 1 is Ahad/Sunday
     */
    public static int jdToWeekday(double jd) {
        return (int) Math.floor(jd + 1.5) % 7 + 1;
    }

    /**
     * Return Julian Day with added hours.
     */
    public static double adjustJdHour(double jd, double hours) {
        return jd + hours / 24.0;
    }

    /**
     * Return qibla direction in degrees from the north (clock-wise).
     *
     * @param lat latitude in degrees
     * @param lng longitude in degrees
     * @return 0 means north, 90 means east, 270 means west, etc
     */
    public static double qibla(double lat, double lng) {
        double lngA = 39.82616111;
        double latA = 21.42250833;
        double deg = atan2Deg(sinDeg(lngA - lng), cosDeg(lat) * tanDeg(latA) - sinDeg(lat) * cosDeg(lngA - lng));
        return deg >= 0 ? deg : deg + 360;
    }

    private static double sinDeg(double deg) {
        return Math.sin(Math.toRadians(deg));
    }

    private static double cosDeg(double deg) {
        return Math.cos(Math.toRadians(deg));
    }

    private static double acosDeg(double x_r) {
        return Math.toDegrees(Math.acos(x_r));
    }

    private static double tanDeg(double deg) {
        return Math.tan(Math.toRadians(deg));
    }

    private static double acotDeg(double x_y) {
        return Math.toDegrees(Math.atan(1.0 / x_y));
    }

    private static double atan2Deg(double y, double x) {
        return Math.toDegrees(Math.atan2(y, x));
    }
}
