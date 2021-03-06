package com.joyhen.android.camera;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

/**
 * Created by chenhailin on 16/1/28.
 *
 * It used to display the live image data coming from a camera.
 */
public final class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "CameraPreview";
    private SurfaceHolder mHolder = null;
    private Camera mCamera = null;
    private int mOrientation = Configuration.ORIENTATION_PORTRAIT;
    private Camera.PreviewCallback mPreviewCallback = null;

    public CameraPreview(Context context, Camera camera, Camera.PreviewCallback callback, int orientation) {
        super(context);
        mCamera = camera;
        mPreviewCallback = callback;
        mOrientation = orientation;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = this.getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {

            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        int calcWidth = 0;
        int calcHeight = 0;
        Camera.Parameters params = mCamera.getParameters();
        List<Camera.Size> supportedPreviewSizes = params.getSupportedPreviewSizes();
        for (Camera.Size s : supportedPreviewSizes) {
//            Log.d(TAG, "==(" + s.width + "," + s.height + ")");
            if (s.width <= width && s.height <= height) {
                if (s.width >= calcWidth && s.height >= calcHeight) {
                    calcWidth = s.width;
                    calcHeight = s.height;
                }
            }
        }
        Log.d(TAG, "PreviewSize:(" + calcWidth + "," + calcHeight + ")");
//        params.setPreviewSize(calcWidth, calcHeight);
        params.setPreviewSize(calcHeight, calcWidth);
        mCamera.setParameters(params);


        CameraUtil.setCameraDisplayOrientation((Activity)getContext(), 0, mCamera);

        // start preview with new settings
        try {
            mCamera.setPreviewCallback(mPreviewCallback);
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }
}
