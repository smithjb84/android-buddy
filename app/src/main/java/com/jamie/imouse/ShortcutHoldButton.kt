package com.jamie.imouse
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.Socket

class ShortcutHoldButton(context: Context, attrs: AttributeSet):AppCompatImageButton(context, attrs) {

    init {
        setOnTouchListener(View.OnTouchListener { v, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    val tag = v.tag
                    val command = tag ?: "none"
                    v.isPressed = true

                    GlobalScope.launch(Dispatchers.IO) {
                        try{
                            SocketHandler.socket.outputStream.write((command as String).toByteArray())
                        }
                        catch(e: Exception){

                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    v.isPressed = false
                    val tag = v.tag
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

            return@OnTouchListener true
        })
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val size = if (width > height) height else width
        setMeasuredDimension(size, size)
    }
}