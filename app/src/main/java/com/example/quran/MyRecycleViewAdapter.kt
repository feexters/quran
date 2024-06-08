import android.content.Context
import android.graphics.Color
import android.text.Layout
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.AlignmentSpan
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quran.Ayah
import com.example.quran.R
import com.example.quran.createScaledImageSpan
import com.example.quran.toArabicNumerals

class MyRecycleViewAdapter(private val pages: List<List<Ayah>>, val context: Context) :
    RecyclerView.Adapter<MyRecycleViewAdapter.ViewHolder>() {

    private lateinit var mRecyclerView: RecyclerView

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var quranPage: TextView = view.findViewById(R.id.quran_page)
        var pageNumber: TextView = view.findViewById(R.id.page_number)
        var bottomBorder: View = view.findViewById(R.id.border)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycleview_row, parent, false)
        )
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    fun scrollToPosition(position: Int) {
        mRecyclerView.scrollToPosition(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var selectedIndexes = emptyList<Int>().toMutableList()

        fun onPressAyah(selectedIndex: Int) {
            if (selectedIndexes.contains(selectedIndex)) {
                selectedIndexes = selectedIndexes.filter { it != selectedIndex }.toMutableList()
            } else {
                selectedIndexes += selectedIndex
            }

            holder.quranPage.text =
                onGetText(pages[position], selectedIndexes) { index -> onPressAyah(index) }
        }

        holder.quranPage.text =
            onGetText(pages[position], selectedIndexes) { index -> onPressAyah(index) }
        holder.quranPage.movementMethod = LinkMovementMethod.getInstance()

        val page = pages[position]
        holder.pageNumber.text = "${position + 1}"

    }

    override fun getItemCount(): Int {
        return pages.size
    }

    private fun onGetText(
        ayahs: List<Ayah>,
        selectedIndexes: List<Int>,
        onPress: (index: Int) -> Unit
    ): SpannableStringBuilder {
        val quranSpannable = SpannableStringBuilder()

        var totalCounts = 0

        ayahs.forEachIndexed { index, it ->
            if (it.index == 0) {
                val surahNumber = it.surahIndex
                if (surahNumber.toInt() >= 0) {
                    val surahImageResId = context.resources.getIdentifier("surah${surahNumber.toInt() + 1}", "drawable", context.packageName)
                    if (surahImageResId != 0) {
                        quranSpannable.append("\n\n")

                        val imageWidth = 300
                        val imageHeight = 125
                        val surahImageSpan = createScaledImageSpan(context, surahImageResId, imageWidth, imageHeight)

                        val surahImageSpannable = SpannableString(" ")
                        surahImageSpannable.setSpan(surahImageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        surahImageSpannable.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        quranSpannable.append(surahImageSpannable)
                        quranSpannable.append("\n\n")
                    }
                }
            }

            if (it.bismillah != null) {
                val bismillahImageResId = context.resources.getIdentifier("bismillah", "drawable", context.packageName)

                val imageWidth = 500
                val imageHeight = 100

                val bismillahImageSpan = createScaledImageSpan(context, bismillahImageResId, imageWidth, imageHeight)
                val bismillahImageSpannable = SpannableString(" ")
                bismillahImageSpannable.setSpan(bismillahImageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                bismillahImageSpannable.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                quranSpannable.append(bismillahImageSpannable)
                quranSpannable.append("\n\n")
            }

            val clickableSpan: ClickableSpan = object : ClickableSpan() {
                override fun onClick(view: View) {
                    onPress(index)
                }

                override fun updateDrawState(textPaint: TextPaint) {
                    super.updateDrawState(textPaint)
                    textPaint.isUnderlineText = false
                    textPaint.color = if (selectedIndexes.contains(index)) Color.GREEN else Color.BLACK
                }
            }

            val ayahText = it.text
            val ayahIndex = toArabicNumerals(it.index.toInt() + 1)

            val textWithIndex = SpannableString("$ayahText $ayahIndex")

            textWithIndex.setSpan(
                clickableSpan,
                0,
                textWithIndex.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            quranSpannable.append(textWithIndex)

            if (index < ayahs.lastIndex) {
                quranSpannable.append(" ")
            }

            totalCounts += it.text.length + 1
        }

        return quranSpannable
    }
}
