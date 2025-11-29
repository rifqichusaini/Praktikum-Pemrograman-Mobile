package com.kiki.p5

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kiki.p5.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var db: DatabaseBarang
    private lateinit var barangDao: BarangDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Init database
        db = DatabaseBarang.getDatabase(this)
        barangDao = db.barangDao()

        // Ambil ID barang dari Intent
        val barangId = intent.getIntExtra("barang_id", -1)

        if (barangId == -1) {
            binding.tvDetailNama.text = "Data tidak ditemukan"
            return
        }

        // Ambil detail barang dari database (Room tidak bisa di main thread → gunakan thread)
        Thread {
            val barang = barangDao.getBarangById(barangId)

            runOnUiThread {
                if (barang != null) {
                    binding.tvDetailId.text = "ID: ${barang.id}"
                    binding.tvDetailNama.text = "Nama: ${barang.nama}"
                    binding.tvDetailJenis.text = "Jenis: ${barang.jenis}"
                    binding.tvDetailHarga.text = "Harga: Rp ${barang.harga}"
                } else {
                    binding.tvDetailNama.text = "Barang tidak ditemukan!"
                }
            }
        }.start()
    }
}
