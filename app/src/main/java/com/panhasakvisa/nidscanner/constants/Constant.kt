package com.panhasakvisa.nidscanner.constants

import com.panhasakvisa.nidscanner.models.DocumentType

class Constant{
    companion object{
        val documentTypeMap = hashMapOf<String,String>(
            DocumentType.MRTD_TYPE_IDENTITY_CARD.toString() to "Khmer ID",
            DocumentType.MRTD_TYPE_PASSPORT.toString() to "Passport"
        )
    }
}