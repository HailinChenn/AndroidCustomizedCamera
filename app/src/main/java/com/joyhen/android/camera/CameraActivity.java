package com.joyhen.android.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity {
    public static final String TAG = "CameraActivity";

    private Camera mCamera = null;
    private CameraPreview mPreview = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.camera);

        createCamera();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        // we need to create the camera again.
        createCamera();;
    }

    @Override
    protected void onStop() {
        super.onStop();
        // we need to release the camera.
        releaseCamera();
    }

    /**
     * Create a camera.
     */
    private void createCamera() {
        mCamera = CameraUtil.getCameraInstance(Camera.CameraInfo.CAMERA_FACING_FRONT);

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera, mPreviewCallback, getResources().getConfiguration().orientation);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
    }


    /**
     * Release a camera.
     */
    private void releaseCamera() {
        // If you set a callback previously.
        mCamera.setPreviewCallback(null);
        // You must do this, or the app will throws an exception.
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;

        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.removeView(mPreview);
        mPreview = null;
    }

    private int count = 0;
    private Camera.PreviewCallback mPreviewCallback = new Camera.PreviewCallback() {

        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            // just for simple test to save image.
            count++;
            if (count < 200) {
                return;
            }
            count = 0;
            Camera.Parameters parameters = camera.getParameters();

            try {
                YuvImage yuvImage = new YuvImage(data,
                        parameters.getPreviewFormat(),
                        parameters.getPreviewSize().width,
                        parameters.getPreviewSize().height, null);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                yuvImage.compressToJpeg(
                        new Rect(0, 0, parameters.getPreviewSize().width,
                                parameters.getPreviewSize().height), 90, baos);
                byte cdata[] = baos.toByteArray();
                Bitmap bitmap = BitmapFactory.decodeByteArray(cdata, 0,
                        cdata.length);
                baos.close();

                // camera image needs to be rotated by angles.

                Log.d(TAG, "rotation:" + CameraUtil.mOrientationOfCameraImage);

                Matrix matrix = new Matrix();
                matrix.postRotate(CameraUtil.mOrientationOfCameraImage);
                Bitmap rBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                bitmap.recycle();


                if (null != rBitmap) {

                    ByteArrayOutputStream rbaos = new ByteArrayOutputStream();
                    BufferedOutputStream bos = new BufferedOutputStream(rbaos);
                    rBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);

                    bos.flush();
                    bos.close();
                    CameraUtil.saveImage(rbaos.toByteArray());
                    rbaos.close();
                    rBitmap.recycle();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }

        }
    };


//    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
//
//        @Override
//        public void onPictureTaken(byte[] data, Camera camera) {
//            CameraUtil.saveImage(data);
//        }
//    };
}
