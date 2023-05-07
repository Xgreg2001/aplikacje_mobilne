package com.example.mygallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class MainActivity : AppCompatActivity(), ImageGalleryFragment.OnImageRatingChangedListener {

    private lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager2
    private var images = mutableListOf(
        ImageData(R.drawable.image1, "Opis obrazu 1", 0f),
        ImageData(R.drawable.image2, "Opis obrazu 2", 0f),
        ImageData(R.drawable.image3, "Opis obrazu 3", 0f),
        ImageData(R.drawable.image4, "Opis obrazu 4", 0f),
        ImageData(R.drawable.image5, "Opis obrazu 5", 0f),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Wczytaj oceny zdjęć
        loadImageRatings()

        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.pager)

        val adapter = GalleryAndDetailsFragmentStateAdapter(this, images)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Galeria"
                else -> "Szczegóły"
            }
        }.attach()
    }

    override fun onImageRatingChanged(imageData: ImageData) {
        // Zaktualizuj wybrany obraz w ImageDetailsFragment
        (supportFragmentManager.findFragmentByTag("f1") as? ImageDetailsFragment)?.updateImage(imageData)

        // Sortuj zdjęcia w galerii po ocenie i zaktualizuj widok
        val galleryFragment = supportFragmentManager.findFragmentByTag("f0") as? ImageGalleryFragment
        galleryFragment?.let {
            it.adapter.sortByRating()
        }
    }

    private fun saveImageRatings() {
        val sharedPreferences = getSharedPreferences("image_ratings", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()

        images.forEachIndexed { index, imageData ->
            val json = gson.toJson(imageData)
            editor.putString("image_$index", json)
        }
        editor.apply()
    }

    private fun loadImageRatings() {
        val sharedPreferences = getSharedPreferences("image_ratings", MODE_PRIVATE)
        val gson = Gson()

        images.forEachIndexed { index, _ ->
            val json = sharedPreferences.getString("image_$index", null)
            if (json != null) {
                val imageData = gson.fromJson(json, ImageData::class.java)
                images[index] = imageData
            }
        }
    }

    override fun onStop() {
        super.onStop()

        // Zapisz oceny zdjęć
        saveImageRatings()
    }
}