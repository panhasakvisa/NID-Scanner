package com.panhasakvisa.nidscanner.views.dialogues

import android.app.DatePickerDialog
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.panhasakvisa.nidscanner.R
import com.panhasakvisa.nidscanner.constants.Constant
import com.panhasakvisa.nidscanner.helpers.DateHelper
import com.panhasakvisa.nidscanner.models.DocumentType
import com.panhasakvisa.nidscanner.viewmodels.LoginscreenViewModel
import kotlinx.android.synthetic.main.fragment_scan_result_dialog.*
import java.util.*
import kotlin.collections.ArrayList

class ScanResultDialogFragment : BottomSheetDialogFragment() {
    private val loginScreenViewModel:LoginscreenViewModel by activityViewModels()

    private var mYear:Int? = null
    private var mMonth:Int? = null
    private var mDay:Int? = null

    private var spinnerAdapter:ArrayAdapter<String>? = null

    companion object {
        fun newInstance(): ScanResultDialogFragment =
            ScanResultDialogFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_scan_result_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initData()

        editTextDate.setOnClickListener {
            showDatePickerDialog()
        }

        buttonBack.setOnClickListener {
            dismiss()
        }
    }

    private fun initData(){
        initSpinner()
        val scanResult = loginScreenViewModel.scanResult
        if(scanResult != null){
            editTextIDNumber.setText(scanResult.documentNumber)
            editTextDate.setText(DateHelper().formatDate(scanResult.dateOfExpiry.day,scanResult.dateOfExpiry.month,scanResult.dateOfExpiry.year))
            spinnerAdapter?.let{
                spinnerAdapter?.getPosition(Constant.documentTypeMap[scanResult.documentType.toString()])?.let {
                    spinner.setSelection(it)
                }
            }
        }
    }

    private fun initSpinner(){
        val documentTypes = ArrayList<String>()
        DocumentType.values().forEach { documentType: DocumentType ->
            if(Constant.documentTypeMap[documentType.toString()] != null) {
                documentTypes.add(Constant.documentTypeMap[documentType.toString()]!!)
            }
        }
        spinnerAdapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item,documentTypes)
        spinner.adapter = spinnerAdapter
    }

    private fun showDatePickerDialog(){
        // Get Current Date
        val c = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                editTextDate.setText(DateHelper().formatDate(dayOfMonth,month+1,year)) }
            ,mYear!!
            ,mMonth!!
            ,mDay!!
        ).show()
    }
}