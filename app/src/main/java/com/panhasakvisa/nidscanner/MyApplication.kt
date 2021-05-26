package com.panhasakvisa.nidscanner

import android.app.Application
import com.microblink.MicroblinkSDK

class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        MicroblinkSDK.setLicenseKey("sRwAAAAbY29tLnBhbmhhc2FrdmlzYS5uaWRzY2FubmVyOyjgo+kHTCfZyjZDTVr4YCOlOPh2KlMBuEFImCnpHn5SVJYVLDJJj5x056EiGro9dez3XrRJYcpziNLsoyAT8vyIYCc7DKXhDwahLjPCFJnLioSwt8XIKydV+4oCw499UlggfFmA1Y/9khQjIlqXpPWqsiz4MMWDFUfgYSKNRU7AsFodEMjVJ4TE31eOUDU+koNjR8t16riF6umYau0ZZ4mgSQuDehIkqsksGx2q",this)
    }
}