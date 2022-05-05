package kz.shymkent.relaxhouse.removeClientFragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import kz.shymkent.relaxhouse.R
import kz.shymkent.relaxhouse.models.Client

class RemoveClientDialogFragment(var listener : ClickListener, var client : Client) : DialogFragment() {
    private lateinit var alertDialog : AlertDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val alertDialogBuilder = AlertDialog.Builder(activity)

        alertDialogBuilder.setTitle(client.formatCurrentDate(client.checkInDate))
        alertDialogBuilder.setMessage(R.string.do_you_want_remove_client)
        alertDialogBuilder.setPositiveButton(android.R.string.ok) { _, _ ->
            listener.onOkClicked(client)
        }
        alertDialogBuilder.setNegativeButton(android.R.string.cancel) { _, _ ->
            alertDialog.dismiss()
        }
        alertDialog = alertDialogBuilder.create()
        return alertDialog
    }

    interface ClickListener{
        fun onOkClicked(client : Client)
    }
}