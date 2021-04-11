package com.jamie.imouse
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_connection.*
import kotlinx.coroutines.*
import java.io.BufferedInputStream
import java.io.DataInputStream
import java.lang.Exception
import java.net.InetSocketAddress
import java.net.Socket


class ConnectionActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connection)

    }

    override fun onResume() {
        // Reset socket on connection screen
        SocketHandler.socket = Socket()

        // populate fields from saved data
        val settings = this.getPreferences(Context.MODE_PRIVATE)
        ipText.setText(settings.getString("ip", ""))
        //portText.setText(settings.getString("port", ""))
        super.onResume()
    }


    fun getConnection(view: View) {

        // save ip and port as persistent data
        val settings = this.getPreferences(Context.MODE_PRIVATE).edit()
        settings.putString("ip", ipText.text.toString())
        //settings.putString("port", portText.text.toString())
        settings.apply()

        // create next activity intent
        val intent = Intent(this, MainActivity::class.java).apply {}

        // try and connect and move to next activity
        GlobalScope.launch(Dispatchers.IO) {
            try {
                // get address
                val address = InetSocketAddress(ipText.text.toString(), 6769)

                SocketHandler.socket.connect(address, 1000)
                SocketHandler.socket.outputStream.write(("hello" as String).toByteArray())
                SocketHandler.socket.soTimeout = 1000
                val input = DataInputStream(BufferedInputStream(SocketHandler.socket.getInputStream()))
                val bytes = ByteArray(1024)
                input.read(bytes)
                val msg = bytes.toString(Charsets.UTF_8).replace("\u0000", "")
                if (msg == "hello"){
                    withContext(Dispatchers.Main){
                        SocketHandler.socket.soTimeout = 0
                        startActivity(intent)
                    }
                }
                else{
                    SocketHandler.socket.close()
                    SocketHandler.socket = Socket()
                }
            }
            catch(e:Exception) {
                SocketHandler.socket.close()
                SocketHandler.socket = Socket()
            }
        }
    }

}

