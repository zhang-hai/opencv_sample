package com.example.opencvexample.ui

import android.Manifest
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.example.opencvexample.R
import com.example.opencvexample.base.BaseActivity
import com.example.opencvexample.utils.PermissionUtils
import org.opencv.android.CameraBridgeViewBase
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import org.opencv.videoio.VideoCapture
import org.opencv.videoio.Videoio

/**
 * 图像入门 - 视频
 */
class ExampleActivity02:BaseActivity(), CameraBridgeViewBase.CvCameraViewListener2 {

    //ImageView组件
    val imageView: ImageView by lazy{
        findViewById<ImageView>(R.id.iv_lake)
    }

    //按钮
    val button : Button by lazy {
        findViewById(R.id.btn_img_read)
    }
    val cap = VideoCapture()

    val mOpenCvCameraView : CameraBridgeViewBase by lazy {
        findViewById(R.id.id_surface_view)
    }


    override fun onDestroy() {
        cap.release()
        mOpenCvCameraView?.disableView()
        super.onDestroy()
    }

    override fun onApplyPermissionSuccess() {
        initCameraView()
        mOpenCvCameraView.setCameraPermissionGranted()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example_02)

        if (PermissionUtils.checkPermission(this, mutableListOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA).toTypedArray(),
                PERMISSION_REQUEST_CODE)) {
//            onApplyPermissionSuccess()
        }

        button.setOnClickListener {
            readAndShowImage()
        }
    }

    private fun initCameraView(){
        mOpenCvCameraView.visibility = View.VISIBLE
        mOpenCvCameraView.setCvCameraViewListener(this)
        mOpenCvCameraView.enableView()
    }


    /**
     * 你将学习以下功能：cv.imread()，cv.imwrite()
     */
    private fun readAndShowImage() {
        val aviFilePath = "${Environment.getExternalStorageDirectory().path}/vid_20190719_151337_201971915216.avi"
        val ret = cap.open(aviFilePath)
        //判断是否打开成功
        if (!cap.isOpened){
            //若未打开，则先打开相机
            showToast("未打开Camera")
            return
        }
        val frame = Mat()
        cap.read(frame)
        val destMat = Mat()
        Imgproc.cvtColor(frame,destMat,Imgproc.COLOR_BGR2GRAY)

        val bitmap = Bitmap.createBitmap(destMat.width(),destMat.height(),Bitmap.Config.ARGB_8888)
        imageView.setImageBitmap(bitmap)
    }

    override fun onCameraViewStarted(width: Int, height: Int) {

    }

    override fun onCameraViewStopped() {

    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat? {
        return inputFrame?.rgba()
    }

}