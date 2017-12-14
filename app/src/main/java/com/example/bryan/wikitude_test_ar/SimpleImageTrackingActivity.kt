package com.example.bryan.wikitude_test_ar

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.example.bryan.wikitude_test_ar.utils.CustomSurfaceView
import com.example.bryan.wikitude_test_ar.utils.Driver
import com.example.bryan.wikitude_test_ar.utils.GLRenderer
import com.example.bryan.wikitude_test_ar.utils.WikitudeSDKConstants
import com.wikitude.NativeStartupConfiguration
import com.wikitude.WikitudeSDK
import com.wikitude.common.camera.CameraSettings
import com.wikitude.common.rendering.RenderExtension
import com.wikitude.rendering.ExternalRendering
import com.wikitude.tracker.*

/**
 * Created by bryan on 12/14/2017.
 */
class SimpleImageTrackingActivity(private var mTargetCollectionResource: TargetCollectionResource, private var mWikitudeSDK: WikitudeSDK,
                                  private var mView: CustomSurfaceView, private var mDriver: Driver, private var mGLRenderer: GLRenderer) : Activity(), ImageTrackerListener, ExternalRendering {
    private val TAG = "SimpleClientTracking"

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        mWikitudeSDK = WikitudeSDK(this)
        var nativeStartupConfiguration = NativeStartupConfiguration()
        nativeStartupConfiguration.licenseKey = WikitudeSDKConstants.WIKITUDE_SDK_KEY
        nativeStartupConfiguration.cameraPosition = CameraSettings.CameraPosition.BACK
        nativeStartupConfiguration.cameraResolution = CameraSettings.CameraResolution.AUTO

        mWikitudeSDK.onCreate(applicationContext, this, nativeStartupConfiguration)

        mTargetCollectionResource = mWikitudeSDK.trackerManager.createTargetCollectionResource("file://android_asset/BJE5Zrlzz.wtc",
                object: TargetCollectionResourceLoadingCallback{
                    override fun onError(errorCode: Int, errorMessage: String?) {
                        Log.v(TAG, "Failed to load target collection resource. Reason: " + errorMessage)
                    }

                    override fun onFinish() {
                        mWikitudeSDK.trackerManager.createImageTracker(mTargetCollectionResource, this@SimpleImageTrackingActivity, null)
                    }
                })
    }



    override fun onRenderExtensionCreated(renderExtension: RenderExtension?) {
        mGLRenderer = GLRenderer(renderExtension)
        mView = CustomSurfaceView(applicationContext, mGLRenderer)
        mDriver = Driver(mView, 30)
        setContentView(mView)

    }

    override fun onImageLost(tracker: ImageTracker?, target: ImageTarget?) {
        Log.v(TAG, "Lost target: " + target)
    }

    override fun onErrorLoadingTargets(tracker: ImageTracker?, errorCode: Int, errorMessage: String?) {
        Log.v(TAG, "Unable to load image tracker: " + errorMessage)
    }

    override fun onTargetsLoaded(tracker: ImageTracker?) {
        Log.v(TAG, "Image tracker loaded")
    }

    override fun onImageRecognized(tracker: ImageTracker?, target: ImageTarget?) {
        Log.v(TAG, "Target recognized " + target?.name)
    }

    override fun onExtendedTrackingQualityChanged(p0: ImageTracker?, p1: ImageTarget?, p2: Int, p3: Int) {
    }

    override fun onImageTracked(p0: ImageTracker?, target: ImageTarget?) {
        Log.v(TAG, "Image tracked " + target?.name)
    }




}