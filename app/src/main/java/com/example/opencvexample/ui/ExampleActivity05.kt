package com.example.opencvexample.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.SeekBar
import com.example.opencvexample.R
import com.example.opencvexample.base.BaseActivity

/**
 * 图像的基本操作
 */
class ExampleActivity05:BaseActivity() {

    override fun onApplyPermissionSuccess() {

    }

    //ImageView组件
    val imageView: ImageView by lazy{
        findViewById<ImageView>(R.id.iv_lake)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example_05)

        buttonEvent()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun buttonEvent(){


    }

    /**
     * 显示绘制的图形
     */
    private fun showDrawShapes(){


    }

}