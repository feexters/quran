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
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quran.Ayah
import com.example.quran.R
import com.example.quran.toArabicNumerals

class MyRecycleViewAdapter(private val pages: List<List<Ayah>>, val context: Context) :
        RecyclerView.Adapter<MyRecycleViewAdapter.ViewHolder>() {

    private lateinit var mRecyclerView: RecyclerView

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var quranPage: TextView = view.findViewById(R.id.quran_page)
        var surahImage: ImageView = view.findViewById(R.id.surah_image)
        var bismillah: ImageView = view.findViewById(R.id.bismillah)
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


        val surahNumber = page.firstOrNull()?.surahIndex ?: 0
        if (surahNumber.toInt() >= 0) {
            val surahImageResId = context.resources.getIdentifier("surah${surahNumber.toInt() + 1}", "drawable", context.packageName)
            if (surahImageResId != 0) {
                holder.surahImage.setImageResource(surahImageResId)
                holder.surahImage.visibility = View.VISIBLE
            } else {
                holder.surahImage.visibility = View.GONE
            }
        } else {
            holder.surahImage.visibility = View.GONE
        }

        val ayahWithBismillah = page.find { it.bismillah != null }
        val bismillahImageResId = context.resources.getIdentifier("bismillah", "drawable", context.packageName)
        if (ayahWithBismillah!=null) {
            holder.bismillah.setImageResource(bismillahImageResId)
            holder.bismillah.visibility = View.VISIBLE
        } else {
            holder.bismillah.visibility = View.GONE
        }
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

            if (it.pageNumber != null) {
                val pageNumberText = SpannableString("\n\n${it.pageNumber}\n")
                pageNumberText.setSpan(
                    object : ClickableSpan() {
                        override fun onClick(widget: View) {}

                        override fun updateDrawState(ds: TextPaint) {
                            super.updateDrawState(ds)
                            ds.isUnderlineText = false
                            ds.color = Color.GRAY
                            ds.textSize = 52f
                        }
                    },
                    0,
                    pageNumberText.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                pageNumberText.setSpan(
                    AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                    0,
                    pageNumberText.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                quranSpannable.append(pageNumberText)

                val borderText = SpannableString("──────────────\n")
                borderText.setSpan(
                    object : ClickableSpan() {
                        override fun onClick(widget: View) {}

                        override fun updateDrawState(ds: TextPaint) {
                            super.updateDrawState(ds)
                            ds.isUnderlineText = false
                            ds.color = Color.BLACK
                            ds.textSize = 32f
                        }
                    },
                    0,
                    borderText.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                borderText.setSpan(
                    AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                    0,
                    borderText.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                quranSpannable.append(borderText)
            }

        }

        return quranSpannable
    }
}
