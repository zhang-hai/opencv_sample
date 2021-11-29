package com.example.opencvexample.ui

import android.Manifest
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.example.opencvexample.R
import com.example.opencvexample.base.BaseActivity
import com.example.opencvexample.utils.PermissionUtils
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.imgcodecs.Imgcodecs

class MainActivity : BaseActivity() {

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
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_example_01).setOnClickListener {
            startActivity(ExampleActivity01::class.java)
        }
        findViewById<Button>(R.id.btn_example_02).setOnClickListener {
            startActivity(ExampleActivity02::class.java)
        }
        findViewById<Button>(R.id.btn_example_03).setOnClickListener {
            startActivity(ExampleActivity03::class.java)
        }
        findViewById<Button>(R.id.btn_example_04).setOnClickListener {
            startActivity(ExampleActivity04::class.java)
        }
        findViewById<Button>(R.id.btn_example_05).setOnClickListener {
            startActivity(ExampleActivity05::class.java)
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