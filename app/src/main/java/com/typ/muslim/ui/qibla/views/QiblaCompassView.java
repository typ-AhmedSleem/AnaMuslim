/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.qibla.views;

import static com.typ.muslim.enums.QiblaCompassMode.COMPASS_2D;
import static com.typ.muslim.enums.QiblaCompassMode.LIVE_COMPASS;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import androidx.annotation.Nullable;

import com.typ.muslim.R;
import com.typ.muslim.enums.QiblaCompassMode;
import com.typ.muslim.enums.SensorAccuracy;
import com.typ.muslim.interfaces.QiblaCompassCallback;
import com.typ.muslim.libs.iclib.QiblaUtils;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.managers.ResMan;
import com.typ.muslim.utils.MathUtils;

import java.util.Locale;

public class QiblaCompassView extends View implements SensorEventListener {

    // Statics
    private static final String TAG = "QiblaCompassView";
    private final float[] rotationMatrix = new float[9];
    private final float[] inclinationMatrix = new float[9];
    private final float[] orientationMatrix = new float[3];
    // (gravityMatrix) and (magneticMatrix) are the raw readings from accel and mag sensors
    private final float[] accelRawMatrix = new float[3];
    private final float[] magFieldRawMatrix = new float[3];
    // Necessary during drawing view
    public float alpha = 0.1f;
    private final Paint paint = new Paint();
    // Qibla settings
    private boolean isVibrationEnabled = false;
    // Runtime
    private boolean isReady = false;
    private Vibrator vibrator;
    private SensorManager sensorManager;
    private WindowManager windowManager;
    private SensorAccuracy currAccuracy = SensorAccuracy.UNRELIABLE;
    private final boolean isVibrating = false;
    private QiblaCompassMode currMode = COMPASS_2D;
    private float currNorthAngle = 0f, qiblaAngle = 0f, currQiblaAngle = 0f, currentRollAngle = 0f;
    // Listeners and Callbacks
    private @Nullable
    QiblaCompassCallback callback;

    public QiblaCompassView(Context context) {
        super(context);
    }

    public QiblaCompassView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public QiblaCompassView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void setupQiblaCompass() {
        if (isReady) return;
        // Calculate the qibla angle for current location
        this.qiblaAngle = (float) QiblaUtils.getCurrentLocationBearing(getContext());
//        this.qiblaAngle = 136.04663f;
        AManager.log(TAG, "Qibla Angle in Degrees: " + this.qiblaAngle);
        // Get SensorManager and Vibrator services
        if (vibrator == null) vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        if (windowManager == null) windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        if (sensorManager == null) sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        // Get working rotation sensor if available
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magneticSensor == null || accelerometerSensor == null) {
            // Rotation sensor isn't supported
            if (this.callback != null) this.callback.onCompassNotSupported();
            return;
        }
        // Register sensor event listeners
        this.sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_UI);
        this.sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        // Notify listener that compass is ready
        if (this.callback != null) {
            isReady = true;
            this.callback.onCompassReady();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float radius = Math.min(getHeight(), getWidth()) / 2.0f;
        // Draw base circle
        paint.setAntiAlias(true);
        paint.setColor(ResMan.getColor(getContext(), isArrowBearingToZero(this.currQiblaAngle) ? R.color.green : R.color.isha_bg));
        paint.setStyle(Paint.Style.FILL);
//        if (currMode == LIVE_COMPASS) {
//            // Live compass
//            paint.setColor(ResMan.getColor(getContext(), R.color.color_maghrib_isha_highlight));
//            canvas.drawCircle(radius, radius, radius, paint);
//            return;
//        } else
        canvas.drawCircle(radius, radius, radius, paint); // Normal 2D.
        /* Draw indicator arrows */
        paint.setColor(ResMan.getColor(getContext(), isArrowBearingToZero(this.currQiblaAngle) ? R.color.isha_bg : R.color.color_maghrib_isha_highlight));
        paint.setStrokeWidth(15f);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.FILL);
        // Draw center circle
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(radius, radius, radius / 10, paint);
        // North indicator line
        canvas.drawLine(radius, radius, radius, 40, paint);
