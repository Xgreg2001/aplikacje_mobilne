package com.example.mygallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment

class ImageDetailsFragment : Fragment() {

    private var imageData: ImageData? = null
    private lateinit var imageView: ImageView
    private lateinit var description: TextView
    private lateinit var ratingBar: RatingBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageData = it.getParcelable(ARG_IMAGE_DATA)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView = view.findViewById(R.id.image)
        description = view.findViewById(R.id.description)
        ratingBar = view.findViewById(R.id.ratingBar)

        imageData?.let { updateImage(it) }

        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            imageData?.let {
                it.rating = rating
                (activity as? ImageGalleryFragment.OnImageRatingChangedListener)?.onImageRatingChanged(it)
            }
        }
    }

    fun updateImage(imageData: ImageData) {
        this.imageData = imageData
        imageView.setImageResource(imageData.imageResourceId)
        description.text = imageData.description
        ratingBar.rating = imageData.rating
    }

    companion object {
        private const val ARG_IMAGE_DATA = "image_data"

        @JvmStatic
        fun newInstance(imageData: ImageData?) =
            ImageDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_IMAGE_DATA, imageData)
                }
            }
    }
}