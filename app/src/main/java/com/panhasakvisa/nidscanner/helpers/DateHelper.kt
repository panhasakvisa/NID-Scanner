package com.panhasakvisa.nidscanner.helpers

class DateHelper{
    fun formatDate(day:Int,month:Int,year:Int):String{
        return "${String.format("%02d",day)}-${String.format("%02d",month)}-${String.format("%02d",year)}"
    }
}