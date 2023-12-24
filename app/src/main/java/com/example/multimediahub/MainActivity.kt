package com.example.multimediahub

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputBinding
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.multimediahub.Adapter.AdapterViewPager
import com.example.multimediahub.Adapter.PDFsAdapter
import com.example.multimediahub.fragment.FragmentAudio
import com.example.multimediahub.fragment.FragmentImage

import com.example.multimediahub.fragment.FragmentPDFs
import com.example.multimediahub.fragment.FragmentVideos
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File

class MainActivity : AppCompatActivity() {

    private var pagerMain: ViewPager2? = null
    private var fragmentArrayList = ArrayList<Fragment>()
    var bottomNav: BottomNavigationView? = null

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            this@MainActivity,
            Manifest.permission.READ_EXTERNAL_STORAGE)

        return if (result == PackageManager.PERMISSION_GRANTED) {
            true
        } else false
    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this@MainActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            Toast.makeText(
                this@MainActivity,
                "Storage permission is required, please allow from settings",
                Toast.LENGTH_SHORT
            ).show()
        } else ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf<String>(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            101
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pagerMain = findViewById(R.id.pagerMain)
        bottomNav = findViewById(R.id.bottomNav)
        fragmentArrayList.add(FragmentPDFs())
        fragmentArrayList.add(FragmentImage())
        fragmentArrayList.add(FragmentVideos())
        fragmentArrayList.add(FragmentAudio())
        val adapterViewPager = AdapterViewPager(this, fragmentArrayList)
        //setAdapter
        pagerMain?.run {
            adapter = adapterViewPager
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    when (position) {
                        0 -> {
                            bottomNav?.run { selectedItemId = R.id.itPDFs }
                        }

                        1 -> {
                            bottomNav?.run { selectedItemId = R.id.itImages }
                        }

                        2 -> {
                            bottomNav?.run { selectedItemId = R.id.itVideos }
                        }

                        3 -> {
                            bottomNav?.run { selectedItemId = R.id.itAudio }
                        }
                    }
                    super.onPageSelected(position)
                }
            })
        }
        bottomNav.run {
            this?.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.itPDFs -> {
                        pagerMain?.run { currentItem = 0 }
                    }

                    R.id.itImages -> {
                        pagerMain?.run { currentItem = 1 }
                    }

                    R.id.itVideos -> {
                        pagerMain?.run { currentItem = 2 }
                    }

                    R.id.itAudio -> {
                        pagerMain?.run { currentItem = 3 }
                    }
                }
                true
            })
        }

    }


}


