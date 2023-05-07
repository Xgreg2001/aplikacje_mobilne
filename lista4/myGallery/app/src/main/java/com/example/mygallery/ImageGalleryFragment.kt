package com.example.mygallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class ImageGalleryFragment : Fragment() {
    interface OnImageRatingChangedListener {
        fun onImageRatingChanged(imageData: ImageData)
    }

    private var images: List<ImageData> = emptyList()
    lateinit var adapter: ImageGalleryAdapter private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            images = it.getParcelableArrayList(ARG_IMAGES) ?: emptyList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        adapter = ImageGalleryAdapter(images) { imageData ->
            (activity as? OnImageRatingChangedListener)?.onImageRatingChanged(imageData)
            (activity as MainActivity).viewPager.setCurrentItem(1, true)
        }
        recyclerView.adapter = adapter
    }

    companion object {
        private const val ARG_IMAGES = "images"

        @JvmStatic
        fun newInstance(images: List<ImageData>) =
            ImageGalleryFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_IMAGES, ArrayList(images))
                }
            }
    }
}