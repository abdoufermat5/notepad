package fermat.kotlin.notepad

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import fermat.kotlin.notepad.NoteDetail.Companion.ACTION_DELETE_NOTE
import fermat.kotlin.notepad.NoteDetail.Companion.ACTION_SAVE_NOTE
import fermat.kotlin.notepad.NoteDetail.Companion.EXTRA_NOTE
import fermat.kotlin.notepad.NoteDetail.Companion.EXTRA_NOTE_INDEX
import fermat.kotlin.notepad.NoteDetail.Companion.REQUEST_EDIT_NOTE
import fermat.kotlin.notepad.modele.Note
import fermat.kotlin.notepad.utils.deleteNotePersisted
import fermat.kotlin.notepad.utils.loadNotes
import fermat.kotlin.notepad.utils.persistNote

class NoteListActivity : AppCompatActivity(), NoteAdapter.OnItemClickListener {
    val TAG = NoteListActivity::class.java.simpleName
    lateinit var adapter: NoteAdapter
    lateinit var notes: MutableList<Note>
    lateinit var coordinatorLayout: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.rv)
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            createNote()
        }

        notes = loadNotes(this)
        adapter = NoteAdapter(notes, this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        coordinatorLayout = findViewById<CoordinatorLayout>(R.id.coordinator_layout)


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode != Activity.RESULT_OK || data == null) {
            return
        }
        when (requestCode) {
            REQUEST_EDIT_NOTE -> processEditNote(data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun processEditNote(data: Intent) {
        val noteIndex = data.getIntExtra(EXTRA_NOTE_INDEX, -1)

        when(data.action){
            ACTION_SAVE_NOTE -> {
                val note: Note? = data.getParcelableExtra(EXTRA_NOTE)
                if (note != null) {
                    saveNote(note, noteIndex)
                }
            }
            ACTION_DELETE_NOTE -> {
                deleteNote(noteIndex)
            }
        }

    }

    private fun createNote() {
        showNoteDetailActivity(-1)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun saveNote(note: Note, index: Int) {
        persistNote(this, note)
        if (index < 0) {
            notes.add(0, note)
            adapter.notifyDataSetChanged()
        } else {
            notes[index] = note
            adapter.notifyDataSetChanged()
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun deleteNote(index: Int){
        if (index < 0) return
        val note = notes.removeAt(index)
        deleteNotePersisted(this, note)
        adapter.notifyDataSetChanged()

        Snackbar.make(coordinatorLayout, "${note.titre} supprimé !", Snackbar.LENGTH_SHORT).show()
    }


    private fun showNoteDetailActivity(pos: Int) {
        val note: Note = if (pos < 0) Note() else notes[pos]
        val intent = Intent(this, NoteDetail::class.java)
        intent.putExtra(EXTRA_NOTE, note as Parcelable)
        intent.putExtra(EXTRA_NOTE_INDEX, pos)
        startActivityForResult(intent, REQUEST_EDIT_NOTE)
    }

    override fun onItemClick(position: Int) {
        showNoteDetailActivity(position)
    }

}