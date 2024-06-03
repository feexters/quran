package com.example.quran

import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ImageSpan
import android.util.Log
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.beust.klaxon.Klaxon

fun TextView.charLocation(offset: Int): Point? {
    layout ?: return null // Layout may be null right after change to the text view

    val lineOfText = layout.getLineForOffset(offset)
    val xCoordinate = layout.getPrimaryHorizontal(offset).toInt()
    val yCoordinate = layout.getLineTop(lineOfText)
    return Point(xCoordinate, yCoordinate)
}

class MainActivity : AppCompatActivity() {
    lateinit var ayahs: List<Ayah>
    private var offset: Int = 0
    private var limit: Int = 100
    private var prevRange = IntRange(0, limit)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val quranJson: String =
            applicationContext.assets.open("quran.json").bufferedReader().use { it.readText() }

        val quran = Klaxon().parse<QuranDto>(quranJson) as QuranDto

        ayahs = quran.quran.surahs.flatMap { it.ayahs.toList() }

        val textScroll = findViewById<ScrollView>(R.id.textScroll)

        textScroll.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            val textView = findViewById<TextView>(R.id.quranText)

            val totalHeight = textView.height
            Log.i("HEIGHT",  totalHeight.toString())

            val isEndReached = totalHeight - 500 < (v.height + scrollY)


            if (isEndReached) {
                onLoadMore()
            }
        }

        onRenderText(IntRange(0, limit))
    }

    private fun onRenderText(renderRange: IntRange) {
        val textView = findViewById<TextView>(R.id.quranText)

        if (renderRange.first > prevRange.first) {
            val slicedText = ayahs.slice(IntRange(prevRange.first, renderRange.first)).joinToString(" ") {
                it.text
            }

            val positionY = textView.charLocation(slicedText.length - 1)?.y

            Log.i("RENDER positionY",  positionY.toString())

            if (positionY != null) {
                textView.setPadding(0, positionY, 0, 0)
            }
        }

        val renderAyahs = ayahs.slice(renderRange)

        val quranText = renderAyahs.joinToString(" ") {
            it.text
        }

        val quranSpannable = SpannableStringBuilder(quranText)

        var totalCounts = 0

        renderAyahs.forEachIndexed { index, it ->
            val currentCountStart = totalCounts
            val clickableSpan: ClickableSpan = object : ClickableSpan() {
                override fun onClick(view: View) {
                    val textView = findViewById<TextView>(R.id.quranText)

                    Log.i("sdfsdfsd", textView.charLocation(currentCountStart - 1)?.y.toString())
                    Log.i("APP LOGS", "PRESSED message")
                }

                override fun updateDrawState(textPaint: TextPaint) {
                    super.updateDrawState(textPaint)
                    textPaint.isUnderlineText = false
                    textPaint.color = Color.RED
                }
            }

            if (index % 4 == 0) {
                val surahIcon: Drawable = getResources().getDrawable(R.drawable.surah);
                surahIcon.setBounds(0, 0, surahIcon.getIntrinsicWidth() * 3, 24 * 3);

                quranSpannable.setSpan(
                    ImageSpan(surahIcon, ImageSpan.ALIGN_BASELINE),
                    totalCounts,
                    totalCounts + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                quranSpannable.setSpan(
                    clickableSpan,
                    totalCounts,
                    totalCounts + it.text.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            totalCounts += it.text.length + 1
        }

        textView.text = quranSpannable

        prevRange = renderRange
    }

    private fun onLoadMore() {
        val isEnd = offset >= ayahs.size

        offset += limit

        var renderedPage = (prevRange.last - prevRange.first + limit) / limit
        var startIndex = 0

        if (renderedPage >= 2) {
            startIndex = prevRange.first + limit
        }

        Log.i("RENDER INDEX",  startIndex.toString())
        Log.i("RENDER OFFSET",  offset.toString())

        if (!isEnd) {
            onRenderText(IntRange(startIndex, offset - 1))
        }
    }
}