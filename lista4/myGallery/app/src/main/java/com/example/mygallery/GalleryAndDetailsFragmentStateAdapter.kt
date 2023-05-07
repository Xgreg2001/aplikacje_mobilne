package com.example.mygallery

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class GalleryAndDetailsFragmentStateAdapter(activity: FragmentActivity, private val images: List<ImageData>) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ImageGalleryFragment.newInstance(images)
            else -> ImageDetailsFragment.newInstance(null)
        }
    }
}