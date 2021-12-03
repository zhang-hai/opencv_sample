package com.github.harry.sketch.base

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.harry.sketch.utils.PermissionUtils

abstract class BaseActivity:AppCompatActivity() {

    companion object{
        const val PERMISSION_REQUEST_CODE = 100
    }


    /**
     * 线上toast消息
     */
    fun showToast(msg:String) = Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE){
            if (PermissionUtils.checkGrantIsSuccess(grantResults.toList())){
                onApplyPermissionSuccess()
            }else{
                showToast("权限申请失败")
            }
        }
    }

    /**
     * 权限申请成功后，后续逻辑处理
     */
    abstract fun onApplyPermissionSuccess()


    fun <T> startActivity(clazz:Class<T>){
        val intent = Intent(this,clazz)
        startActivity(intent)
    }
}