package fermat.kotlin.notepad

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import fermat.kotlin.notepad.modele.Note

class NoteDetail : AppCompatActivity() {
    lateinit var note: Note
    private var noteIndex: Int = -1

    lateinit var titleView: TextView
    lateinit var textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        note = intent.getParcelableExtra<Note>(EXTRA_NOTE)!!
        noteIndex = intent.getIntExtra(EXTRA_NOTE_INDEX, -1)
        titleView = findViewById(R.id.title)
        textView = findViewById(R.id.text)

        titleView.text = note.titre
        textView.text = note.text

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_note_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.save -> {
                saveNote()
                return true
            }
            R.id.delete -> {
                showConfirmDeleteNoteDialog()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showConfirmDeleteNoteDialog() {
        val fragment = note.titre?.let { ConfirmDeleteNoteDialogFragment(it) }

        if (fragment != null) {
            fragment.listener = object: ConfirmDeleteNoteDialogFragment.ConfirmDeleteDialogListener{
                override fun onDialogPositiveClick() {
                    deleteNote()
                }

                override fun onDialogNegativeClick() {

                }
            }
            fragment.show(supportFragmentManager, "OK")
        }

    }

    private fun deleteNote() {
        intent = Intent(ACTION_DELETE_NOTE)
        intent.putExtra(EXTRA_NOTE_INDEX, noteIndex)

        setResult(Activity.RESULT_OK, intent)
        finish()
    }


    private fun saveNote(){
        note.titre = titleView.text.toString()
        note.text = textView.text.toString()

        intent = Intent(ACTION_SAVE_NOTE)
        intent.putExtra(EXTRA_NOTE, note as Parcelable)
        intent.putExtra(EXTRA_NOTE_INDEX, noteIndex)

        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    companion object {
        const val REQUEST_EDIT_NOTE = 1
        const val EXTRA_NOTE = "note"
        const val EXTRA_NOTE_INDEX = "noteIndex"

        const val ACTION_SAVE_NOTE = "fermat.kotlin.notepad.actions.ACTION_SAVE_NOTE"
        const val ACTION_DELETE_NOTE = "fermat.kotlin.notepad.actions.ACTION_DELETE_NOTE"
    }
}