package com.typ.muslim.features.prays;

import com.typ.muslim.features.prays.enums.Prays;
import com.typ.muslim.enums.PrayNotifyMethod;

public interface PrayNotifyMethodChangedCallback {

	void onPrayNotifyMethodChanged(Prays forPray, PrayNotifyMethod newMethod);

}
