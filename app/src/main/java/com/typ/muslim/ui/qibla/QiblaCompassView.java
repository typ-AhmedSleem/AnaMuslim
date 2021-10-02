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

import java.util.Locale;

public class QiblaCompassView extends View implements SensorEventListener {

    // Statics
    private static final String TAG = "QiblaCompassView";
    // Runtime
    private Vibrator vibrator;
    private QiblaCompassMode currMode;
    private SensorManager sensorManager;
    private boolean isVibrating = false;
    private final float[] rawRotationMatrix = new float[9];
    private final float[] cookedRotationMatrix = new float[3];
    private float northAngle = 0f, qiblaAngle = -1f, currAngle = 0f, currGravityY = 0f;
    private SensorAccuracy currAccuracy = SensorAccuracy.UNRELIABLE;
    // Qibla settings
    private boolean isVibrationEnabled = true;
    // Necessary during drawing view
    private final Paint paint = new Paint();
    // Listeners and Callbacks
    private @Nullable QiblaCompassCallback listener;

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
        // Calculate the qibla angle for current location
        this.qiblaAngle = (float) QiblaUtils.getCurrentLocationBearing(getContext());
        this.qiblaAngle = 136.04663f;
        if (this.qiblaAngle == -1f) this.qiblaAngle = (float) QiblaUtils.getCurrentLocationBearing(getContext());
        AManager.log(TAG, "Qibla Angle in Degrees: " + this.qiblaAngle);
        // Get SensorManager and Vibrator services
        if (this.sensorManager == null) this.sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        if (this.vibrator == null) this.vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        // Get working rotation sensor if available
        Sensor rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (rotationSensor == null) {
            // Rotation sensor isn't supported
            if (this.listener != null) this.listener.onCompassNotSupported();
            return;
        }
        // Register sensor event listeners
        this.sensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_UI);
        if (accelerometerSensor != null) this.sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        // Notify listener that compass is ready
        if (this.listener != null) this.listener.onCompassReady();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float radius = Math.min(getHeight(), getWidth()) / 2.0f;
        float textSize = DisplayUtils.sp2px(getContext(), 25);
        // Draw base circle
        paint.setAntiAlias(true);
        paint.setColor(this.isArrowBearingToZero(this.currAngle) ? Color.GREEN : Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(radius, radius, radius, paint);
        // Draw north indicator arrow
        paint.setColor(this.isArrowBearingToZero(this.currAngle) ? Color.WHITE : Color.YELLOW);
        paint.setStrokeWidth(20f);
        canvas.drawLine(radius, radius, radius, 0, paint);
        // Draw rotation angle on compass
        paint.setColor(this.isArrowBearingToZero(this.currAngle) ? Color.BLACK : Color.WHITE);
        paint.setTextSize(textSize);
        canvas.drawText(String.valueOf(Math.round(this.currAngle)), radius - textSize / 2, radius + radius / 2, paint);
        this.invalidate();
    }

    public QiblaCompassView unRegisterListeners() {
        // Unregister sensor event listener
        this.sensorManager.unregisterListener(this);
        this.listener = null;
        return this;
    }

    public QiblaCompassView registerListeners(QiblaCompassCallback listener) {
        // Register listeners
        this.listener = listener;
        this.setupQiblaCompass();
        return this;
    }

    private float fixAngle(float angle) {
        return angle > 360 ? angle - 360 : angle < 0 ? angle + 360 : angle;
    }

    private boolean isArrowBearingToZero(float targetQiblaAngle) {
        return (targetQiblaAngle == 0 || targetQiblaAngle == 360 || (targetQiblaAngle >= 0 && targetQiblaAngle <= 1) || (targetQiblaAngle >= 360 && targetQiblaAngle <= 1));
    }

    @Override
    protected void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);
        this.invalidate(); // Invalidate the while view and draw it again.
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // todo -> Rotate QiblaView (Qibla & North) indicators
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            // Calculate orientation
            SensorManager.getRotationMatrixFromVector(rawRotationMatrix, event.values);
            SensorManager.getOrientation(rawRotationMatrix, cookedRotationMatrix);
            // Check if compass mode is changed to another mode or not
            if (this.listener != null) {
                final boolean isNewMode2D = (this.currGravityY > 5.0f && event.values[1] <= 5.0f); /*I Inverted the result of condition to report correct state*/
                if (currMode == COMPASS_2D && !isNewMode2D) {
                    this.currMode = COMPASS_3D;
                    this.listener.onCompassViewChanged(currMode);
                    return;
                }
            }
            // Rotate compass
            float targetNorthAngle = -(float) Math.toDegrees(cookedRotationMatrix[0]);
            float targetQiblaAngle = this.fixAngle(this.northAngle + this.qiblaAngle);
            RotateAnimation rotateAnim = new RotateAnimation(this.currAngle, targetQiblaAngle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnim.setFillAfter(true);
            rotateAnim.setDuration(250);
            QiblaCompassView.this.startAnimation(rotateAnim);
            // Vibrate sensor
            if (this.isArrowBearingToZero(targetQiblaAngle) && this.isVibrationEnabled) {
                if (!isVibrating) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) this.vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
                    else this.vibrator.vibrate(300);
                }
                this.isVibrating = true;
            } else this.isVibrating = false;
            // Update current angles at runtime
            AManager.log(TAG, String.format(Locale.ENGLISH, "ROTATING NORTH [%d]->[%d] | ROTATING QIBLA [%d]->[%d]\n", Math.round(northAngle), Math.round(targetNorthAngle), Math.round(currAngle), Math.round(targetQiblaAngle)));
            this.northAngle = targetNorthAngle;
            this.currAngle = targetQiblaAngle;
        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Check device Y-Axis
            AManager.log(TAG, "ACCELEROMETER: GravityInX -> " + event.values[1]);
            // Update current gravity at runtime
            this.currGravityY = event.values[1];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Show alert to user when accuracy is low
        if (this.listener != null) this.listener.onAccuracyChanged(sensor.getType(), this.currAccuracy, SensorAccuracy.valueOf(accuracy));
        AManager.log(TAG, String.format(Locale.ENGLISH, "onAccuracyChanged: SENSOR[%s] - ACCURACY[%d]\n", sensor.getName(), accuracy));
    }

}
