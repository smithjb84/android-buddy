package com.jamie.imouse
import java.net.Socket

object SocketHandler {
    @get:Synchronized
    @set:Synchronized
    var socket: Socket = Socket()

}