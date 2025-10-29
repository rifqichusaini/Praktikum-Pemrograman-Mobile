package com.kiki.myapplication

import AppExecutor
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.kiki.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbWarga: DatabaseWarga
    private lateinit var wargaDao: WargaDao
    private lateinit var appExecutors: AppExecutor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appExecutors = AppExecutor()
        dbWarga = DatabaseWarga.getDatabase(applicationContext)
        wargaDao = dbWarga.wargaDao()

        // Setup Spinner Status Pernikahan
        val statusPernikahan = arrayOf("Belum Menikah", "Menikah", "Cerai")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statusPernikahan)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerStatusPernikahan.adapter = spinnerAdapter

        binding.apply {
            // Tombol Simpan Data
            btnSimpan.setOnClickListener {
                val namaLengkap = etNamaLengkap.text.toString().trim()
                val nik = etNik.text.toString().trim()
                val kabupaten = etKabupaten.text.toString().trim()
                val kecamatan = etKecamatan.text.toString().trim()
                val desa = etDesa.text.toString().trim()
                val rt = etRt.text.toString().trim()
                val rw = etRw.text.toString().trim()
                val jenisKelamin = when (rgJenisKelamin.checkedRadioButtonId) {
                    R.id.rbLakiLaki -> "Laki-Laki"
                    R.id.rbPerempuan -> "Perempuan"
                    else -> ""
                }
                val statusPernikahan = spinnerStatusPernikahan.selectedItem.toString()

                // Validasi Input
                if (namaLengkap.isEmpty()) {
                    Toast.makeText(this@MainActivity, "Nama Lengkap harus diisi!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (nik.isEmpty() || nik.length != 16 || !nik.all { it.isDigit() }) {
                    Toast.makeText(this@MainActivity, "NIK harus 16 digit angka!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (kabupaten.isEmpty() || kecamatan.isEmpty() || desa.isEmpty() || rt.isEmpty() || rw.isEmpty()) {
                    Toast.makeText(this@MainActivity, "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (jenisKelamin.isEmpty()) {
                    Toast.makeText(this@MainActivity, "Pilih jenis kelamin!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Simpan ke Database
                val newWarga = Warga(
                    namaLengkap = namaLengkap,
                    nik = nik,
                    kabupaten = kabupaten,
                    kecamatan = kecamatan,
                    desa = desa,
                    rt = rt,
                    rw = rw,
                    jenisKelamin = jenisKelamin,
                    statusPernikahan = statusPernikahan
                )

                appExecutors.diskIO.execute {
                    wargaDao.insert(newWarga)
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()
                        resetForm()
                    }
                }
            }

            // Tombol Reset Data
            btnReset.setOnClickListener {
                resetForm()
            }

            // Observe data dari database
            val wargaList = wargaDao.getAllWarga()
            wargaList.observe(this@MainActivity, Observer { list ->
                val wargaAdapter = WargaAdapter(this@MainActivity, list)
                lvWarga.adapter = wargaAdapter

                lvWarga.setOnItemClickListener { _, _, position, _ ->
                    val selectedWarga = list[position]
                    val detailIntent = Intent(this@MainActivity, DetailActivity::class.java)
                    detailIntent.putExtra("warga_id", selectedWarga.id)
                    startActivity(detailIntent)
                }
            })
        }
    }

    private fun resetForm() {
        binding.apply {
            etNamaLengkap.text?.clear()
            etNik.text?.clear()
            etKabupaten.text?.clear()
            etKecamatan.text?.clear()
            etDesa.text?.clear()
            etRt.text?.clear()
            etRw.text?.clear()
            rgJenisKelamin.clearCheck()
            spinnerStatusPernikahan.setSelection(0)
        }
    }
}