//        final int northCX = (int) ((radius) * Math.cos(Math.toRadians(-(int) northAngle)));
//        final int northCY = (int) ((radius) * Math.sin(Math.toRadians(-(int) northAngle)));
//        paint.setColor(Color.GREEN);
//        canvas.drawCircle(northCX, northCY, 10, paint);
//        canvas.drawLine(radius, radius, northCX, northCY, paint);
//        final int qiblaCX = (int) ((radius) * Math.cos(Math.toRadians((int) currAngle)));
//        final int qiblaCY = (int) ((radius) * Math.sin(Math.toRadians((int) currAngle)));
//        paint.setColor(Color.YELLOW);
//        canvas.drawCircle(qiblaCX, qiblaCY, 10, paint);
//        canvas.drawLine(radius, radius, qiblaCX, qiblaCY, paint);
//        AManager.log(TAG, "R[%d] NI[%d,%d,%d] | QI[%d,%d,%d]", (int) radius, (int) northAngle, northCX, northCY, (int) currAngle, qiblaCX, qiblaCY);
        this.invalidate();
    }

    public void unregisterListeners() {
        // Unregister sensor event listener
        this.sensorManager.unregisterListener(this);
        this.callback = null;
    }

    public void registerListeners(QiblaCompassCallback listener) {
        // Register listeners
        this.callback = listener;
        this.setupQiblaCompass();
    }

    private boolean isArrowBearingToZero(float targetQiblaAngle) {
        return targetQiblaAngle == 360 || targetQiblaAngle >= 0 && targetQiblaAngle <= 1;
    }

    @Override
    protected void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);
        this.invalidate(); // Invalidate the while view and draw it again.
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Get and filter sensors readings
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magFieldRawMatrix[0] = event.values[0];
            magFieldRawMatrix[1] = event.values[1];
            magFieldRawMatrix[2] = event.values[2];
        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelRawMatrix[0] = event.values[0];
            accelRawMatrix[1] = event.values[1];
            accelRawMatrix[2] = event.values[2];
        }
        // Calculate rotation matrix using sensors readings
        SensorManager.getRotationMatrix(rotationMatrix, inclinationMatrix, accelRawMatrix, magFieldRawMatrix);
        SensorManager.getOrientation(rotationMatrix, orientationMatrix);
        // Calculate north angle
        float azimuth = (float) Math.toDegrees(-orientationMatrix[0]);
        if (azimuth < 0) azimuth += 360;
        float targetNorthAngle = MathUtils.fixAngle(azimuth + 360 - getDeviceOrientation());
        // Calculate qibla angle
        float targetQiblaAngle = MathUtils.fixAngle(targetNorthAngle + qiblaAngle);
        // Check if angle hasn't changed
        if (targetNorthAngle == currNorthAngle || targetQiblaAngle == currQiblaAngle) return;
        // Update roll angle (in radians)
        currentRollAngle = orientationMatrix[1];
        // Rotate compass
        RotateAnimation rotateAnim = new RotateAnimation(currQiblaAngle, targetQiblaAngle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnim.setFillAfter(true);
        rotateAnim.setDuration(100);
        QiblaCompassView.this.startAnimation(rotateAnim);
        // Update current runtime
        this.currNorthAngle = targetNorthAngle;
        this.currQiblaAngle = targetQiblaAngle;
        // Redraw the compass
        invalidate();
        // Check if compass mode is changed to another mode or not
        if (this.callback != null) {
            callback.onSensorChanged(this.currQiblaAngle, targetQiblaAngle, targetNorthAngle);
            final boolean isNewMode2D = (this.currentRollAngle <= 5.0f && accelRawMatrix[1] <= 5.0f); /*I Inverted the result of condition to report correct state*/
            switch (currMode) {
                case COMPASS_2D:
                    if (!isNewMode2D) {
                        this.currMode = LIVE_COMPASS;
                        this.callback.onCompassViewChanged(LIVE_COMPASS);
                        break;
                    }
                case LIVE_COMPASS:
                    if (isNewMode2D) {
                        this.currMode = COMPASS_2D;
                        this.callback.onCompassViewChanged(COMPASS_2D);
                        break;
                    }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Show alert to user when accuracy is low
        if (currAccuracy.ordinal() == accuracy) return;
        if (callback != null) this.callback.onAccuracyChanged(sensor.getType(), currAccuracy, SensorAccuracy.valueOf(accuracy));
        currAccuracy = SensorAccuracy.valueOf(accuracy);
        AManager.log(TAG, String.format(Locale.ENGLISH, "onAccuracyChanged: SENSOR[%s] - ACCURACY[%s]\n", sensor.getName(), SensorAccuracy.valueOf(accuracy)));
    }

    public int getDeviceOrientation() {
        int rotation = windowManager.getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
            case Surface.ROTATION_0:
            default:
                return 0;
        }
    }

    public float getCurrentBearing() {
        return currQiblaAngle;
    }

    public float getQiblaAngle() {
        return qiblaAngle;
    }

    public void disableVibration() {
        this.isVibrationEnabled = false;
    }

    public void enableVibration() {
        this.isVibrationEnabled = true;
    }

    public SensorAccuracy getCurrentAccuracy() {
        return this.currAccuracy;
    }
}
