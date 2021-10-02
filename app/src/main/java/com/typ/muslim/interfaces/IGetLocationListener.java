/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.interfaces;

import com.typ.muslim.models.Location;

public interface IGetLocationListener {

    void onGetLocationSucceed(Location location);
    void onPermissionDenied();
    void onGetLocationFailed();

}
