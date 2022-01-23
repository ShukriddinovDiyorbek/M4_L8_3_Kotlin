package com.example.kotlin_fragment_task3

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.kotlin_fragment_task3.fragment.FirstFragment
import com.example.kotlin_fragment_task3.fragment.ThirdFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

@RequiresApi(Build.VERSION_CODES.M)
class MainActivity : AppCompatActivity() {

    companion object {
        const val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissionPhone()
        permissionCall()
        loadData()
    }


    private fun loadData() {
        val firstFragment = FirstFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_first, firstFragment)
            commit()
        }
    }

    private fun permissionPhone() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )
        }
    }

    private fun permissionCall() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.CALL_PHONE),
                101
            )
        }
    }
}