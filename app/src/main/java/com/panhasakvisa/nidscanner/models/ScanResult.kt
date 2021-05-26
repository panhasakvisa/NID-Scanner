package com.panhasakvisa.nidscanner.models

import com.microblink.results.date.Date
import java.io.Serializable
import java.util.*

data class ScanResult(val documentType: DocumentType, val documentNumber: String, val dateOfExpiry: Date):Serializable