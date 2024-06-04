package com.example.quran

import MyRecycleViewAdapter
import android.graphics.Point
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.Klaxon


fun TextView.charLocation(offset: Int): Point? {
    layout ?: return null // Layout may be null right after change to the text view

    val lineOfText = layout.getLineForOffset(offset)
    val xCoordinate = layout.getPrimaryHorizontal(offset).toInt()
    val yCoordinate = layout.getLineTop(lineOfText)
    return Point(xCoordinate, yCoordinate)
}

class MainActivity : AppCompatActivity() {
//    lateinit var ayahs: List<Ayah>
    lateinit var pages: List<List<Ayah>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val quranJson: String =
            applicationContext.assets.open("quran.json").bufferedReader().use { it.readText() }

        val quran = Klaxon().parse<QuranDto>(quranJson) as QuranDto

//        ayahs = quran.quran.surahs.flatMap { it.ayahs.toList() }
        pages = quran.quran.surahs.map { it.ayahs.toList() }


        // set up the RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.quranScroll)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MyRecycleViewAdapter(pages, this)

//        val textScroll = findViewById<ScrollView>(R.id.textScroll)
//
//        textScroll.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//            Log.i("OFFSET", offset.toString())
//
//            Log.i("lastPaginateScrollY", lastPaginateScrollY.toString())
//            Log.i("scrollY", scrollY.toString())
//
//            if (scrollY - lastPaginateScrollY > 50) {
//                val textView = findViewById<TextView>(R.id.quranText)
//
//                val totalHeight = textView.height
//
//                val isEndReached = totalHeight / 1.4 < (v.height + scrollY)
//
//                Log.i("HEIGHT", totalHeight.toString())
//                Log.i("SCROLL", (v.height + scrollY).toString())
//
//                if (isEndReached) {
//                    lastPaginateScrollY = scrollY
//                    onLoadMore()
//                }
//            }
//
//        }
//
//        onRenderText(IntRange(0, limit), null)
    }

//    @RequiresApi(Build.VERSION_CODES.P)
//    private fun onRenderText(renderRange: IntRange, cutIndex: Int?) {
//
//        val textView = findViewById<TextView>(R.id.quranText)
//
//        val renderAyahs = ayahs.slice(renderRange)
//
//        val quranText = renderAyahs.joinToString(" ") {
//            it.text
//        }
//
//        val quranSpannable = SpannableStringBuilder(quranText)
//
//        var totalCounts = 0
//
//        renderAyahs.forEachIndexed { index, it ->
//            val currentCountStart = totalCounts
//            val clickableSpan: ClickableSpan = object : ClickableSpan() {
//                override fun onClick(view: View) {
////                    Log.i("sdfsdfsd", textView.charLocation(currentCountStart - 1)?.y.toString())
////                    Log.i("APP LOGS", "PRESSED message")
//                }
//
//                override fun updateDrawState(textPaint: TextPaint) {
//                    super.updateDrawState(textPaint)
//                    textPaint.isUnderlineText = false
//                    val rnd: Random = Random()
//                    textPaint.color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
//                }
//            }
//
//            if (index % 10 == 0) {
////                val surahIcon: Drawable = getResources().getDrawable(R.drawable.surah);
////                surahIcon.setBounds(0, 0, surahIcon.getIntrinsicWidth() * 3, 24 * 3);
////
////                quranSpannable.setSpan(
////                    ImageSpan(surahIcon, ImageSpan.ALIGN_BASELINE),
////                    totalCounts,
////                    totalCounts + 1,
////                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
////                )
//
//                quranSpannable.setSpan(
//                    clickableSpan,
//                    totalCounts,
//                    totalCounts + it.text.length,
//                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//                )
//            }
//
//            totalCounts += it.text.length + 1
//        }
//
//        textView.text = quranSpannable
//        textView.movementMethod = LinkMovementMethod.getInstance()
//
//        val params = textView.textMetricsParams
//        val ref = WeakReference(textView)
//
//        GlobalScope.launch(Dispatchers.Default) {
//            val pText = PrecomputedText.create(quranSpannable, params)
//            withContext(Dispatchers.Main) {
//                ref.get()?.let { textView ->
//
//                    if (cutIndex != null) {
////                        Log.i("CUT TEXT Y POSITION ===>",  textView.charLocation(cutIndex)?.y.toString())
//
//                        val cutPosition = textView.charLocation(cutIndex)?.y
//
//                        if (cutPosition != null) {
//                            val textScroll = findViewById<ScrollView>(R.id.textScroll)
//
//                            textScroll.scrollBy(0, textScroll.scrollY - cutPosition)
//                        }
//                    }
//
//                    textView.text = pText
//
//
//                }
//            }
//        }
//
//        prevRange = renderRange
//    }
//
//    private fun onLoadMore() {
//        val isEnd = offset >= ayahs.size
//
//        var renderedPage = (prevRange.last + limit - prevRange.first) / limit
//        var startIndex = prevRange.first
//        var cutIndex: Int? = null
//
//        if (renderedPage >= 3) {
//            cutIndex = prevRange.first + limit
//            startIndex = prevRange.first + limit
//        }
//
////        Log.i("RENDER INDEX", startIndex.toString())
//        Log.i("RENDER OFFSET", offset.toString())
//
//        if (!isEnd) {
//            if (offset + limit > ayahs.size) {
//                Log.i("END ----->>>>", IntRange(startIndex, ayahs.size - 1).toString())
//
//                onRenderText(IntRange(startIndex, ayahs.size - 1), cutIndex)
//            }
//
//            onRenderText(IntRange(startIndex, offset + limit), cutIndex)
//            offset += limit
//        }
//    }
//
//    private fun onLoadBack() {
//        val isEnd = offset == 0
//
//        var renderedPage = (prevRange.last - (prevRange.first - limit)) / limit
//        var startIndex = prevRange.first
//
//        if (renderedPage >= 10) {
//            startIndex = prevRange.first - limit
//        }
//
//        Log.i("BACK --- RENDER INDEX", startIndex.toString())
//        Log.i("BACK --- RENDER OFFSET", offset.toString())
//
//        if (!isEnd) {
//            if (offset - limit <= 0) {
//                onRenderText(IntRange(startIndex, ayahs.size - 1))
//            }
//
//            onRenderText(IntRange(startIndex, offset + limit))
//            offset += limit
//        }
//    }
}
