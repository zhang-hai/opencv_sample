package com.example.opencvexample.ui.chapter02

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ImageView
import com.example.opencvexample.R
import com.example.opencvexample.base.BaseActivity
import com.google.android.material.tabs.TabLayout
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * 图像入门 - 通过触摸绘制图形
 */
class ExampleActivity04:BaseActivity() {

    override fun onApplyPermissionSuccess() {

    }

    companion object{
        const val TYPE_LINE = 0     //直线
        const val TYPE_RECTANGLE = 1//矩形
        const val TYPE_CIRCLE = 2   //圆形
        const val TYPE_ELLIPSE = 3  //椭圆
    }

    private var mShapeType = 0

    //ImageView组件
    val imageView: ImageView by lazy{
        findViewById<ImageView>(R.id.iv_lake)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example_04)

        bitmap = (imageView.drawable as BitmapDrawable).bitmap

        buttonEvent()
    }



    private var mStartPoint = Point()
    private lateinit var bitmap : Bitmap

    @SuppressLint("ClickableViewAccessibility")
    private fun buttonEvent(){

        val tb_shape = findViewById<TabLayout>(R.id.tb_shape)
        val shapes = listOf("直线","矩形","圆形","椭圆")

        shapes.forEach {
            tb_shape.addTab(tb_shape.newTab().setText(it))
        }

        tb_shape.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                mShapeType = tab?.position?: TYPE_LINE
                println("选择了--->$mShapeType")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })


        imageView.setOnTouchListener { v, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN ->{
                    mStartPoint.apply {
                        x = event.x.toDouble()
                        y = event.y.toDouble()
                    }
                    bitmap = (imageView.drawable as BitmapDrawable).bitmap
                }
                MotionEvent.ACTION_MOVE ->{
                    when(mShapeType){
                        TYPE_LINE ->{
                            showDrawShapes(bitmap) {
                                Imgproc.line(it,mStartPoint, Point(event.x.toDouble(),event.y.toDouble()),Scalar(255.0,0.0,0.0),5,Imgproc.LINE_AA)
                            }
                        }
                        TYPE_RECTANGLE ->{
                            showDrawShapes(bitmap){
                                Imgproc.rectangle(it,mStartPoint, Point(event.x.toDouble(),event.y.toDouble()),Scalar(255.0,0.0,0.0),5,Imgproc.LINE_AA)
                            }
                        }
                        TYPE_CIRCLE -> {
                            showDrawShapes(bitmap){
                                val r = (event.x - mStartPoint.x)*(event.x - mStartPoint.x) + (event.y - mStartPoint.y)*(event.y - mStartPoint.y)
                                Imgproc.circle(it,mStartPoint, abs(sqrt(r)).toInt(),Scalar(255.0,0.0,0.0),5,Imgproc.LINE_AA)
                            }
                        }
                        TYPE_ELLIPSE ->{
                            showDrawShapes(bitmap){
                                val spaceX = event.x.toDouble() - mStartPoint.x
                                val spaceY = event.y.toDouble() - mStartPoint.y
                                val center = Point(mStartPoint.x+spaceX/2,mStartPoint.y+spaceY/2)
                                val size = Size(abs(spaceX)/2, abs(spaceY)/2)

                                Imgproc.ellipse(it, center, size,0.0,0.0,360.0,Scalar(255.0,0.0,0.0),5,Imgproc.LINE_AA)
                            }
                        }
                    }
                }
                MotionEvent.ACTION_UP ->{

                }
            }
            true
        }
    }

    /**
     * 显示绘制的图形
     */
    private fun showDrawShapes(bitmap: Bitmap,drawShape:(mat:Mat)->Unit){
        val mat = Mat()
        //将bitmap转为mat对象
        Utils.bitmapToMat(bitmap,mat)

        //绘制图形函数
        drawShape(mat)

        val bitmapNew = Bitmap.createBitmap(mat.width(),mat.height(),Bitmap.Config.RGB_565)
        //重新将mat对象转为bitmap对象
        Utils.matToBitmap(mat,bitmapNew)
        //显示到imageView中
        imageView.setImageBitmap(bitmapNew)
    }

}