package com.example.mygallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.pager)
        tabLayout = findViewById(R.id.tab_layout)

        // Przykład listy danych obrazów
        val images = listOf<ImageData>(
            ImageData(R.drawable.image1, "Opis zdjęcia 1", 0f),
            ImageData(R.drawable.image2, "Opis zdjęcia 2", 0f),
            ImageData(R.drawable.image3, "Opis zdjęcia 3", 0f),
            ImageData(R.drawable.image4, "Opis zdjęcia 4", 0f),
            ImageData(R.drawable.image5, "Opis zdjęcia 5", 0f),
        )


        val adapter = ImagePagerAdapter(this, images)
        viewPager.adapter = adapter

        // Integracja ViewPager2 z TabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            // Można dostosować sposób wyświetlania tytułu karty
            tab.text = "Zdjęcie ${position + 1}"
        }.attach()

        // Obsługa zapisywania stanu ViewPager2
        savedInstanceState?.let {
            viewPager.currentItem = it.getInt("currentItem", 0)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("currentItem", viewPager.currentItem)
    }
}