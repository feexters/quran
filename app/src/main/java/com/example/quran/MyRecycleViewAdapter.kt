
import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
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

    private lateinit var mRecyclerView: RecyclerView

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

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    fun scrollToPosition(position: Int) {
        mRecyclerView.scrollToPosition(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//  TODO: use selected ayahs global state and use ids
        var selectedIndexes = emptyList<Int>().toMutableList()

        fun onPressAyah(selectedIndex: Int) {
            if (selectedIndexes.indexOf(selectedIndex) > -1) {
                selectedIndexes =
                    selectedIndexes.filter {
                        it != selectedIndex
                    }.toMutableList()
            } else {
                selectedIndexes += selectedIndex
            }

            holder.quranPage.text =
                onGetText(pages[position], selectedIndexes) { index -> onPressAyah(index) }
        }

        holder.quranPage.text =
            onGetText(pages[position], selectedIndexes) { index -> onPressAyah(index) }
        holder.quranPage.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun getItemCount(): Int {
        return pages.size
    }

    private fun onGetText(
        ayahs: List<Ayah>,
        selectedIndexes: List<Int>,
        onPress: (index: Int) -> Unit
    ): SpannableStringBuilder {
        val quranText = ayahs.joinToString(" ") {
            it.text
        }

        val quranSpannable = SpannableStringBuilder(quranText)

        var totalCounts = 0

        ayahs.forEachIndexed { index, it ->
            val clickableSpan: ClickableSpan = object : ClickableSpan() {
                override fun onClick(view: View) {
                    onPress(index)
                }

                override fun updateDrawState(textPaint: TextPaint) {
                    super.updateDrawState(textPaint)
                    textPaint.isUnderlineText = false

                    if (selectedIndexes.indexOf(index) > -1) {
                        textPaint.color = Color.GREEN
                    } else {
                        textPaint.color = Color.BLACK
                    }
                }
            }

            quranSpannable.setSpan(
                clickableSpan,
                totalCounts,
                totalCounts + it.text.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            totalCounts += it.text.length + 1
        }

        return quranSpannable
    }
}