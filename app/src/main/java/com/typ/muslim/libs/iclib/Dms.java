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

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Locale;

/**
 * Representation of angle in degree-minute-second.
 *
 * @author fikr4n
 */
public class Dms implements Serializable, Comparable<Dms> {

    private static final long serialVersionUID = 1L;

    public final int degree, minute;
    public final double second;

    public Dms(double degree) {
        double seconds = (degree * 3600);
        this.degree = (int) (seconds / 3600);
        this.minute = (int) (seconds % 3600 / 60);
        this.second = seconds % 60;
    }

    public String toString(int prec) {
        int d = degree;
        int m = minute;
        double s = second;
        if (d < 0 || m < 0 || s < 0)
            return String.format(Locale.getDefault(), "-%d° %d′ %." + prec + "f″", -d, -m, -s);
        else
            return String.format(Locale.getDefault(), "%d° %d′ %." + prec + "f″", d, m, s);
    }

    @NotNull
    @Override
    public String toString() {
        return toString(0);
    }

    @Override
    public int compareTo(Dms o) {
        int c = Integer.compare(degree, o.degree);
        if (c != 0) return c;
        c = Integer.compare(minute, o.minute);
        if (c != 0) return c;
        return Double.compare(second, o.second);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Dms && compareTo((Dms) obj) == 0;
    }

    @Override
    public int hashCode() {
        int h = degree;
        h = 31 * h + minute;
        long sl = Double.doubleToRawLongBits(second);
        h = 31 * h + (int) sl;
        h = 31 * h + (int) (sl >> 32);
        return h;
    }
}
