package com.mandarsadye.mandar.ess_user.CustomClasses

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class DrawView : View {
    internal var paint = Paint()

    private fun init() {
        paint.color = Color.BLACK
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    public override fun onDraw(canvas: Canvas) {
        canvas.drawLine(0f, 0f, 20f, 20f, paint)
        canvas.drawLine(20f, 0f, 0f, 20f, paint)
    }

}