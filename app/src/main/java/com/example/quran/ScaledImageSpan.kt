package com.example.quran

import android.graphics.BitmapFactory
import android.text.style.DynamicDrawableSpan
import android.text.style.ImageSpan
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

fun createScaledImageSpan(context: Context, resId: Int, width: Int, height: Int): ImageSpan {
    val drawable: Drawable? = VectorDrawableCompat.create(context.resources, resId, context.theme)
    return if (drawable != null) {
        drawable.setBounds(0, 0, width, height)
        ImageSpan(drawable, DynamicDrawableSpan.ALIGN_CENTER)
    } else {
        val placeholderDrawable = context.resources.getDrawable(R.drawable.bismillah, null)
        placeholderDrawable.setBounds(0, 0, width, height)
        ImageSpan(placeholderDrawable, DynamicDrawableSpan.ALIGN_CENTER)
    }
}

private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2

        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}
