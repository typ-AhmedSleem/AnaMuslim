/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.features.prays.interfaces

import com.typ.muslim.features.prays.models.Pray

interface PrayTimeCameListener {
    /**
     * Fired when CountDownView has finished counting to show azan or notify user
     * Returns the next pray from working activity without doing any operations inside view.
     *
     * @param pray The pray its time has come
     * @return The next [Pray].
     */
    fun onPrayTimeCame(pray: Pray): Pray
}
