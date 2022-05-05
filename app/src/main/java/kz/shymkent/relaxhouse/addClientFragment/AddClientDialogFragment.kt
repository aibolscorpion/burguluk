package kz.shymkent.relaxhouse.addClientFragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import kz.shymkent.relaxhouse.R
import kz.shymkent.relaxhouse.databinding.FragmentAddClientBinding
import kz.shymkent.relaxhouse.mainActivity.MainActivityViewModel
import kz.shymkent.relaxhouse.models.Client
import java.util.*

class AddClientDialogFragment : DialogFragment() {
    val client = Client()
    private lateinit var alertDialog : AlertDialog
    private lateinit var binding : FragmentAddClientBinding
    private val calendar = Calendar.getInstance()
    private lateinit var viewModel : MainActivityViewModel


    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        viewModel = ViewModelProvider(requireActivity())[MainActivityViewModel::class.java]
        val alertDialogBuilder = AlertDialog.Builder(activity)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_add_client, null, false)
        alertDialogBuilder.setView(binding.root)
        alertDialogBuilder.setTitle(getString(R.string.new_client))
        alertDialog = alertDialogBuilder.create()
        binding.client = client
        binding.addClientDialogFragment = this
        return alertDialog
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun showCheckInDatePickerDialog(){
        val datePickerDialog = DatePickerDialog(requireContext())
        datePickerDialog.setOnDateSetListener { _, year, month, dayOfMonth ->
            client.checkInDate = "$dayOfMonth." + (month + 1) + ".$year"
            showCheckInTimePickerDialog()
        }
        datePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun showCheckOutDatePickerDialog(){
        val datePickerDialog = DatePickerDialog(requireContext())
        datePickerDialog.setOnDateSetListener { _, year, month, dayOfMonth ->
            client.checkOutDate = "$dayOfMonth." + (month + 1) + ".$year"
            showCheckOutTimePickerDialog()
        }
        datePickerDialog.show()
    }

    private fun showCheckInTimePickerDialog(){
        val timePickerDialog = TimePickerDialog(context,
            { _, hourOfDay, minute ->
                client.checkInTime = "$hourOfDay:" + String.format("%02d", minute)
                binding.invalidateAll()
            },calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE], true)
        timePickerDialog.show()
    }

    private fun showCheckOutTimePickerDialog(){
        val timePickerDialog = TimePickerDialog(context,
            { _, hourOfDay, minute ->
                client.checkOutTime = "$hourOfDay:" + String.format("%02d", minute)
                binding.invalidateAll()
            },calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE], true)
        timePickerDialog.show()
    }
    fun addClient(client : Client){
        if(client.checkInDate.isNotEmpty() && client.checkInTime.isNotEmpty() && client.checkOutDate.isNotEmpty() && client.checkOutTime.isNotEmpty()
            && client.quantity.isNotEmpty() && client.avans.isNotEmpty() && client.debt.isNotEmpty() && client.phoneNumber.isNotEmpty()
        ) {
            viewModel.addClientToFirebase(client)
            Toast.makeText(context, getString(R.string.new_client_added), Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
        }else{
            Toast.makeText(context, getString(R.string.fill_empty_fields), Toast.LENGTH_SHORT).show()
        }
    }

    fun cancel(){
        alertDialog.dismiss()
    }
}