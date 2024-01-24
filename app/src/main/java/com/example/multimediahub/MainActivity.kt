package com.example.multimediahub

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.NestedScrollView
import androidx.viewpager.widget.ViewPager
import com.example.multimediahub.Adapter.MyPagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var bottomNav: BottomNavigationView

//    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
//    private var isReadPermissionGranted = false
//    private var isAudioPermissionGranted = false
//    private var isImagesPermissionGranted = false
//    private var isVideoPermissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkAndRequestStoragePermission()

        viewPager = findViewById(R.id.viewPager)
        bottomNav = findViewById(R.id.bottom_nav)


        val adapter = MyPagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter



        // Set up ViewPager to change fragment on swipe
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                // Update BottomNavigationView when ViewPager page changes
                bottomNav.menu.getItem(position).isChecked = true
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        // Set up BottomNavigationView to change ViewPager page on item click
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_images -> viewPager.currentItem = 0
                R.id.action_pdf -> viewPager.currentItem = 1
                R.id.action_music -> viewPager.currentItem = 2
                R.id.action_video -> viewPager.currentItem = 3
            }
            true
        }
    }

    private fun checkAndRequestStoragePermission() {
        // Check and request storage-related permissions
//        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
//            isReadPermissionGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: isReadPermissionGranted
//            isAudioPermissionGranted = permissions[Manifest.permission.READ_MEDIA_AUDIO] ?: isAudioPermissionGranted
//            isImagesPermissionGranted = permissions[Manifest.permission.READ_MEDIA_IMAGES] ?: isImagesPermissionGranted
//            isVideoPermissionGranted = permissions[Manifest.permission.READ_MEDIA_VIDEO] ?: isVideoPermissionGranted
//        }
//        requestPermission()

        // If the Android version is equal to or greater than Android 11 (R)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                // Request "Manage All Files Access" permission
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(intent)
            }
        }
    }

//    private fun requestPermission() {
//        isReadPermissionGranted = ContextCompat.checkSelfPermission(
//            this,
//            Manifest.permission.READ_EXTERNAL_STORAGE
//        ) == PackageManager.PERMISSION_GRANTED
//
//        isAudioPermissionGranted = ContextCompat.checkSelfPermission(
//            this,
//            Manifest.permission.READ_MEDIA_AUDIO
//        ) == PackageManager.PERMISSION_GRANTED
//
//        isImagesPermissionGranted = ContextCompat.checkSelfPermission(
//            this,
//            Manifest.permission.READ_MEDIA_IMAGES
//        ) == PackageManager.PERMISSION_GRANTED
//
//        isVideoPermissionGranted = ContextCompat.checkSelfPermission(
//            this,
//            Manifest.permission.READ_MEDIA_VIDEO
//        ) == PackageManager.PERMISSION_GRANTED
//
//        val permissionRequest: MutableList<String> = ArrayList()
//
//        if (!isReadPermissionGranted) {
//            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
//        }
//
//        if (!isAudioPermissionGranted) {
//            permissionRequest.add(Manifest.permission.READ_MEDIA_AUDIO)
//        }
//
//        if (!isImagesPermissionGranted) {
//            permissionRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
//        }
//
//        if (!isVideoPermissionGranted) {
//            permissionRequest.add(Manifest.permission.READ_MEDIA_VIDEO)
//        }
//
//        if (permissionRequest.isNotEmpty()) {
//            permissionLauncher.launch(permissionRequest.toTypedArray())
//        }
//    }


}
