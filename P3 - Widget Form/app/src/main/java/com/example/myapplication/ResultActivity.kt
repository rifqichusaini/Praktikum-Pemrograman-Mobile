package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(getLayoutInflater())
        setContentView(binding.root)

        val fullName = getIntent().getStringExtra(ResultActivity.Companion.DATA_FULL_NAME)
        val username = getIntent().getStringExtra(ResultActivity.Companion.DATA_USERNAME)
        val age = getIntent().getStringExtra(ResultActivity.Companion.DATA_AGE)
        val email = getIntent().getStringExtra(ResultActivity.Companion.DATA_EMAIL)
        val gender = getIntent().getStringExtra(ResultActivity.Companion.DATA_GENDER)

        binding.tvFullName.setText("Full Name: " + fullName)
        binding.tvUsername.setText("Username: " + username)
        binding.tvAge.setText("Age: " + age)
        binding.tvEmail.setText("Email: " + email)
        binding.tvGender.setText("Gender: " + gender)
    }

    companion object {
        const val DATA_FULL_NAME: String = "data_full_name"
        const val DATA_USERNAME: String = "data_username"
        const val DATA_AGE: String = "data_age"
        const val DATA_EMAIL: String = "data_email"
        const val DATA_GENDER: String = "data_gender"
    }
}