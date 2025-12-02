package com.kiki.post

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.kiki.post.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Book Detail"

        // Get data from intent
        val title = intent.getStringExtra("BOOK_TITLE")
        val originalTitle = intent.getStringExtra("BOOK_ORIGINAL_TITLE")
        val cover = intent.getStringExtra("BOOK_COVER")
        val releaseDate = intent.getStringExtra("BOOK_RELEASE")
        val pages = intent.getIntExtra("BOOK_PAGES", 0)
        val description = intent.getStringExtra("BOOK_DESCRIPTION")

        // Set data to views
        binding.apply {
            tvDetailTitle.text = title
            tvDetailOriginalTitle.text = originalTitle ?: title
            tvDetailReleaseDate.text = releaseDate
            tvDetailDescription.text = description
            tvPages.text = ("Pages : " + pages.toString())

            // Load cover image
            Glide.with(this@DetailActivity)
                .load(cover)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(ivDetailCover)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}