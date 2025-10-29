package com.kiki.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class WargaAdapter(private val context: Context, private val wargaList: List<Warga>) : BaseAdapter() {

    override fun getCount(): Int = wargaList.size

    override fun getItem(position: Int): Any = wargaList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_warga, parent, false)

        val warga = wargaList[position]

        val tvNama = view.findViewById<TextView>(R.id.tvNama)
        val tvNik = view.findViewById<TextView>(R.id.tvNik)
        val tvAlamat = view.findViewById<TextView>(R.id.tvAlamat)

        tvNama.text = "${warga.namaLengkap} (${warga.jenisKelamin}) - ${warga.statusPernikahan}"
        tvNik.text = "NIK: ${warga.nik}"
        tvAlamat.text = "Alamat: RT ${warga.rt}/RW ${warga.rw}, ${warga.desa}, ${warga.kecamatan}, ${warga.kabupaten}"

        return view
    }
}