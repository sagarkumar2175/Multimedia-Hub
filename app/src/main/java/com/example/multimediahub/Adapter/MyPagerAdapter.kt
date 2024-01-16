package com.example.multimediahub.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.multimediahub.Fragment.ImagesFragment
import com.example.multimediahub.Fragment.MusicFragment
import com.example.multimediahub.Fragment.PdfFragment
import com.example.multimediahub.Fragment.VideoFragment

class MyPagerAdapter(supportFragmentManager: FragmentManager) : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ImagesFragment() // Replace with your ImagesFragment class
            1 -> PdfFragment() // Replace with your PdfFragment class
            2 -> MusicFragment() // Replace with your MusicFragment class
            3 -> VideoFragment() // Replace with your VideoFragment class
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    override fun getCount(): Int {
        return 4 // Number of fragments
    }
}
