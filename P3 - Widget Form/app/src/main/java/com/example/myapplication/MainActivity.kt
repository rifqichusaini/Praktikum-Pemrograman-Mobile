package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.ResultActivity.Companion.DATA_AGE
import com.example.myapplication.ResultActivity.Companion.DATA_EMAIL
import com.example.myapplication.ResultActivity.Companion.DATA_FULL_NAME
import com.example.myapplication.ResultActivity.Companion.DATA_GENDER
import com.example.myapplication.ResultActivity.Companion.DATA_USERNAME
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var gender = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnSubmit.setOnClickListener {
                submitForm()
            }
        }
    }

    private fun submitForm() {
        binding.apply {
            // Get input values
            val fullName = etFullName.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val age = etAge.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (fullName.isEmpty()) {
                Toast.makeText(this@MainActivity, "Please enter your full name", Toast.LENGTH_SHORT).show()
                return
            }

            if (username.isEmpty()) {
                Toast.makeText(this@MainActivity, "Please enter your username", Toast.LENGTH_SHORT).show()
                return
            }

            if (age.isEmpty()) {
                Toast.makeText(this@MainActivity, "Please enter your age", Toast.LENGTH_SHORT).show()
                return
            }

            if (email.isEmpty()) {
                Toast.makeText(this@MainActivity, "Please enter your email", Toast.LENGTH_SHORT).show()
                return
            }

            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            if (!email.matches(emailPattern.toRegex())) {
                Toast.makeText(this@MainActivity, "Format email tidak valid", Toast.LENGTH_SHORT).show()
                return
            }

            if (rbMale.isChecked) {
                gender = "Laki-laki"
            } else if (rbFemale.isChecked) {
                gender = "Perempuan"
            } else {
                Toast.makeText(this@MainActivity, "Please choose your gender", Toast.LENGTH_SHORT).show()
                return
            }

            if (password.isEmpty()) {
                Toast.makeText(this@MainActivity, "Please enter your password", Toast.LENGTH_SHORT).show()
                return
            }

            if (confirmPassword.isEmpty()) {
                Toast.makeText(this@MainActivity, "Please confirm your password", Toast.LENGTH_SHORT).show()
                return
            }

            if (password != confirmPassword) {
                Toast.makeText(this@MainActivity, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return
            }

            // Create intent to ResultActivity
            val intent = Intent(this@MainActivity, ResultActivity::class.java)
                .putExtra(DATA_FULL_NAME, fullName)
                .putExtra(DATA_USERNAME, username)
                .putExtra(DATA_AGE, age)
                .putExtra(DATA_EMAIL, email)
                .putExtra(DATA_GENDER, gender)
            startActivity(intent)
        }
    }
}