package com.example.mygallery

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ImagePagerAdapter(
    fragmentActivity: FragmentActivity,
    private val images: List<ImageData>
) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return ImageFragment.newInstance(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }
}