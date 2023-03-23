/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.qibla;

import static com.typ.muslim.enums.QiblaCompassMode.COMPASS_2D;
import static com.typ.muslim.enums.QiblaCompassMode.COMPASS_3D;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import androidx.annotation.Nullable;

import com.typ.muslim.enums.QiblaCompassMode;
import com.typ.muslim.enums.SensorAccuracy;
import com.typ.muslim.interfaces.QiblaCompassCallback;
import com.typ.muslim.libs.iclib.QiblaUtils;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.utils.DisplayUtils;
import com.typ.muslim.utils.LowPassFilter;
import com.typ.muslim.utils.MathUtils;

import java.util.Locale;

public class QiblaCompassView2 extends View implements SensorEventListener {

    // Statics
    private static final String TAG = "QiblaCompassView";
    private final float[] rotationMatrix = new float[9];
    private final float[] gravityMatrix = new float[3];
    private final float[] magneticMatrix = new float[3];
    private final float[] orientationMatrix = new float[3];
    // Necessary during drawing view
    private final Paint paint = new Paint();
    // Qibla settings
    private final boolean isVibrationEnabled = true;
    // Runtime
    private SensorManager sensorManager;
    private Vibrator vibrator;
    private boolean isVibrating = false;
    private QiblaCompassMode currMode;
    private float northAngle = 0f, qiblaAngle = -1f, currAngle = 0f, currGravityY = 0f;
    private SensorAccuracy currAccuracy;
    // Listeners and Callbacks
    private @Nullable
    QiblaCompassCallback callback;

    public QiblaCompassView2(Context context) {
        super(context);
    }

    public QiblaCompassView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public QiblaCompassView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void setupQiblaCompass() {
        // Calculate the qibla angle for current location
        this.qiblaAngle = (float) QiblaUtils.getCurrentLocationBearing(getContext());
        this.qiblaAngle = 136.04663f;
        AManager.log(TAG, "Qibla Angle in Degrees: " + this.qiblaAngle);
        // Get SensorManager and Vibrator services
        if (this.sensorManager == null)
            this.sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        if (this.vibrator == null)
            this.vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        // Get working rotation sensor if available
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magneticSensor == null || accelerometerSensor == null) {
            // Rotation sensor isn't supported
            if (this.callback != null) this.callback.onCompassNotSupported();
            return;
        }
        // Register sensor event listeners
        this.sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_FASTEST);
        this.sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);
        // Notify listener that compass is ready
        if (this.callback != null) this.callback.onCompassReady();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float radius = Math.min(getHeight(), getWidth()) / 2.0f;
        float textSize = DisplayUtils.sp2px(getContext(), 10);
        // Draw base circle
        paint.setAntiAlias(true);
        paint.setColor(this.isArrowBearingToZero(this.currAngle) ? Color.GREEN : Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(radius, radius, radius, paint);
        // Draw qibla indicator arrow
        paint.setStrokeWidth(20f);
        paint.setColor(this.isArrowBearingToZero(this.currAngle) ? Color.WHITE : Color.YELLOW);
        canvas.drawLine(radius, radius, radius, 0, paint);
        // Draw rotation angle text on compass
        paint.setColor(this.isArrowBearingToZero(this.currAngle) ? Color.GREEN : Color.WHITE);
        paint.setTextSize(textSize);
        canvas.drawText(String.valueOf((int) MathUtils.fixAngle(this.currAngle)), radius - textSize / 2, radius + radius / 2, paint);
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
        final float[] smoothedValues;
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            smoothedValues = LowPassFilter.filter(0.05f, event.values, gravityMatrix);
            this.gravityMatrix[0] = smoothedValues[0];
            this.gravityMatrix[1] = smoothedValues[1];
            this.gravityMatrix[2] = smoothedValues[2];
            this.currGravityY = gravityMatrix[1];
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            smoothedValues = LowPassFilter.filter(0.05f, event.values, magneticMatrix);
            this.magneticMatrix[0] = smoothedValues[0];
            this.magneticMatrix[1] = smoothedValues[1];
            this.magneticMatrix[2] = smoothedValues[2];
        }
        // Calculate rotation matrix using sensors readings
        boolean hasSucceed = SensorManager.getRotationMatrix(rotationMatrix, null, gravityMatrix, magneticMatrix);
        if (hasSucceed) SensorManager.getOrientation(rotationMatrix, orientationMatrix);
        // Check if compass mode is changed to another mode or not
        if (this.callback != null) {
            final boolean isNewMode2D = (this.currGravityY > 5.0f && gravityMatrix[1] <= 5.0f); /*I Inverted the result of condition to report correct state*/
            if (currMode == COMPASS_2D && !isNewMode2D) {
                this.currMode = COMPASS_3D;
                this.callback.onCompassViewChanged(currMode);
                return;
            }
        }
        // Rotate compass
        float targetNorthAngle = (float) Math.toDegrees(-orientationMatrix[0]);
        float targetQiblaAngle = MathUtils.fixAngle(this.northAngle + this.qiblaAngle);
        RotateAnimation rotateAnim = new RotateAnimation(this.currAngle, targetQiblaAngle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnim.setFillEnabled(false);
        rotateAnim.setDuration(50);
        QiblaCompassView2.this.startAnimation(rotateAnim);
        // Vibrate sensor
        if (this.isArrowBearingToZero(targetQiblaAngle) && this.isVibrationEnabled) {
            if (!isVibrating) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    this.vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
                else this.vibrator.vibrate(300);
            }
            this.isVibrating = true;
        } else this.isVibrating = false;
        // Notify callback with new sensor values
        if (callback != null)
            callback.onSensorChanged(this.currAngle, targetQiblaAngle, targetNorthAngle);
        // Update current runtime
//		AManager.log(TAG, String.format(Locale.ENGLISH, "ROTATING NORTH [%d]->[%d] | ROTATING QIBLA [%d]->[%d]\n", Math.round(northAngle), Math.round(targetNorthAngle), Math.round(currAngle), Math.round(targetQiblaAngle)));
        this.northAngle = targetNorthAngle;
        this.currAngle = targetQiblaAngle;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Show alert to user when accuracy is low
        if (this.callback != null)
            this.callback.onAccuracyChanged(sensor.getType(), this.currAccuracy, SensorAccuracy.valueOf(accuracy));
        AManager.log(TAG, String.format(Locale.ENGLISH, "onAccuracyChanged: SENSOR[%s] - ACCURACY[%d]\n", sensor.getName(), accuracy));
    }

}