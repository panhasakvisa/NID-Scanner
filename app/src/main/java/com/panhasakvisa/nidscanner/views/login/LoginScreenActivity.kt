package com.panhasakvisa.nidscanner.views.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import com.microblink.entities.recognizers.Recognizer
import com.microblink.uisettings.ActivityRunner
import com.microblink.uisettings.BlinkIdUISettings
import com.panhasakvisa.nidscanner.R
import com.panhasakvisa.nidscanner.helpers.BiometricHelper
import com.panhasakvisa.nidscanner.helpers.OnBiometricResultListener
import com.panhasakvisa.nidscanner.models.DocumentType
import com.panhasakvisa.nidscanner.models.ScanResult
import com.panhasakvisa.nidscanner.viewmodels.LoginscreenViewModel
import com.panhasakvisa.nidscanner.views.dialogues.ScanResultDialogFragment


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class LoginScreenActivity : AppCompatActivity(),OnBiometricResultListener {
    private val loginScreenViewModel:LoginscreenViewModel by viewModels()

    private var biometricHelper: BiometricHelper? = null

    private var REQUEST_CODE_SCAN_MRZ = 1002

    private var SCAN_RESULT_DIALOG = "SCAN_RESULT_DIALOG"

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_loginscreen)

        biometricHelper = BiometricHelper(this,this)
    }

    fun showBiometricPrompt(view: View) {
        biometricHelper?.showBiometricPrompt()
    }

    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
        Toast.makeText(applicationContext,
            "Authentication error: $errString", Toast.LENGTH_SHORT)
            .show()
    }

    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        Toast.makeText(applicationContext,
            "Authentication succeeded!", Toast.LENGTH_SHORT)
            .show()
        openMRZScanner()
    }

    override fun onAuthenticationFailed() {
        Toast.makeText(applicationContext, "Authentication failed",
            Toast.LENGTH_SHORT)
            .show()
    }

    private fun openMRZScanner(){
        // Settings for BlinkIdActivity
        val settings = BlinkIdUISettings(biometricHelper?.mRecognizerBundle)
        // Start activity
        ActivityRunner.startActivityForResult(this, REQUEST_CODE_SCAN_MRZ, settings)
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SCAN_MRZ && resultCode == Activity.RESULT_OK && data != null) {
            // load the data into all recognizers bundled within your RecognizerBundle
            biometricHelper?.mRecognizerBundle!!.loadFromIntent(data)

            // now every recognizer object that was bundled within RecognizerBundle
            // has been updated with results obtained during scanning session

            // you can get the result by invoking getResult on recognizer
            val result = biometricHelper?.mRecognizer!!.result
            if (result.resultState == Recognizer.Result.State.Valid) {
                val scanResult = ScanResult(
                    documentType = DocumentType.valueOf(result.mrzResult.documentType.name),
                    documentNumber = result.mrzResult.documentNumber,
                    dateOfExpiry = result.mrzResult.dateOfExpiry.date!!
                )

                loginScreenViewModel.scanResult = scanResult

                ScanResultDialogFragment.newInstance().show(supportFragmentManager,SCAN_RESULT_DIALOG)
            }
        }
    }
}