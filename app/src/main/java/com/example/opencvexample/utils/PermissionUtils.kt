package com.example.opencvexample.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.lang.Exception

/**
 * 申请权限
 */
object PermissionUtils {

    /**
     * 申请权限，单个权限申请
     */
    fun checkPermission(activity: AppCompatActivity,permission:String,requestCode: Int):Boolean{
        return checkPermission(activity, listOf(permission).toTypedArray(), requestCode)
    }

    /**
     * 检测指定权限，批量申请，若无则申请
     */
    fun checkPermission(activity:AppCompatActivity,permissions:Array<String>,requestCode:Int):Boolean{

        if (permissions.isNullOrEmpty()){
            return true
        }
        val needPermissions = mutableListOf<String>()
        permissions.forEach {
            val code = ContextCompat.checkSelfPermission(activity,it)
            if (code != PackageManager.PERMISSION_GRANTED){
                needPermissions.add(it)
            }
        }

        //所有权限已经授权
        if (needPermissions.isEmpty()){
            return true
        }

        //有未授权的申请权限，SDK版本大于等于6.0，需要申请权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            try {
                ActivityCompat.requestPermissions(activity,needPermissions.toTypedArray(),requestCode)
                return false
            }catch (ex:Exception){
            }
        }

        return true
    }


    /**
     * 检测申请授权结果
     */
    fun checkGrantIsSuccess(grantResults:List<Int>):Boolean{
        if (grantResults.isNullOrEmpty()){
            return true
        }
        grantResults.forEach {
            //只要有一个权限未赋予，则直接权限拒绝,返回false
            if (it != PackageManager.PERMISSION_GRANTED){
                return false
            }
        }
        return true
    }
}