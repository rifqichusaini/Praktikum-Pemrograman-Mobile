package com.kiki.post

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kiki.post.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hide action bar
        supportActionBar?.hide()

        binding.rvBooks.layoutManager = LinearLayoutManager(this)

        fetchBooks()
    }

    private fun fetchBooks() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://potterapi-fedeperin.vercel.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        api.getBooks().enqueue(object : Callback<List<Book>> {
            override fun onResponse(
                call: Call<List<Book>>,
                response: Response<List<Book>>
            ) {
                if (response.isSuccessful) {
                    val books = response.body() ?: emptyList()

                    adapter = BookAdapter(books) { book ->
                        // Handle item click - navigate to detail
                        val intent = Intent(this@MainActivity, DetailActivity::class.java)
                        intent.putExtra("BOOK_TITLE", book.title)
                        intent.putExtra("BOOK_ORIGINAL_TITLE", book.originalTitle)
                        intent.putExtra("BOOK_COVER", book.cover)
                        intent.putExtra("BOOK_RELEASE", book.releaseDate)
                        intent.putExtra("BOOK_PAGES", book.pages)
                        intent.putExtra("BOOK_DESCRIPTION", book.description)
                        startActivity(intent)
                    }

                    binding.rvBooks.adapter = adapter
                }
            }

            override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}