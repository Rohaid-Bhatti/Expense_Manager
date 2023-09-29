package com.hzm.expensemanager

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import com.hzm.expensemanager.adapters.FragmentAdapter
import com.hzm.expensemanager.databinding.ActivityMainBinding
import com.hzm.expensemanager.respository.ExpenseRepository
import com.hzm.expensemanager.roomDB.ExpensesDatabase
import com.hzm.expensemanager.roomDB.ExpensesEntity
import com.hzm.expensemanager.viewModels.ExpenseViewModel
import com.hzm.expensemanager.viewModels.ExpenseViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var spinnerCategory: Spinner
    private lateinit var etAmount: EditText
    private lateinit var etReason: EditText
    private lateinit var etDescription: EditText
    lateinit var expenseViewModel: ExpenseViewModel
    private var selectedDocumentUri: Uri? = null
    private lateinit var textDocumentName: TextView
    private lateinit var textDate: TextView

    companion object {
        private const val PICK_DOCUMENT_REQUEST_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dao = ExpensesDatabase.getDatabase(applicationContext).expensesDao()
        val repository = ExpenseRepository(dao)

        expenseViewModel = ViewModelProvider(
            this,
            ExpenseViewModelFactory(repository)
        ).get(ExpenseViewModel::class.java)

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        viewPager.adapter = FragmentAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            // Customize tab text as needed
            when (position) {
                0 -> tab.text = "All"
                1 -> tab.text = "Expenses"
                2 -> tab.text = "Income"
            }
        }.attach()

        binding.fab.setOnClickListener {
            showExpenseBottomSheetDialog()
        }
    }

    private fun showExpenseBottomSheetDialog() {
        // on below line we are creating a new bottom sheet dialog.
        val dialog = BottomSheetDialog(this)

        // on below line we are inflating a layout file which we have created.
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog_layout, null)
        spinnerCategory = view.findViewById(R.id.spinnerCategory)

        // Initialize the Spinner with values
        val sizeOptions = arrayOf("Expense", "Income")
        val sizeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sizeOptions)
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = sizeAdapter

        val btnClose = view.findViewById<Button>(R.id.buttonCancel)
        val btnSubmit = view.findViewById<Button>(R.id.buttonSubmit)

        val btnDatePicker = view.findViewById<Button>(R.id.btnDatePicker)
        textDate = view.findViewById(R.id.textDate)

        etAmount = view.findViewById(R.id.etAmount)
        etReason = view.findViewById(R.id.etReason)
        etDescription = view.findViewById(R.id.etDescription)

        val btnAttachDocument = view.findViewById<Button>(R.id.btnAttachDocument)
        textDocumentName = view.findViewById(R.id.textDocumentName)

        btnAttachDocument.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*" // Specify the MIME type(s) of files you want to allow
            startActivityForResult(intent, PICK_DOCUMENT_REQUEST_CODE)
        }

        // Initialize a Calendar instance to hold the selected date.
        val selectedDate = Calendar.getInstance()

        // Set an OnClickListener for the date picker button.
        btnDatePicker.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    // Update the selectedDate with the chosen date.
                    selectedDate.set(year, month, dayOfMonth)

                    // Format the selected date as Date (not String).
                    val formattedDate = selectedDate.time

                    // Format the selected date as a string for display.
                    val formattedDateString =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(formattedDate)

                    // Set the selected date to the textDate TextView.
                    textDate.text = "Date: $formattedDateString"
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.show()
        }

        btnSubmit.setOnClickListener {
            val amount = etAmount.text.toString()
            val reason = etReason.text.toString()
            val desc = etDescription.text.toString()
            val spinnerCategory = spinnerCategory.selectedItem.toString()

            val sharedPreference = getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)
            val userEmail = sharedPreference.getString("USER_EMAIL", "")

            // Get the selected date from the Calendar instance.
            val formattedDate = selectedDate.time

            // Get the selected document's name
            val documentName = selectedDocumentUri?.let { uri ->
                val cursor = contentResolver.query(uri, null, null, null, null)
                cursor?.use {
                    if (it.moveToFirst()) {
                        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        it.getString(nameIndex)
                    } else {
                        ""
                    }
                } ?: ""
            }

            // Check if any of the fields are empty before inserting data.
            if (amount.isNotEmpty() && reason.isNotEmpty() && desc.isNotEmpty()) {
                val data = ExpensesEntity(
                    0,
                    amount,
                    spinnerCategory,
                    reason,
                    desc,
                    formattedDate,
                    documentName ?: "",
                    userEmail!!
                )
                expenseViewModel.insertData(data)
                // Dismiss the dialog.
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        // below line is use to set cancelable to avoid
        // closing of dialog box when clicking on the screen.
        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_DOCUMENT_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.data != null) {
                selectedDocumentUri = data.data

                // Display the selected document's name
                val cursor = contentResolver.query(selectedDocumentUri!!, null, null, null, null)
                cursor?.use {
                    if (it.moveToFirst()) {
                        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        val documentName = it.getString(nameIndex)
                        textDocumentName.text = documentName
                        textDocumentName.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}