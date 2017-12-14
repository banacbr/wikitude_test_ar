package com.example.bryan.wikitude_test_ar.utils

import android.app.Activity
import android.content.Context

import android.graphics.PixelFormat
import android.opengl.GLSurfaceView
import android.os.Build
import android.util.AttributeSet
import android.util.Log

import java.util.Arrays

import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.egl.EGLContext
import javax.microedition.khronos.egl.EGLDisplay

import android.opengl.GLSurfaceView.RENDERMODE_WHEN_DIRTY

/**
 * Created by bryan on 12/14/2017.
 */

class CustomSurfaceView(context: Context, private val mRenderer: GLRenderer, attrs: AttributeSet?, vararg targetRenderingAPIs: TargetRenderingAPI) : GLSurfaceView(context, attrs) {

    enum class TargetRenderingAPI {
        OPENGL_ES_2, OPENGL_ES_3
    }

    constructor(context: Context, renderer: GLRenderer, vararg targetRenderingAPIs: TargetRenderingAPI) : this(context, renderer, null, *targetRenderingAPIs) {}

    init {

//        if (this@CustomSurfaceView.context == null || this@CustomSurfaceView.context is Activity && (this@CustomSurfaceView.context as Activity).isFinishing) {
//            return
//        }

        val targetRenderingAPIList = Arrays.asList(*targetRenderingAPIs)

        val tryOpenGlEs3_0 = targetRenderingAPIList.contains(TargetRenderingAPI.OPENGL_ES_3)
        val tryOpenGlEs2_0 = targetRenderingAPIList.contains(TargetRenderingAPI.OPENGL_ES_2)

        setEGLContextFactory(object : GLSurfaceView.EGLContextFactory {
            private val EGL_CONTEXT_CLIENT_VERSION = 0x3098
            private val GL_VERSION_3_0 = 3.0f
            private val GL_VERSION_2_0 = 2.0f

            override fun createContext(egl: EGL10, display: EGLDisplay, eglConfig: EGLConfig): EGLContext {
                /** OpenGLES 3.0 was introduced with android 4.3(18).  */
                if (tryOpenGlEs3_0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    val gles3Context = createOpenGL3Context(egl, display, eglConfig)

                    if (gles3Context == EGL10.EGL_NO_CONTEXT && tryOpenGlEs2_0) {
                        return createOpenGL2Context(egl, display, eglConfig)
                    } else {
                        if (gles3Context == EGL10.EGL_NO_CONTEXT) {
                            Log.e(TAG, "createContext: OpenGL ES 3 context could not be created.")
                        }
                        return gles3Context
                    }
                } else {
                    return createOpenGL2Context(egl, display, eglConfig)
                }
            }

            private fun createOpenGL3Context(egl: EGL10, display: EGLDisplay, eglConfig: EGLConfig): EGLContext {
                Log.i(TAG, "creating OpenGL ES $GL_VERSION_3_0 context")
                val attrib_list = intArrayOf(EGL_CONTEXT_CLIENT_VERSION, GL_VERSION_3_0.toInt(), EGL10.EGL_NONE)
                return egl.eglCreateContext(display, eglConfig, EGL10.EGL_NO_CONTEXT, attrib_list)
            }

            private fun createOpenGL2Context(egl: EGL10, display: EGLDisplay, eglConfig: EGLConfig): EGLContext {
                Log.i(TAG, "creating OpenGL ES $GL_VERSION_2_0 context")
                val attrib_list_2_0 = intArrayOf(EGL_CONTEXT_CLIENT_VERSION, GL_VERSION_2_0.toInt(), EGL10.EGL_NONE)
                return egl.eglCreateContext(display, eglConfig, EGL10.EGL_NO_CONTEXT, attrib_list_2_0)
            }

            override fun destroyContext(egl: EGL10, display: EGLDisplay, context: EGLContext) {
                egl.eglDestroyContext(display, context)
            }
        })
        setEGLConfigChooser(8, 8, 8, 8, 16, 0)

        setRenderer(mRenderer)
        renderMode = RENDERMODE_WHEN_DIRTY

        holder.setFormat(PixelFormat.TRANSLUCENT)

    }

    override fun onPause() {
        super.onPause()
        mRenderer.onPause()
    }

    override fun onResume() {
        super.onResume()
        mRenderer.onResume()
    }

    companion object {

        val TAG = "WTGLSurfaceView"
    }
}
