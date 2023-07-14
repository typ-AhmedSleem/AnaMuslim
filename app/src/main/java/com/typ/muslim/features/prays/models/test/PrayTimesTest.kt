package com.typ.muslim.features.prays.models.test

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.typ.muslim.features.prays.PrayerManager
import com.typ.muslim.features.prays.enums.PrayType
import com.typ.muslim.features.prays.models.PrayTimes
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class PrayTimesTest{

    private lateinit var ctx: Context
    private lateinit var prays: PrayTimes

    @Before
    fun setUp() {
//        ctx = InstrumentationRegistry.getInstrumentation().context
        ctx = ApplicationProvider.getApplicationContext()
        prays = PrayerManager.getTodayPrays(ctx)
    }

    @Test
    fun praysRetrieved() {
        Assert.assertNotNull(prays)

        val arrPrays = prays.toArray()
        val arrPraysNoSunrise = prays.toArray()
        Assert.assertNotNull(arrPrays)
    }

    @Test
    fun getFajr() {
        Assert.assertNotNull(prays.fajr)
        Assert.assertEquals(prays.fajr.type, PrayType.FAJR)
    }

    @Test
    fun getSunrise() {
        Assert.assertNotNull(prays.sunrise)
        Assert.assertEquals(prays.sunrise.type, PrayType.SUNRISE)
    }

    @Test
    fun getDhuhr() {
        Assert.assertNotNull(prays.dhuhr)
        Assert.assertEquals(prays.dhuhr.type, PrayType.DHUHR)
    }

    @Test
    fun getAsr() {
        Assert.assertNotNull(prays.asr)
        Assert.assertEquals(prays.asr.type, PrayType.ASR)
    }

    @Test
    fun getMaghrib() {
        Assert.assertNotNull(prays.maghrib)
        Assert.assertEquals(prays.maghrib.type, PrayType.MAGHRIB)
    }

    @Test
    fun getIsha() {
        Assert.assertNotNull(prays.isha)
        Assert.assertEquals(prays.isha.type, PrayType.ISHA)
    }

    @Test
    fun get() {
        Assert.assertNotNull((0..5).random())
    }

    @Test
    fun toArray() {
        Assert.assertEquals(prays.toArray().size, 6)

    }

    @Test
    fun toArrayNoSunrise() {
        Assert.assertEquals(prays.toArrayNoSunrise().size, 5)
    }

    @Test
    fun testToString() {
        Assert.assertNotNull(prays.toString())
    }

    @Test
    fun newBuilder() {
    }
}