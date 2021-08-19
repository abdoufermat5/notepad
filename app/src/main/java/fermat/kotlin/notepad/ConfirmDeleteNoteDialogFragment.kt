package fermat.kotlin.notepad

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class ConfirmDeleteNoteDialogFragment(val noteTitle: String = ""): DialogFragment() {
    interface ConfirmDeleteDialogListener{
        fun onDialogPositiveClick()
        fun onDialogNegativeClick()
    }

    var listener: ConfirmDeleteDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = context?.let { AlertDialog.Builder(it) }

        if (builder != null) {
            builder.setMessage("Êtes vous sûr de supprimer la note \"$noteTitle\" ?")
                .setPositiveButton("Supprimer"
                ) { _, _ ->
                    listener?.onDialogPositiveClick()
                }
                .setNegativeButton("Annuler"
                ) { _, _ ->
                    listener?.onDialogNegativeClick()
                }
            return builder.create()
        }
        return super.onCreateDialog(savedInstanceState)
    }
}