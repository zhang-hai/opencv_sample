package com.example.opencvexample.ui.chapter03

import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.example.opencvexample.R
import com.example.opencvexample.base.BaseActivity
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Scalar
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import kotlin.experimental.and

/**
 * 3.1 图像的基本操作
 */
class C3_01Activity: BaseActivity() {

    override fun onApplyPermissionSuccess() {

    }

    private val imageView: ImageView by lazy {
        findViewById(R.id.iv_show)
    }
    private val tvContent:TextView by lazy {
        findViewById(R.id.tv_content)
    }
    private var originMat = Mat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example_301)


        loadImage()

    }

    private fun loadImage() {
        val mat = Utils.loadResource(this, R.mipmap.avater_1)
        Imgproc.cvtColor(mat, originMat, Imgproc.COLOR_BGR2RGB)
        showImage(originMat)
    }

    private fun showImage(dst:Mat){
        val bitmap = Bitmap.createBitmap(dst.width(), dst.height(), Bitmap.Config.RGB_565)
        Utils.matToBitmap(dst, bitmap)
        imageView.setImageBitmap(bitmap)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_mat_operation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_visit_px -> {//访问像素点
                visitImgPx()
            }
            R.id.item_modify_px -> {//修改像素点
                modifyImgPx(10,300,10,150)
            }
            R.id.item_visit_property -> {//访问图像属性
                visitImgProperty()
            }
            R.id.item_set_roi -> {//设置感兴趣区域
                setImgRoi()
            }
            R.id.item_split -> {//图像分割
                val mats = mutableListOf<Mat>()
                Core.split(originMat,mats)
                showImage(mats.last())
            }
            R.id.item_contact -> {//图像合并
                val mats = mutableListOf<Mat>()
                Core.split(originMat,mats)
                val dst = Mat()
                Core.merge(mats,dst)
                showImage(dst)
            }
            R.id.item_img_border->{
                val dst = Mat()
                /**
                 * src 要修改的源Mat对象
                 * dst 修改后的Mat对象
                 * top 顶部边框粗细
                 * bottom 底部边框粗细
                 * left 左侧边框粗细
                 * right 右侧边框粗细
                 * borderType 边框类型
                 * value Scalar类型的对象，控制边框颜色值，其参数分别对应R、G、B
                 */
                Core.copyMakeBorder(originMat,dst,50,50,30,30,Core.BORDER_CONSTANT,Scalar(255.0,0.0,0.0))
                showImage(dst)
            }
        }
        return true
    }

    private fun visitImgPx() {
        var text = "${tvContent.text}访问像素点\n"
        //方式一、直接访问比较慢
        val px = originMat[100, 100]
        text+="方式一:获取[100,100]的像素值->${px.asList()}\n"

        //方式二、
        val px2 = originMat.get(100, 100)
        text+="方式二：获取get(100,100)的像素值->${px2.asList()}\n\n"
        tvContent.text = text
    }

    /**
     * 修改某一块区域颜色
     * row 代表高度方向
     * col 代表宽度方向
     */
    private fun modifyImgPx(startRow:Int,endRow:Int,startCol:Int,endCol:Int) {
        if (startRow > endRow || startCol > endCol){
            showToast("参数错误")
            return
        }
        val rows = originMat.rows()
        val cols = originMat.cols()

        try {
            /**
             * row 代表高度方向
             * col 代表宽度方向
             */
            val bytes = ByteArray(originMat.channels())
            //先检测边界
            val srow = if (startRow < 0) 0 else startRow
            val erow = if (endRow > rows) rows else endRow
            val scol = if (startCol < 0) 0 else startCol
            val ecol = if (endCol > cols) cols else endCol
            for (row in (srow until erow)){
                for (col in (scol until ecol)){
                    //读取
                    originMat.get(row,col,bytes)
                    //修改
                    bytes[0] = (255 - (bytes[0].toShort() and 0xff)).toByte()
                    bytes[1] = (255 - (bytes[1].toShort() and 0xff)).toByte()
                    bytes[2] = (255 - (bytes[2].toShort() and 0xff)).toByte()
                    //保存
                    // todo 注意：这里保存需要是byte数组，试过int数组或short数组会抛异常
                    originMat.put(row, col, bytes)
                }
            }
            //转成bitmap并显示出来
            showImage(originMat)
        } catch (ex: Exception) {
            println(ex.printStackTrace())
        }
    }

    /**
     * 访问图像的相关属性
     */
    private fun visitImgProperty(){
        var text = "${tvContent.text}访问图像的相关属性\n"
        text+="图像宽度：${originMat.width()}，高度:${originMat.height()}\n"
        text+="行数：${originMat.rows()},列数：${originMat.cols()},通道数：${originMat.channels()}\n"
        text+="图像数据类型：${originMat.type()}，像素总数：${originMat.size()}\n"
        tvContent.text = text
    }

    private fun setImgRoi(){
        val rows = originMat.rows()
        val cols = originMat.cols()

        try {
            /**
             * row 代表高度方向
             * col 代表宽度方向
             */
            //先检测边界
            val srow = rows/2 - 50
            val erow = rows/2 + 90
            val scol = cols/2 - 150
            val ecol = cols/2 + 50

            //将感兴趣区域取出
            val roiMat = originMat.submat(srow,erow,scol,ecol)
            //转成bitmap并显示出来
            showImage(roiMat)
        } catch (ex: Exception) {
            println(ex.printStackTrace())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        originMat.release()
    }
}

