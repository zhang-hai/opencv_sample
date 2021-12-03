package com.example.opencvexample.ui.chapter02

import android.Manifest
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.example.opencvexample.R
import com.example.opencvexample.base.BaseActivity
import com.example.opencvexample.utils.PermissionUtils
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc

/**
 * 图像入门 - 图片读取和写入
 */
class ExampleActivity03:BaseActivity() {

    override fun onApplyPermissionSuccess() {

    }


    //ImageView组件
    val imageView: ImageView by lazy{
        findViewById<ImageView>(R.id.iv_lake)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example_03)

        buttonEvent()
    }

    private fun buttonEvent(){
        //画直线
        findViewById<Button>(R.id.btn_draw_line).setOnClickListener {
            drawLine()
        }
        //绘制箭头
        findViewById<Button>(R.id.btn_draw_arrowline).setOnClickListener {
            drawArrowLine()
        }

        //画矩形
        findViewById<Button>(R.id.btn_draw_rectangle).setOnClickListener {
            drawRectangle()
        }
        //画圆形
        findViewById<Button>(R.id.btn_draw_circle).setOnClickListener {
            drawCircle()
        }
        //画椭圆
        findViewById<Button>(R.id.btn_draw_oval).setOnClickListener {
            drawEllipse()
        }
        //画多边形
        findViewById<Button>(R.id.btn_draw_polygon).setOnClickListener {
            drawPolygon()
        }
        //画文字
        findViewById<Button>(R.id.btn_draw_text).setOnClickListener {
            showDrawShapes {
                Imgproc.putText(it,"FONT_HERSHEY_SIMPLEX", Point(620.0,350.0),Imgproc.FONT_HERSHEY_SIMPLEX,1.0, Scalar(0.0,0.0,255.0))
                Imgproc.putText(it,"FONT_HERSHEY_PLAIN", Point(620.0,400.0),Imgproc.FONT_HERSHEY_PLAIN,1.0, Scalar(0.0,0.0,255.0))
                Imgproc.putText(it,"FONT_HERSHEY_DUPLEX", Point(620.0,450.0),Imgproc.FONT_HERSHEY_DUPLEX,1.0, Scalar(0.0,0.0,255.0))
                Imgproc.putText(it,"FONT_HERSHEY_COMPLEX", Point(620.0,500.0),Imgproc.FONT_HERSHEY_COMPLEX,1.0, Scalar(0.0,0.0,255.0))
                Imgproc.putText(it,"FONT_HERSHEY_TRIPLEX", Point(620.0,550.0),Imgproc.FONT_HERSHEY_TRIPLEX,1.0, Scalar(0.0,0.0,255.0))
                Imgproc.putText(it,"FONT_ITALIC", Point(620.0,600.0),Imgproc.FONT_ITALIC,1.0, Scalar(0.0,0.0,255.0))
            }
        }
        //绘制OpenCV Logo
        findViewById<Button>(R.id.btn_draw_logo).setOnClickListener {
            drawLogo()
        }

    }

    /**
     * 显示绘制的图形
     */
    private fun showDrawShapes(drawShape:(mat:Mat)->Unit){
        val mat = Mat()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        //将bitmap转为mat对象
        Utils.bitmapToMat(bitmap,mat)

        //绘制图形函数
        drawShape(mat)

        //重新将mat对象转为bitmap对象
        Utils.matToBitmap(mat,bitmap)
        //显示到imageView中
        imageView.setImageBitmap(bitmap)
    }

    /**
     * 你将学习以下功能：Imgproc.line()
     */
    private fun drawLine() {
        showDrawShapes {mat->
            val sx = 10.0
            val sy = 50.0
            val ex = 200.0
            val spaceY = 40
            /*
            绘制一条直线 从0,50到200,50
            mat:您要绘制形状的图像
            pt1:绘制直线起始点坐标
            pt2:绘制直线结束点坐标
            color:Scalar类型，形状的颜色。参数分别对应R、G、B
            thickness:线条粗细，如果对闭合图形（如圆）传递 -1 ，它将填充形状。默认厚度= 1
            lineType:：线的类型，是否为8连接线，抗锯齿线等。默认情况下，为8连接线。LINE_AA给出了抗锯齿的线条，看起来非常适合曲线。
            */
            Imgproc.line(mat, Point(sx,sy), Point(ex,sy), Scalar(0.0,0.0,255.0))
            //绘制一条直线 从0,50到200,50,设置了先的粗细
            Imgproc.line(mat, Point(sx,sy+spaceY*1), Point(ex,sy+spaceY*1),Scalar.all(100.0),5)
            //绘制一条直线 从0,50到200,50,设置了先的粗细,设置LineType
            Imgproc.line(mat, Point(sx,sy+spaceY*2), Point(ex,sy+spaceY*2),Scalar.all(150.0),5,Imgproc.FILLED)
            //绘制一条直线 从0,50到200,50,设置了先的粗细,设置LineType - LINE_4
            Imgproc.line(mat, Point(sx,sy+spaceY*3), Point(ex,sy+spaceY*3),Scalar.all(60.0),5,Imgproc.LINE_4)
            //绘制一条直线 从0,50到200,50,设置了先的粗细,设置LineType - LINE_8
            Imgproc.line(mat, Point(sx,sy+spaceY*4), Point(ex,sy+spaceY*4),Scalar.all(100.0),5,Imgproc.LINE_8)
            //绘制一条直线 从0,50到200,50,设置了先的粗细,设置LineType - LINE_AA
            Imgproc.line(mat, Point(sx,sy+spaceY*5), Point(ex,sy+spaceY*5),Scalar.all(100.0),5,Imgproc.LINE_AA)
        }
    }


    /**
     * 绘制带箭头的直线Imgproc.arrowedLine()
     */
    private fun drawArrowLine(){
        showDrawShapes {
            val sx = 220.0
            val sy = 50.0
            val ex = 430.0
            val spaceY = 40
            /*
            绘制一条带箭头直线 220,50到400,50
            mat:您要绘制形状的图像
            pt1:绘制直线起始点坐标
            pt2:绘制直线结束点坐标
            color:Scalar类型，形状的颜色。参数分别对应R、G、B
            thickness:线条粗细，如果对闭合图形（如圆）传递 -1 ，它将填充形状。默认厚度= 1
            lineType:：线的类型，是否为8连接线，抗锯齿线等。默认情况下，为8连接线。LINE_AA给出了抗锯齿的线条，看起来非常适合曲线。
            */
            Imgproc.arrowedLine(it,Point(sx,sy), Point(ex,sy), Scalar(0.0,0.0,255.0))
            //绘制一条带箭头直线 从0,50到200,50,设置了先的粗细
            Imgproc.arrowedLine(it, Point(sx,sy+spaceY*1), Point(ex,sy+spaceY*1),Scalar.all(100.0),5)
            //绘制一条带箭头直线 从0,50到200,50,设置了先的粗细,设置LineType
            Imgproc.arrowedLine(it, Point(sx,sy+spaceY*2), Point(ex,sy+spaceY*2),Scalar.all(150.0),5,Imgproc.FILLED)
            //绘制一条带箭头直线 从0,50到200,50,设置了先的粗细,设置LineType - LINE_4
            Imgproc.arrowedLine(it, Point(sx,sy+spaceY*3), Point(ex,sy+spaceY*3),Scalar.all(60.0),5,Imgproc.LINE_4)
            //绘制一条带箭头直线 从0,50到200,50,设置了先的粗细,设置LineType - LINE_8
            Imgproc.arrowedLine(it, Point(sx,sy+spaceY*4), Point(ex,sy+spaceY*4),Scalar.all(100.0),5,Imgproc.LINE_8)
            //绘制一条带箭头直线 从0,50到200,50,设置了先的粗细,设置LineType - LINE_AA
            Imgproc.arrowedLine(it, Point(sx,sy+spaceY*5), Point(ex,sy+spaceY*5),Scalar.all(100.0),5,Imgproc.LINE_AA)
        }
    }

    /**
     * 绘制矩形
     */
    private fun drawRectangle(){
        showDrawShapes {
            //参数含义与绘制直线类似
            Imgproc.rectangle(it, Rect(460,50,200,20), Scalar(255.0,0.0,0.0))
            Imgproc.rectangle(it, Rect(460,80,200,20), Scalar(255.0,0.0,0.0),3)
            Imgproc.rectangle(it, Rect(460,110,200,20), Scalar(255.0,0.0,0.0),3,Imgproc.FILLED)
            Imgproc.rectangle(it, Rect(460,140,200,20), Scalar(255.0,0.0,0.0),-1,Imgproc.LINE_4)
            Imgproc.rectangle(it, Rect(460,170,200,20), Scalar(255.0,0.0,0.0),3,Imgproc.LINE_8)
            //LINE_AA 抗锯齿效果最好
            Imgproc.rectangle(it, Rect(460,200,200,20), Scalar(255.0,0.0,0.0),3,Imgproc.LINE_AA)
        }
    }

    /**
     * 绘制圆形
     */
    private fun drawCircle(){
        showDrawShapes {
            Imgproc.circle(it, Point(770.0,60.0),50, Scalar(255.0,0.0,0.0))
            Imgproc.circle(it, Point(880.0,60.0),50, Scalar(255.0,0.0,0.0),3)
            Imgproc.circle(it, Point(990.0,60.0),50, Scalar(255.0,0.0,0.0),3,Imgproc.FILLED)
            Imgproc.circle(it, Point(770.0,180.0),50, Scalar(255.0,0.0,0.0),-1,Imgproc.LINE_4)
            Imgproc.circle(it, Point(880.0,180.0),50, Scalar(255.0,0.0,0.0),3,Imgproc.LINE_8)
            //LINE_AA 抗锯齿效果最好
            Imgproc.circle(it, Point(990.0,180.0),50, Scalar(255.0,0.0,0.0),3,Imgproc.LINE_AA)
        }
    }

    /**
     * 绘制椭圆形
     */
    private fun drawEllipse(){
        showDrawShapes {
            /*
            绘制一个椭圆
            img:您要绘制形状的图像
            center:椭圆的中心点坐标
            axes:椭圆主轴的大小，由宽和高组成，传入的Size中的宽高是主轴宽高的1/2
            angle:椭圆旋转角度
            startAngle:开始角度
            endAngle:绘制结束角度
            color:Scalar类型，形状的颜色。参数分别对应R、G、B
            thickness:线条粗细，如果对闭合图形（如圆）传递-1，它将填充形状。默认厚度=1
            lineType:：线的类型，是否为8连接线，抗锯齿线等。默认情况下，为8连接线。LINE_AA给出了抗锯齿的线条，看起来非常适合曲线。
             */
            Imgproc.ellipse(it, Point(100.0,400.0), Size(100.0,60.0),0.0,0.0,360.0,Scalar(255.0,0.0,0.0))
            Imgproc.ellipse(it, Point(310.0,400.0), Size(100.0,60.0),0.0,0.0,270.0,Scalar(255.0,0.0,0.0),-1)
            Imgproc.ellipse(it, Point(100.0,530.0), Size(100.0,60.0),30.0,0.0,360.0,Scalar(255.0,0.0,0.0),1,Imgproc.LINE_4)
            //LINE_AA 抗锯齿
            Imgproc.ellipse(it, Point(310.0,530.0), Size(100.0,60.0),0.0,0.0,360.0,Scalar(255.0,0.0,0.0),3,Imgproc.LINE_AA)
        }
    }

    /**
     * 绘制多边形
     */
    private fun drawPolygon(){
        showDrawShapes {
            Imgproc.polylines(it, mutableListOf(MatOfPoint(Point(460.0,400.0),Point(610.0,460.0),Point(530.0,500.0),Point(520.0,450.0))),true,Scalar(0.0,0.0,255.0),2)
        }
    }

    /**
     * 作业：绘制Logo
     */
    private fun drawLogo(){
        showDrawShapes {
            Imgproc.rectangle(it, Rect(10,600,260,260), Scalar(255.0,255.0,255.0),-1)
            //绘制三个原型
            //红环
            Imgproc.ellipse(it, Point(140.0,690.0), Size(50.0,50.0),120.0,0.0,300.0,Scalar(255.0,0.0,0.0),-1)
            Imgproc.circle(it,Point(140.0,690.0),25,Scalar(255.0,255.0,255.0),-1)
            //绿环
            Imgproc.ellipse(it, Point(80.0,800.0), Size(50.0,50.0),0.0,0.0,300.0,Scalar(0.0,255.0,0.0),-1)
            Imgproc.circle(it,Point(80.0,800.0),25,Scalar(255.0,255.0,255.0),-1)
            //蓝环
            Imgproc.ellipse(it, Point(200.0,800.0), Size(50.0,50.0),-60.0,0.0,300.0,Scalar(0.0,0.0,255.0),-1)
            Imgproc.circle(it,Point(200.0,800.0),25,Scalar(255.0,255.0,255.0),-1)
        }

    }
}