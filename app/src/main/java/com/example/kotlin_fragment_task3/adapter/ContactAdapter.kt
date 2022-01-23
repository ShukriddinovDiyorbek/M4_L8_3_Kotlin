package com.example.kotlin_fragment_task3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_fragment_task3.R
import com.example.kotlin_fragment_task3.model.Contact

class ContactAdapter(
    private val contactList: ArrayList<Contact>,
    private val itemClickListener: ItemClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val contact = contactList[position]

        if (holder is ContactViewHolder) {
            holder.apply {
                name.text = contact.name
                number.text = contact.number
            }
        }
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(contactList[position])
        }
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.text_name_item)
        val number: TextView = view.findViewById(R.id.text_number_item)
    }

    class ItemClickListener(val onClick: (contact: Contact) -> Unit)
}