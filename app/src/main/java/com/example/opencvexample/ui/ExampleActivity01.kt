package com.example.opencvexample.ui

import android.Manifest
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.ImageView
import com.example.opencvexample.R
import com.example.opencvexample.base.BaseActivity
import com.example.opencvexample.utils.PermissionUtils
import org.opencv.android.Utils
import org.opencv.imgcodecs.Imgcodecs

/**
 * 图像入门 - 图片读取和写入
 */
class ExampleActivity01:BaseActivity() {

    override fun onApplyPermissionSuccess() {
        readAndShowImage()
    }


    //ImageView组件
    val imageView: ImageView by lazy{
        findViewById<ImageView>(R.id.iv_lake)
    }

    //按钮
    val button : Button by lazy {
        findViewById(R.id.btn_img_read)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example_01)

        button.setOnClickListener {
            if (PermissionUtils.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE,
                    PERMISSION_REQUEST_CODE)){
                onApplyPermissionSuccess()
            }
        }
    }

    /**
     * 你将学习以下功能：cv.imread()，cv.imwrite()
     */
    private fun readAndShowImage() {
        val fileName = "${Environment.getExternalStorageDirectory().path}/71X58PICNjx_1024.jpg"
        //这里先通过该方法判断指定的图片能否被OpenCV解析，否则获取mat.width和mat.height的值会为0
        if (Imgcodecs.haveImageReader(fileName)){
            val mat = Imgcodecs.imread(fileName, Imgcodecs.IMREAD_UNCHANGED)
            val bitmap = Bitmap.createBitmap(mat.width(),mat.height(), Bitmap.Config.RGB_565)
            Utils.matToBitmap(mat,bitmap)
            imageView.setImageBitmap(bitmap)
        }else{
            showToast("OpenCV不支持加载该图片")
        }
    }

}