package com.location.main

import com.llj.baselib.IOTViewModel

class MainVM:IOTViewModel() {

    var isTesting = false

    fun startTesting(){
        isTesting = true
        sendOrderToDevice("A")
    }

    fun stopTesting(){
        isTesting = false
        sendOrderToDevice("B")
    }

}