package com.homeway.textfab

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton


class TextFloatingActionButton : FloatingActionButton {
    constructor(ctx: Context) : super(ctx)

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

    companion object {
        private const val TEXT_AREA_COMPENSATION_COEFFICIENT = 1.1f
        private const val DEFAULT_ICON_SHRINK_RATIO = 0.87f
    }

    var buttonType: ButtonType = ButtonType.Auto
        set(value) {
            field = value
            refresh()
        }

    @DrawableRes
    var icon: Int? = null
        set(value) {
            field = value
            refresh()
        }

    var iconSizeRational: Float = 1.0f
        set(value) {
            field = value
            refresh()
        }

    var text: String? = null
        set(value) {
            field = value
            refresh()
        }

    var textSizeInSp: Int = 18
        set(value) {
            field = value
            refresh()
        }

    var textAreaSizeRational: Float = 1.0f
        set(value) {
            field = value
            refresh()
        }

    var typeface: Typeface? = null
        set(value) {
            field = value
            refresh()
        }

    private val textPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var textAreaWidth: Int
    private var textAreaHeight: Int
    private var bitmapInstance: Bitmap? = null

    init {
        textPaint.color = Color.WHITE
        textPaint.textAlign = Paint.Align.LEFT

        textAreaWidth = dp2Px(35f).toInt()
        textAreaHeight = dp2Px(35f).toInt()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        refresh()
    }

    private fun refresh() {
        textPaint.textSize = dp2Px(textSizeInSp.toFloat())
        textPaint.typeface = typeface

        textAreaWidth = (dp2Px(35f) / textAreaSizeRational).toInt()
        textAreaHeight = (dp2Px(35f) / textAreaSizeRational).toInt()

        val newBitmap = generateBitmap()
        setImageBitmap(newBitmap)

        recycleBitmap(bitmapInstance)
        bitmapInstance = newBitmap
    }

    private fun getActualButtonType(): ButtonType =
        if (ButtonType.Auto == buttonType) {
            text?.let {
                ButtonType.Text
            } ?: ButtonType.Icon
        } else {
            buttonType
        }

    private fun recycleBitmap(bitmap: Bitmap?) {
        bitmap?.apply {
            if (!isRecycled) {
                recycle()
            }
        }
    }

    private fun generateBitmap(): Bitmap? =
        if (getActualButtonType() == ButtonType.Icon) {
            icon?.let { drawableAsBitmap(it) }
        } else {
            text?.let { textAsBitmap(it) }
        }

    private fun textAsBitmap(text: String): Bitmap? {
        val baseline: Float = -textPaint.ascent()
        val textWidth = (textPaint.measureText(text) + 0.0f).toInt()
        val textHeight = (baseline + textPaint.descent() + 0.0f).toInt()
        val bitmap = Bitmap.createBitmap(textAreaWidth, textAreaHeight, Bitmap.Config.ARGB_8888)
        return bitmap?.apply {
            val canvas = Canvas(this)
            val x = (textAreaWidth - textWidth * TEXT_AREA_COMPENSATION_COEFFICIENT) / 2f
            val y = (textAreaHeight - textHeight * TEXT_AREA_COMPENSATION_COEFFICIENT) / 2f
            canvas.drawText(text, x, baseline + y * TEXT_AREA_COMPENSATION_COEFFICIENT, textPaint)
        }
    }

    private fun drawableAsBitmap(drawableId: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(context, drawableId)
        return when (drawable) {
            is VectorDrawable -> vectorDrawableAsBitmap(drawable)
            else -> null
        }
    }

    private fun vectorDrawableAsBitmap(drawable: Drawable): Bitmap? {
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        val left = (canvas.width * (1 - DEFAULT_ICON_SHRINK_RATIO * iconSizeRational)).toInt()
        val top = (canvas.height * (1 - DEFAULT_ICON_SHRINK_RATIO * iconSizeRational)).toInt()
        val right = canvas.width - left
        val bottom = canvas.height - top
        drawable.setBounds(left, top, right, bottom)
        drawable.draw(canvas)
        return bitmap
    }

    private fun dp2Px(dp: Float, r: Resources = resources): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        r.displayMetrics
    )
}