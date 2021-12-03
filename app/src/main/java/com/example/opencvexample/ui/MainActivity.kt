package com.example.opencvexample.ui

import android.os.Bundle
import android.widget.TextView
import com.example.opencvexample.R
import com.example.opencvexample.base.BaseActivity
import com.example.opencvexample.ui.chapter02.*
import com.example.opencvexample.ui.chapter03.Chapter03Activity

class MainActivity : BaseActivity() {

    override fun onApplyPermissionSuccess() {
        TODO("Not yet implemented")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.btn_example_01).setOnClickListener {
            startActivity(Chapter02Activity::class.java)
        }
        findViewById<TextView>(R.id.btn_example_02).setOnClickListener {
            startActivity(Chapter03Activity::class.java)
        }
        findViewById<TextView>(R.id.btn_example_03).setOnClickListener {
            startActivity(ExampleActivity03::class.java)
        }
        findViewById<TextView>(R.id.btn_example_04).setOnClickListener {
            startActivity(ExampleActivity04::class.java)
        }
        findViewById<TextView>(R.id.btn_example_05).setOnClickListener {
            startActivity(ExampleActivity05::class.java)
        }
    }


}