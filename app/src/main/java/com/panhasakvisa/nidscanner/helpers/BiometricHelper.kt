package com.panhasakvisa.nidscanner.helpers

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.microblink.entities.recognizers.RecognizerBundle
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer
import java.util.concurrent.Executor

@Suppress("DEPRECATION")
class BiometricHelper(val context:Context, val biometricResultListener: OnBiometricResultListener) {
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    var mRecognizer: BlinkIdCombinedRecognizer? = null
        private set

    var mRecognizerBundle: RecognizerBundle? = null
        private set

    private var REQUEST_CODE_BIOMETRIC = 1001

    init {
        initBiometric()
    }

    private fun initBiometric(){
        executor = ContextCompat.getMainExecutor(context)
        biometricPrompt = BiometricPrompt(context as FragmentActivity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    biometricResultListener.onAuthenticationError(errorCode,errString)
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    biometricResultListener.onAuthenticationSucceeded(result)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    biometricResultListener.onAuthenticationFailed()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()

        // create BlinkIdCombinedRecognizer
        mRecognizer = BlinkIdCombinedRecognizer()

        // bundle recognizers into RecognizerBundle
        mRecognizerBundle = RecognizerBundle(mRecognizer)
    }

    fun showBiometricPrompt() {
        if(checkBiometricAuthenticationAvailable())
            biometricPrompt.authenticate(promptInfo)
    }

    private fun checkBiometricAuthenticationAvailable():Boolean{
        val biometricManager = BiometricManager.from(context)
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
                return true
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.e("MY_APP_TAG", "No biometric features available on this device.")
                return false
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")
                return false
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Prompts the user to create credentials that your app accepts.
                // Only support Android 11
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                    val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                        putExtra(
                            Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                            android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG or android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
                        )
                    }
                    (context as AppCompatActivity).startActivityForResult(enrollIntent, REQUEST_CODE_BIOMETRIC)
                }
            }
        }
        return false
    }
}

interface OnBiometricResultListener{
    fun onAuthenticationError(errorCode: Int,
                              errString: CharSequence)
    fun onAuthenticationSucceeded(
        result: BiometricPrompt.AuthenticationResult)
    fun onAuthenticationFailed()
}