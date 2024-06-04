
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quran.Ayah
import com.example.quran.R


class MyRecycleViewAdapter(private val pages: List<List<Ayah>>, val context: Context) :
    RecyclerView.Adapter<MyRecycleViewAdapter.ViewHolder>() {
    // holder class to hold reference
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //get view reference
        var quranPage: TextView = view.findViewById<TextView>(R.id.quran_page)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create view holder to hold reference
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycleview_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //set values
        holder.quranPage.text = onGetText(pages[position])
    }

    override fun getItemCount(): Int {
        return pages.size
    }

    private fun onGetText(ayahs: List<Ayah>): SpannableStringBuilder {
        val quranText = ayahs.joinToString(" ") {
            it.text
        }

        val quranSpannable = SpannableStringBuilder(quranText)

        var totalCounts = 0

        ayahs.forEachIndexed { index, it ->
            val clickableSpan: ClickableSpan = object : ClickableSpan() {
                override fun onClick(view: View) {
//                    Log.i("sdfsdfsd", textView.charLocation(currentCountStart - 1)?.y.toString())
//                    Log.i("APP LOGS", "PRESSED message")
                }

                override fun updateDrawState(textPaint: TextPaint) {
                    super.updateDrawState(textPaint)
                    textPaint.isUnderlineText = false
                    textPaint.color = Color.GREEN
                }
            }

            if (index % 4 == 0) {
                val surahIcon: Drawable = context.getResources().getDrawable(R.drawable.surah);
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

       return quranSpannable
    }
}