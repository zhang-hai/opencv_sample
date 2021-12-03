package com.github.harry.sketch.utils

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.MatOfInt
import org.opencv.imgcodecs.Imgcodecs
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object BitmapUtils {

    /**
     * 保存Bitmap到SDCard
     *
     * @param bitmap
     * @param filePath 图片保存路径
     */
    fun saveBitmapPng(bitmap: Bitmap, filePath: String): Boolean {

        var success: Boolean = false
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
        }
        val dir = file.parentFile
        dir?.apply {
            if (!dir.exists()) {
                dir.mkdirs()
            }
        }
        var start = Core.getTickCount()
        //优先采用opencv保存图片方式，性能比Android保存方式提高3倍
        val dst = Mat()
        Utils.bitmapToMat(bitmap,dst)
        success = Imgcodecs.imwrite(filePath,dst)
        //转码成功才打印耗时
        if (success){
            var cost = (Core.getTickCount() - start)/Core.getTickFrequency()
            println("OpenCV 保存图片耗时--->${cost}")
        }
        if (!success){
            success = try {
                start = Core.getTickCount()
                val out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.flush()
                out.close()
                val cost = (Core.getTickCount() - start)/Core.getTickFrequency()
                println("Android 保存图片耗时--->${cost}")
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }
        return success
    }
}