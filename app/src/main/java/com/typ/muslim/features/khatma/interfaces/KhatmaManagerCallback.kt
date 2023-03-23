/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.features.khatma.interfaces

import com.typ.muslim.features.khatma.models.Khatma
import com.typ.muslim.features.khatma.models.KhatmaWerd

interface KhatmaManagerCallback {

    /**
     * Called when KhatmaManager instance is created initializing its constructor.
     */
    fun onPrepareManager()

    /**
     * Called when KhatmaManager puts an Khatma under its management
     *
     * @param khatma The under manager management khatma
     */
    fun onManageKhatma(khatma: Khatma)

    /**
     * Called when KhatmaManager deletes a khatma or
     */
    fun onNoActiveKhatma()

    /**
     * Called when the currently under-management active khatma has made a new progress on it.
     */
    fun onProgressUpdated(nextWerd: KhatmaWerd)

    /**
     * Called when KhatmaManager switches from this khatma to another one
     */
    fun onSwitchKhatma(khatma: Khatma)

    fun onFinishKhatma()

}