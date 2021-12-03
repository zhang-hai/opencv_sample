package com.example.opencvexample.base

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

abstract class BaseBindingActivity<VB:ViewDataBinding>:BaseActivity() {

    lateinit var viewBinding : VB

    /**
     * 布局文件id
     */
    abstract fun getLayoutId():Int

    abstract fun init():Unit

    override fun onApplyPermissionSuccess() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this,getLayoutId())

        init()
    }

    /**
     * 加载图像
     */
    fun loadImage(img: ImageView, resId:Int) : Mat {
        val mat = Utils.loadResource(this, resId)
        val dst = Mat()
        Imgproc.cvtColor(mat, dst, Imgproc.COLOR_BGR2RGB)
        showImage(img,dst)
        return dst
    }

    fun showImage(img: ImageView, dst: Mat){
        val bitmap = Bitmap.createBitmap(dst.width(), dst.height(), Bitmap.Config.RGB_565)
        Utils.matToBitmap(dst, bitmap)
        img.setImageBitmap(bitmap)
    }
}