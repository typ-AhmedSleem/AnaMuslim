/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.interfaces;

import com.typ.muslim.models.Pray;

public interface PrayTimeCameListener {

    /**
     * Fired when CountDownView has finished counting to show azan or notify user
     * Returns the next pray from working activity without doing any operations inside view.
     *
     * @param pray The pray its time has come
     * @return The next {@link Pray}.
     */
    Pray onPrayTimeCame(Pray pray);

}
