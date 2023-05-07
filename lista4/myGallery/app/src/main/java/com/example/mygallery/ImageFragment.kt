package com.example.mygallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment

class ImageFragment : Fragment() {
    private lateinit var image: ImageView
    private lateinit var description: TextView
    private lateinit var ratingBar: RatingBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_image, container, false)

        image = view.findViewById(R.id.image)
        description = view.findViewById(R.id.description)
        ratingBar = view.findViewById(R.id.ratingBar)

        // Pobierz przekazane dane obrazu
        val imageData = arguments?.getParcelable(ARG_IMAGE_DATA, ImageData::class.java)

        imageData?.let {
            // Wczytywanie obrazu
            image.setImageResource(it.imageResourceId)

            // Wczytywanie opisu
            description.text = it.description

            // Wczytywanie aktualnej oceny
            ratingBar.rating = it.rating

            // Obsługa zmian oceny za pomocą RatingBar
            ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
                it.rating = rating
                // Przekazanie oceny do głównej aktywności lub wykonanie innych działań
            }
        }

        return view
    }

    companion object {
            private const val ARG_IMAGE_DATA = "image_data"

            fun newInstance(imageData: ImageData): ImageFragment {
                val args = Bundle()
                args.putParcelable(ARG_IMAGE_DATA, imageData)
                val fragment = ImageFragment()
                fragment.arguments = args
                return fragment
            }
    }
}