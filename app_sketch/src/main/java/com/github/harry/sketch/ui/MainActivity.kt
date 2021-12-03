package com.github.harry.sketch.ui

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.util.Log
import android.view.View
import com.github.harry.sketch.R
import com.github.harry.sketch.databinding.ActivityMainBinding
import com.github.harry.sketch.engine.GlideEngine
import com.github.harry.sketch.utils.BitmapUtils
import com.github.harry.sketch.utils.FileCommonUtil
import com.github.harry.sketch.utils.PermissionUtils
import com.github.harry.sketch.viewmodle.MainViewModel
import com.harry2815.mvvm.arch.ui.BaseWithViewBindingActivity
import com.huantansheng.easyphotos.EasyPhotos
import com.huantansheng.easyphotos.models.album.entity.Photo
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc

class MainActivity : BaseWithViewBindingActivity<MainViewModel, ActivityMainBinding>() {

    private val TAG = "MainActivity"

    companion object{
        const val PERMISSION_REQUEST_CODE = 0x1000
        const val REQUEST_CODE = 0x1001
    }

    override fun getLayoutId() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding.btnFloatAdd.setOnClickListener {
            if (PermissionUtils.checkPermission(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA),
                    PERMISSION_REQUEST_CODE)){
                onApplyPermissionSuccess(PERMISSION_REQUEST_CODE)
            }
        }
        mViewBinding.btnSave.setOnClickListener {
            save()
        }
    }

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

    override fun onResume() {
        super.onResume()
        if (OpenCVLoader.initDebug()) {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }else{
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization")
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE){
            if (PermissionUtils.checkGrantIsSuccess(grantResults.toList())){
                onApplyPermissionSuccess(requestCode)
            }else{
                showToast("权限申请失败")
            }
        }
    }

    private fun onApplyPermissionSuccess(requestCode: Int){
        if (requestCode == PERMISSION_REQUEST_CODE){
            EasyPhotos.createAlbum(this, true,false, GlideEngine.getInstance())//参数说明：上下文，是否显示相机按钮，是否使用宽高数据（false时宽高数据为0，扫描速度更快），[配置Glide为图片加载引擎](https://github.com/HuanTanSheng/EasyPhotos/wiki/12-%E9%85%8D%E7%BD%AEImageEngine%EF%BC%8C%E6%94%AF%E6%8C%81%E6%89%80%E6%9C%89%E5%9B%BE%E7%89%87%E5%8A%A0%E8%BD%BD%E5%BA%93)
                .setPuzzleMenu(false)   //不显示拼图按钮
                .setCleanMenu(false)    //不显示清除按钮
                .setFileProviderAuthority("com.github.harry.sketch.fileprovider")//参数说明：见下方`FileProvider的配置`
                .start(REQUEST_CODE);
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            val list:List<Photo> = data!!.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS)!!
            convertImg2Binary(list[0].path)
        }
    }


    private fun convertImg2Binary(filePath:String){
        //这里先通过该方法判断指定的图片能否被OpenCV解析，否则获取mat.width和mat.height的值会为0
        if (Imgcodecs.haveImageReader(filePath)){
            //读取sdcard图片
            val mat = Imgcodecs.imread(filePath, Imgcodecs.IMREAD_UNCHANGED)

            //1.将图进行灰度处理
            val grayMat = Mat()
            Imgproc.cvtColor(mat,grayMat,Imgproc.COLOR_BGR2GRAY)
            //2.利用自动阈值，计算灰度图的二值图
            val dstMat = Mat()
            Imgproc.adaptiveThreshold(grayMat,dstMat,255.0,Imgproc.ADAPTIVE_THRESH_MEAN_C,Imgproc.THRESH_BINARY,13,5.0)
            //3.将二值图显示出来
            val bitmap = Bitmap.createBitmap(dstMat.width(),dstMat.height(), Bitmap.Config.RGB_565)
            Utils.matToBitmap(dstMat,bitmap)
            mViewBinding.ivSketchShow.setImageBitmap(bitmap)
            mViewBinding.btnSave.visibility = View.VISIBLE
        }else{
            showToast("OpenCV不支持加载该图片")
        }
    }

    private fun save(){
        val filePath = Environment.getExternalStorageDirectory().absolutePath + "/mysketch/"+System.currentTimeMillis()+".png"
        val ret = BitmapUtils.saveBitmapPng((mViewBinding.ivSketchShow.drawable as BitmapDrawable).bitmap,filePath)
        if (ret){
            FileCommonUtil.sendInsertFileBroadcast(this,filePath)
        }
        showToast(if (ret) "保存成功" else "保存失败")
    }
}