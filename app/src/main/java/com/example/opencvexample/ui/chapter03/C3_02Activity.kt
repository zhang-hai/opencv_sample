package com.example.opencvexample.ui.chapter03

import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.opencvexample.R
import com.example.opencvexample.base.BaseActivity
import com.example.opencvexample.base.BaseBindingActivity
import com.example.opencvexample.databinding.ActivityExample302Binding
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Scalar
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import kotlin.experimental.and

/**
 * 3.2 图像上的算术运算
 */
class C3_02Activity: BaseBindingActivity<ActivityExample302Binding>() {

    private var originMat = Mat()
    private var originMat2 = Mat()

    //设置布局文件
    override fun getLayoutId() = R.layout.activity_example_302

    override fun init() {
        originMat = loadImage(viewBinding.ivShow,R.mipmap.ic_dft_avatar1)
        originMat2 = loadImage(viewBinding.ivShow2,R.mipmap.ic_dft_avatar2)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_mat_operation_302, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_img_add -> {//图像相加
                operation(viewBinding.ivResult){
                    Core.add(originMat,originMat2,it)
                }
                operation(viewBinding.ivResult2){
                    //图像混合，按权重
                    Core.addWeighted(originMat,0.3,originMat2,0.7,1.0,it)
                }
            }
            R.id.item_img_sub -> {//图像相减
                operation(viewBinding.ivResult){
                    Core.subtract(originMat,originMat2,it)
                }
            }
            R.id.item_img_multi -> {//图像相乘
                operation(viewBinding.ivResult){
                    Core.multiply(originMat,originMat2,it)
                }
            }
            R.id.item_img_diff -> {//图像相除
                operation(viewBinding.ivResult){
                    Core.divide(originMat,originMat2,it)
                }
            }
            R.id.item_img_and -> {//图像按位与
                operation(viewBinding.ivResult){
                    Core.bitwise_and(originMat,originMat2,it)
                }
            }
            R.id.item_img_or -> {//图像按位或
                operation(viewBinding.ivResult){
                    Core.bitwise_or(originMat,originMat2,it)
                }
            }
            R.id.item_img_not->{   //图像按位非
                operation(viewBinding.ivResult){
                    Core.bitwise_not(originMat,it)
                }
            }
            R.id.item_img_xor->{    //图像按位异或
                operation(viewBinding.ivResult){
                    Core.bitwise_xor(originMat,originMat2,it)
                }
            }
        }
        return true
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

