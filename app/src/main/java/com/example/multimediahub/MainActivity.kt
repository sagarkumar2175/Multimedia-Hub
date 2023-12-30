package com.example.multimediahub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.multimediahub.Adapter.AdapterViewPager
import com.example.multimediahub.R
import com.example.multimediahub.fragment.FragmentAudio
import com.example.multimediahub.fragment.FragmentImages
import com.example.multimediahub.fragment.FragmentPDFs
import com.example.multimediahub.fragment.FragmentVideos
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    private var pagerMain: ViewPager2? = null
    private var fragmentArrayList = ArrayList<Fragment>()
    var bottomNav: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        pagerMain = findViewById(R.id.pagerMain)
        bottomNav = findViewById(R.id.bottomNav)

        fragmentArrayList.add(FragmentPDFs())
        fragmentArrayList.add(FragmentImages())
        fragmentArrayList.add(FragmentVideos())
        fragmentArrayList.add(FragmentAudio())
        val adapterViewPager = AdapterViewPager(this, fragmentArrayList)

        pagerMain?.adapter = adapterViewPager
        pagerMain?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        bottomNav?.selectedItemId = R.id.itPDFs
                    }

                    1 -> {
                        bottomNav?.selectedItemId = R.id.itImages
                    }

                    2 -> {
                        bottomNav?.selectedItemId = R.id.itVideos
                    }

                    3 -> {
                        bottomNav?.selectedItemId = R.id.itAudio
                    }
                }
                super.onPageSelected(position)
            }
        })

        bottomNav?.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.itPDFs -> {
                    pagerMain?.currentItem = 0
                }

                R.id.itImages -> {
                    pagerMain?.currentItem = 1
                }

                R.id.itVideos -> {
                    pagerMain?.currentItem = 2
                }

                R.id.itAudio -> {
                    pagerMain?.currentItem = 3
                }
            }
            true
        })

    }

}








