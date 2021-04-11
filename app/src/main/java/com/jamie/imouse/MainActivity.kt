package com.jamie.imouse
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.DataInputStream


class MainActivity : AppCompatActivity() {

    fun listenForMessages(){
        GlobalScope.launch(Dispatchers.IO) {
            while (true) {
                try{
                    val input = DataInputStream(BufferedInputStream(SocketHandler.socket.getInputStream()))
                    val bytes = ByteArray(1024)
                    input.read(bytes)
                    val msg = bytes.toString(Charsets.UTF_8).replace("\u0000", "")
                    updateUI(msg)
                }
                catch(e:Exception){
                    break
                }
            }
            SocketHandler.socket.close()
            super.finish()
        }
    }

    suspend fun updateUI(msg:String) {
            withContext(Dispatchers.Main){
                when (msg) {
                    "Inventor" -> {
                        group.removeAllViews()
                        layoutInflater.inflate(R.layout.no_document, group, true)
                        docTitle.text = "No Document"
                        cornerIcon.setImageResource(R.drawable.ic_inventor)
                        textView5.text = ""
                        textView9.text = ""
                        textView6.text = ""
                        textView8.text = ""
                    }
                    "2D Sketch" -> {
                        group.removeAllViews()
                        layoutInflater.inflate(R.layout.sketch_tools, group, true)
                        docTitle.text = "Part Document"
                        cornerIcon.setImageResource(R.drawable.ic_part)
                    }
                    "Part" -> {
                        group.removeAllViews()
                        layoutInflater.inflate(R.layout.part_tools, group, true)
                        docTitle.text = "Part Document"
                        cornerIcon.setImageResource(R.drawable.ic_part)
                    }
                    "Drawing" -> {
                        group.removeAllViews()
                        layoutInflater.inflate(R.layout.drawing_tools, group, true)
                        docTitle.text = "Drawing Document"
                        cornerIcon.setImageResource(R.drawable.ic_drawing)
                    }
                    "Assembly" -> {
                        group.removeAllViews()
                        layoutInflater.inflate(R.layout.assembly_tools, group, true)
                        docTitle.text = "Assembly Document"
                        cornerIcon.setImageResource(R.drawable.ic_assembly)
                    }
                    "Presentation" -> {
                        group.removeAllViews()
                        layoutInflater.inflate(R.layout.presentation_tools, group, true)
                        docTitle.text = "Presentation Document"
                        cornerIcon.setImageResource(R.drawable.ic_pres)
                    }
                    else -> {
                        Log.e("msg", msg)
                        val jsonmsg = JSONObject(msg)
                        textView5.text = jsonmsg.getString("fileName").replace("@inch@", "\"")
                        textView9.text = jsonmsg.getString("title").replace("@inch@", "\"")
                        textView6.text = jsonmsg.getString("partNumber").replace("@inch@", "\"")
                        textView8.text = jsonmsg.getString("description").replace("@inch@", "\"")

                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        listenForMessages()
        GlobalScope.launch(Dispatchers.IO) {
            try{
                SocketHandler.socket.outputStream.write(("INITIAL_SETUP" as String).toByteArray())
            }
            catch(e: java.lang.Exception){

            }
        }
    }

    override fun onBackPressed() {
        SocketHandler.socket.close()
        super.finish()
        super.onBackPressed()
    }

}
