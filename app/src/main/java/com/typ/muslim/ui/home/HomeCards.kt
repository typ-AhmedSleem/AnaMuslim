package com.typ.muslim.ui.home

import com.typ.muslim.ui.calendar.dashboard.HijriDashboardCard
import com.typ.muslim.ui.khatma.dashboard.KhatmaDashboardCard
import com.typ.muslim.ui.names.AllahNamesDashboardCard
import com.typ.muslim.ui.prays.NextPrayDashboardCard
import com.typ.muslim.ui.tasbeeh.TasbeehDashboardCard
import com.typ.muslim.ui.tracker.dashboard.TrackerDashboardCard

class HomeCards(
    val nextPray: NextPrayDashboardCard,
    val khatma: KhatmaDashboardCard,
    val tasbeeh: TasbeehDashboardCard,
    val hijriCalendar: HijriDashboardCard,
    val prayTracker: TrackerDashboardCard,
    val namesOfAllah: AllahNamesDashboardCard
) {

    val listed: Array<DashboardCard> by lazy {
        arrayOf(
            nextPray,
            khatma,
            tasbeeh,
            hijriCalendar,
            prayTracker,
            namesOfAllah
        )
    }

}
