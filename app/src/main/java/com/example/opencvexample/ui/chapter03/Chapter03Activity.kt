package com.example.opencvexample.ui.chapter03

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.opencvexample.R
import com.example.opencvexample.base.BaseActivity
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader

class Chapter03Activity : BaseActivity() {

    private val TAG = "MainActivity"

    private val mLoaderCallback : BaseLoaderCallback = object :BaseLoaderCallback(this){
        override fun onManagerConnected(status: Int) {
            super.onManagerConnected(status)
            when(status){
                LoaderCallbackInterface.SUCCESS ->{
                    Log.i(TAG,"OpenCV loaded successfully")
//                    showToast("OpenCV加载成功")
                }
                else->{
                    super.onManagerConnected(status)
                    showToast("OpenCV加载失败")
                }
            }
        }
    }

    override fun onApplyPermissionSuccess() {
        TODO("Not yet implemented")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter_03)

        findViewById<TextView>(R.id.btn_example_01).setOnClickListener {
            startActivity(C3_01Activity::class.java)
        }
        findViewById<TextView>(R.id.btn_example_02).setOnClickListener {
            startActivity(C3_02Activity::class.java)
        }
        findViewById<TextView>(R.id.btn_example_03).setOnClickListener {
            startActivity(C3_03Activity::class.java)
        }
    }


    override fun onResume() {
        super.onResume()
        if (OpenCVLoader.initDebug()) {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }else{
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization")
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback)
        }
    }


}