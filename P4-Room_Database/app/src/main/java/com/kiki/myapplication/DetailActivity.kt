package com.kiki.myapplication

import AppExecutor
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kiki.myapplication.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var appExecutor: AppExecutor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appExecutor = AppExecutor()

        // Setup Spinner Status Pernikahan
        val statusPernikahan = arrayOf("Belum Menikah", "Menikah", "Cerai")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statusPernikahan)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerStatusPernikahan.adapter = spinnerAdapter

        val wargaId = intent.getIntExtra("warga_id", -1)
        if (wargaId != -1) {
            appExecutor.diskIO.execute {
                val dao = DatabaseWarga.getDatabase(this@DetailActivity).wargaDao()
                val selectedWarga = dao.getWargaById(wargaId)

                runOnUiThread {
                    binding.apply {
                        etNamaLengkap.setText(selectedWarga.namaLengkap)
                        etNik.setText(selectedWarga.nik)
                        etKabupaten.setText(selectedWarga.kabupaten)
                        etKecamatan.setText(selectedWarga.kecamatan)
                        etDesa.setText(selectedWarga.desa)
                        etRt.setText(selectedWarga.rt)
                        etRw.setText(selectedWarga.rw)

                        // Set Radio Button
                        when (selectedWarga.jenisKelamin) {
                            "Laki-Laki" -> rbLakiLaki.isChecked = true
                            "Perempuan" -> rbPerempuan.isChecked = true
                        }

                        // Set Spinner
                        val statusPosition = statusPernikahan.indexOf(selectedWarga.statusPernikahan)
                        if (statusPosition >= 0) {
                            spinnerStatusPernikahan.setSelection(statusPosition)
                        }

                        // Tombol Update
                        btnUpdate.setOnClickListener {
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
                            val status = spinnerStatusPernikahan.selectedItem.toString()

                            // Validasi Input
                            if (namaLengkap.isEmpty()) {
                                Toast.makeText(this@DetailActivity, "Nama Lengkap harus diisi!", Toast.LENGTH_SHORT).show()
                                return@setOnClickListener
                            }
                            if (nik.isEmpty() || nik.length != 16 || !nik.all { it.isDigit() }) {
                                Toast.makeText(this@DetailActivity, "NIK harus 16 digit angka!", Toast.LENGTH_SHORT).show()
                                return@setOnClickListener
                            }
                            if (kabupaten.isEmpty() || kecamatan.isEmpty() || desa.isEmpty() || rt.isEmpty() || rw.isEmpty()) {
                                Toast.makeText(this@DetailActivity, "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
                                return@setOnClickListener
                            }
                            if (jenisKelamin.isEmpty()) {
                                Toast.makeText(this@DetailActivity, "Pilih jenis kelamin!", Toast.LENGTH_SHORT).show()
                                return@setOnClickListener
                            }

                            val updatedWarga = selectedWarga.copy(
                                namaLengkap = namaLengkap,
                                nik = nik,
                                kabupaten = kabupaten,
                                kecamatan = kecamatan,
                                desa = desa,
                                rt = rt,
                                rw = rw,
                                jenisKelamin = jenisKelamin,
                                statusPernikahan = status
                            )

                            appExecutor.diskIO.execute {
                                dao.update(updatedWarga)
                                runOnUiThread {
                                    Toast.makeText(this@DetailActivity, "Data berhasil diupdate!", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                            }
                        }

                        // Tombol Delete
                        btnDelete.setOnClickListener {
                            appExecutor.diskIO.execute {
                                dao.delete(selectedWarga)
                                runOnUiThread {
                                    Toast.makeText(this@DetailActivity, "Data berhasil dihapus!", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}