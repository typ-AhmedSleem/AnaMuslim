/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.interfaces;

import androidx.annotation.NonNull;

import com.typ.muslim.models.Khatma;

public interface KhatmaManagerCallback {


    /**
     * Called when KhatmaManager instance is created and is in constructing and initializing state
     */
    void onPrepareManager();

    /**
     * Called when KhatmaManager puts an Khatma under its management
     *
     * @param underManagementKhatma The under manager management khatma
     */
    void onPutUnderManagement(@NonNull Khatma underManagementKhatma);

    /**
     * Called when KhatmaManager hasn't found any active khatma to put under management
     */
    void onNoActiveKhatmaFound();

    /**
     * Called when the currently under-management active khatma has made a new progress on it.
     */
    void onKhatmaProgressUpdated();

    /**
     * Called when KhatmaManager has released under-management khatma from being managed anymore
     */
    void onKhatmaReleased();

}
