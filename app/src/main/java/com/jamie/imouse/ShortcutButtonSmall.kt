package com.jamie.imouse
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatImageButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.Socket

class ShortcutButtonSmall(context: Context, attrs: AttributeSet):AppCompatImageButton(context, attrs) {

    init {
        this.setOnClickListener {
            val tag = it.tag
            val command = tag ?: "none"

            GlobalScope.launch(Dispatchers.IO) {
                try{
                    SocketHandler.socket.outputStream.write((command as String).toByteArray())
                }
                catch(e: Exception){

                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val size = if (width > height) height else width
        setMeasuredDimension(size, size/2)
    }
}