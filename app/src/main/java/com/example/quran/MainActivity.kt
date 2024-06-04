package com.example.quran

import MyRecycleViewAdapter
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.Klaxon

class MainActivity : AppCompatActivity() {
    lateinit var pages: List<List<Ayah>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val quranJson: String =
            applicationContext.assets.open("quran.json").bufferedReader().use { it.readText() }

        val quran = Klaxon().parse<QuranDto>(quranJson) as QuranDto

        pages = quran.quran.surahs.map { it.ayahs.toList() }

        val recyclerView = findViewById<RecyclerView>(R.id.quranScroll)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MyRecycleViewAdapter(pages, this)
    }
}
