package fermat.kotlin.notepad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import fermat.kotlin.notepad.modele.Note

class NoteAdapter(var notes: List<Note>, var itemClickListener: OnItemClickListener): RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: NoteListActivity) {
        itemClickListener = listener
    }

    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        val cardView: CardView = itemView.findViewById<CardView>(R.id.card_view)
        val titleView: TextView = itemView.findViewById<TextView>(R.id.title)
        val excerptView: TextView = itemView.findViewById<TextView>(R.id.excerpt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent,false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]

        holder.titleView.text = note.titre
        holder.excerptView.text = note.text
        holder.cardView.setOnClickListener{
            if (position != RecyclerView.NO_POSITION) {
                itemClickListener.onItemClick(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }
}