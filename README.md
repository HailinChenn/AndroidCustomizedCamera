# AndroidCustomizedCamera
* ##Step One:  
  You should read the chapter about "android:screenOrientation" in "<activity> element,  
  [reference url](http://developer.android.com/intl/zh-cn/guide/topics/manifest/activity-element.html)

* >##Step Two:  
  >You should get the "natural orientation" of the device.  
  >"Natural orientation" is the base concept Which help us to understand what is the Display's rotation  
  >and what is the Camera's orientation.  
  >
  >>###1.We use "Display" class to get rotation.  
  >>`Display display = getWindowManager().getDefaultDisplay();`  
  >>`DisplayMetrics dm = new DisplayMetrics();`  
  >>// This is important, It will take one of those values:  
  >>// Surface.ROTATION_0 (no rotation), Surface.ROTATION_90, Surface.ROTATION_180, Surface.ROTATION_270  
  >>`int rotation = display.getRotation();`  

  >>More information about "Display" can be found as follows:  
  >>Important:("getRotation function in Display")  
  [reference url](http://developer.android.com/intl/zh-cn/reference/android/view/Display.html)
  >
  >
  >>###2.We use "Configuration" class to get orientation of screen.  
  >>`Configuration config = getResources().getConfiguration();`  
  >>config.orientation will take one of those values:ORIENTATION_LANDSCAPE,ORIENTATION_PORTRAIT  
  >>
  >>More information about "Configuration" can be found as follows:
  >>[reference url](http://developer.android.com/intl/zh-cn/reference/android/content/res/Configuration.html)
  >
  >
  >>###3.We Use the results of 1 and 2 above to get the "natural orientation" of the device.  
  >>reference AndroidUtil

* ##Step Three:
  You should use above results to adjust the camera image displayed on the device.  
  Infomation about this as follows:[reference url](http://developer.android.com/intl/zh-cn/reference/android/hardware/Camera.CameraInfo.html)  




