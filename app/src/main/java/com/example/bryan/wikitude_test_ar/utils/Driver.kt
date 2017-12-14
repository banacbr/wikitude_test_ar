package com.example.bryan.wikitude_test_ar.utils

import java.util.*

/**
 * Created by bryan on 12/14/2017.
 */
class Driver(private val customSurfaceView: CustomSurfaceView, private val mFps: Int, private var mRenderTimer: Timer? = null ) {

    fun start(){
        mRenderTimer?.cancel()

        //mRenderTimer = Timer()
        mRenderTimer?.scheduleAtFixedRate(object : TimerTask(){
            override fun run() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }, 0, (1000/mFps).toLong())
    }

    fun stop(){
        mRenderTimer?.cancel()
        mRenderTimer = null
    }

}