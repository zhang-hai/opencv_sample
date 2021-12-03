package com.example.opencvexample.ui.chapter02

import android.Manifest
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.example.opencvexample.R
import com.example.opencvexample.base.BaseActivity
import com.example.opencvexample.utils.PermissionUtils
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc

/**
 * 图像入门 - 图片读取和写入
 */
class ExampleActivity01:BaseActivity() {

    override fun onApplyPermissionSuccess() {
        readAndShowImage()
    }


    //ImageView组件
    val imageView: ImageView by lazy{
        findViewById(R.id.iv_lake)
    }

    //按钮
    val button : Button by lazy {
        findViewById(R.id.btn_img_read)
    }

    val btnGray: Button by lazy {
        findViewById(R.id.btn_img_gray)
    }

    val btnBinary: Button by lazy {
        findViewById(R.id.btn_img_binary)
    }
    val btnAutoBinary: Button by lazy {
        findViewById(R.id.btn_img_auto_binary)
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
        //图片灰度
        btnGray.setOnClickListener {
            showImage {
                val destMat = Mat()
                //图像灰度处理
                Imgproc.cvtColor(it,destMat,Imgproc.COLOR_BGR2GRAY)
                destMat
            }
        }

        //手动设置二值图
        btnBinary.setOnClickListener {
            //注意：设置二值图时，必须要先对图像进行灰度处理
            showImage{
                //1.先进行灰度处理
                val grayMat = Mat()
                Imgproc.cvtColor(it,grayMat,Imgproc.COLOR_BGR2GRAY)
                //2.进行二值化处理
                val destMat = Mat()
                /**
                 * 参数说明
                 * src      源Mat对象
                 * dst      转换二值图后存储的Mat对象
                 * thresh  伐值，
                 * maxVal  高于设置的thresh伐值时，根据设置的type区分是将大于伐值时设置为maxVal还是小于伐值时设置为maxVal
                 * type    伐值类型，分别为：THRESH_BINARY = 0,THRESH_BINARY_INV = 1,THRESH_TRUNC = 2,THRESH_TOZERO = 3,THRESH_TOZERO_INV = 4,THRESH_MASK = 7,THRESH_OTSU = 8,THRESH_TRIANGLE = 16;
                 */
                Imgproc.threshold(grayMat,destMat,80.0,255.0,Imgproc.THRESH_OTSU)
                destMat
            }
        }

        //通过自动设置二值图
        btnAutoBinary.setOnClickListener {
            showImage {
                //1.先进行灰度处理
                val grayMat = Mat()
                Imgproc.cvtColor(it,grayMat,Imgproc.COLOR_BGR2GRAY)
                //2.进行二值化处理
                val destMat = Mat()
                /**
                 * 参数说明
                 * src              源Mat对象
                 * dst              转换二值图后存储的Mat对象
                 * maxVal           高于设置的thresh伐值时，根据设置的type区分是将大于伐值时设置为maxVal还是小于伐值时设置为maxVal
                 * adaptiveMethod   自定义使用的伐值算法，有：ADAPTIVE_THRESH_MEAN_C（平均），ADAPTIVE_THRESH_GAUSSIAN_C（高斯）
                 * thresholdType    伐值类型，分别为：THRESH_BINARY = 0,THRESH_BINARY_INV = 1,THRESH_TRUNC = 2,THRESH_TOZERO = 3,THRESH_TOZERO_INV = 4,THRESH_MASK = 7,THRESH_OTSU = 8,THRESH_TRIANGLE = 16;
                 * blockSize        用来计算阈值的邻域尺寸，3,5,7等,采用奇数
                 * C                减去平均值或加权平均值的常数，通常为正数，也可能为0或负数
                 */
                Imgproc.adaptiveThreshold(grayMat,destMat,255.0,Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_BINARY,13,5.0)
                destMat
            }
        }
    }

    /**
     * 你将学习以下功能：cv.imread()，cv.imwrite()
     */
    private fun readAndShowImage() {
//        val fileName = "${Environment.getExternalStorageDirectory().path}/71X58PICNjx_1024.jpg"
        //这里先通过该方法判断指定的图片能否被OpenCV解析，否则获取mat.width和mat.height的值会为0
//        if (Imgcodecs.haveImageReader(fileName)){
            //读取sdcard图片
//            val mat = Imgcodecs.imread(fileName, Imgcodecs.IMREAD_UNCHANGED)
            val mat = Utils.loadResource(this,R.mipmap.example_1,Imgcodecs.IMREAD_UNCHANGED)
            val bitmap = Bitmap.createBitmap(mat.width(),mat.height(), Bitmap.Config.RGB_565)
            Utils.matToBitmap(mat,bitmap)
            imageView.setImageBitmap(bitmap)
//        }else{
//            showToast("OpenCV不支持加载该图片")
//        }
    }

    /**
     * 显示绘制的图形
     */
    private fun showImage(callback:(it:Mat)->Mat){
        //1.加载res中资源转为 mat对象
        val mat = Utils.loadResource(this,R.mipmap.sktech_1,Imgcodecs.IMREAD_UNCHANGED)

        //2.执行回调函数，获取处理后的目标destMat
        val destMat = callback(mat)

        //3.生成响应宽高的bitmap
        val bitmap = Bitmap.createBitmap(mat.width(),mat.height(), Bitmap.Config.RGB_565)
        //4.重新将mat对象转为bitmap对象
        Utils.matToBitmap(destMat,bitmap)
        //5.将bitmap显示到imageView中
        imageView.setImageBitmap(bitmap)
    }
}