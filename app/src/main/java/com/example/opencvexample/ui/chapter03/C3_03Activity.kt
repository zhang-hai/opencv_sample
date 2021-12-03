package com.example.opencvexample.ui.chapter03

import android.widget.ImageView
import com.example.opencvexample.R
import com.example.opencvexample.base.BaseBindingActivity
import com.example.opencvexample.databinding.ActivityExample303Binding
import org.opencv.core.Core
import org.opencv.core.Mat
import kotlin.math.cos

/**
 * 3.2 图像上的算术运算
 */
class C3_03Activity: BaseBindingActivity<ActivityExample303Binding>() {

    private var originMat = Mat()
    private var originMat2 = Mat()

    //设置布局文件
    override fun getLayoutId() = R.layout.activity_example_303

    override fun init() {
        originMat = loadImage(viewBinding.ivShow,R.mipmap.ic_dft_avatar1)
        originMat2 = loadImage(viewBinding.ivShow2,R.mipmap.ic_dft_avatar2)

        viewBinding.btnMix.setOnClickListener {
            val start = Core.getTickCount()
            operation(viewBinding.ivResult){
                Core.addWeighted(originMat,0.3,originMat2,0.7,1.0,it)
            }
            val end = Core.getTickCount()
            val cost = (end - start)/Core.getTickFrequency()
            viewBinding.tvContent.text = "当前时钟周期：${Core.getTickFrequency()}\n\n图像混合耗时：(${start} - ${end})/${Core.getTickFrequency()} = ${cost}秒"
        }
    }

    private fun operation(img:ImageView,op:(dst:Mat)->Unit){
        /**
         * 仅大小和通道数相同才能进行相加
         */
        if (originMat.channels() == originMat2.channels() && originMat.size() == originMat2.size()){
            val dst = Mat()
            //操作
            op(dst)
            //显示
            showImage(img,dst)
        }else{
            showToast("图像大小或通道数不一致，无法进行图片运算")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        originMat?.release()
        originMat2.release()
    }


}

