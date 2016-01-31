package com.joyhen.android.camera;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chenhailin on 16/1/28.
 * <p/>
 * This is a helper class for camera.
 */
public final class CameraUtil {
    public static final String TAG = "CameraUtil";

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    public static int mOrientationOfCameraImage = 0;

    private CameraUtil() {
    }


    /**
     * Get the number of cameras that the android device owned.
     *
     * @return number of cameras.
     */
    public static int getNumberOfCameras() {
        return Camera.getNumberOfCameras();
    }

    /**
     * Check if this device has a camera.
     *
     * @param context
     * @return true if the device has a camera or return false.
     */
    public static boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * A safe way to get an instance of the Camera object.
     *
     * @param facing {Camera.CameraInfo.CAMERA_FACING_FRONT, Camera.CameraInfo.CAMERA_FACING_BACK}
     * @return a camera instance if the device has a camera of specified facing
     * and the camera is not in use, or will return null
     */
    public static Camera getCameraInstance(int facing) {

        if (facing != Camera.CameraInfo.CAMERA_FACING_BACK && facing !=
                Camera.CameraInfo.CAMERA_FACING_FRONT) {
            return null;
        }

        Camera camera = null;
        int number = CameraUtil.getNumberOfCameras();
        for (int i = 0; i < number; i++) {
            Camera.CameraInfo ci = new Camera.CameraInfo();
            Camera.getCameraInfo(i, ci);
            if (ci.facing == facing) {
                try {
                    camera = Camera.open(i);
                    mOrientationOfCameraImage = ci.orientation;
//                    Log.d("Orientation:", "" + ci.orientation);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }

        return camera;
    }


    /**
     * Create a file Uri for saving an image or video
     *
     * @param type MEDIA_TYPE_IMAGE or MEDIA_TYPE_VIDEO
     * @return a uri
     */
    public static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     *
     * @param type MEDIA_TYPE_IMAGE or MEDIA_TYPE_VIDEO
     * @return a file
     */
    public static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    /**
     * @param data Image data
     * @return true if the data can be saved to image, or return false.
     */
    public static boolean saveImage(byte[] data) {
        File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (pictureFile == null) {
            Log.d(TAG, "Error creating media file, check storage permissions:");
            return false;
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
        return false;
    }


    /**
     * http://developer.android.com/intl/zh-cn/reference/android/hardware/Camera.html
     *
     * @param activity
     * @param cameraId
     * @param camera
     */
    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }
}
