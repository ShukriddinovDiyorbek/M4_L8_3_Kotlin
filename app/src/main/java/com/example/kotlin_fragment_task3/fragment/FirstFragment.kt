package com.example.kotlin_fragment_task3.fragment

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.kotlin_fragment_task3.R
import com.example.kotlin_fragment_task3.adapter.ContactAdapter
import com.example.kotlin_fragment_task3.model.Contact
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class FirstFragment : Fragment() {

    private val arraySearch = ArrayList<String>()
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var contactList = ArrayList<Contact>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_first, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerview)
        shimmerLayout = view.findViewById(R.id.shimmer_layout)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh)
        autoCompleteTextView = view.findViewById(R.id.auto_complete_text)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val addButton = view.findViewById<FloatingActionButton>(R.id.btn_add_contact)

        addButton.setOnClickListener {
            bottomSheetThird()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            shimmerLayout.visibility = View.GONE
            shimmerLayout.stopShimmer()
            recyclerView.visibility = View.VISIBLE
        }, 2000)

        swipeRefreshLayout.setOnRefreshListener {
            shimmerLayout.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                shimmerLayout.visibility = View.GONE
                shimmerLayout.stopShimmer()
                recyclerView.visibility = View.VISIBLE
            }, 2000)
            shimmerLayout.startShimmer()
            swipeRefreshLayout.isRefreshing = false
        }
        val contacts =
            activity?.contentResolver!!.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null
            )
        while (contacts!!.moveToNext()) {
            val name =
                contacts.let {
                    contacts.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                }
            val number =
                contacts.let { contacts.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)) }

            contactList.add(Contact(name, number))
            arraySearch.add(name)
        }
        val arrayAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_expandable_list_item_1,
            arraySearch
        )
        autoCompleteTextView.setAdapter(arrayAdapter)
        contactAdapter = ContactAdapter(contactList, ContactAdapter.ItemClickListener {
            bottomSheet(it.name, it.number)
        })
        recyclerView.adapter = contactAdapter
        contacts.close()
    }

    private fun bottomSheet(name: String, number: String) {
        val random = (0..4).random()
        val view: View = layoutInflater.inflate(R.layout.fragment_second, null)
        val dialog = activity?.let { BottomSheetDialog(it) }
        dialog!!.setContentView(view)
        val nameText = dialog.findViewById<TextView>(R.id.text_name)
        val numberText = dialog.findViewById<TextView>(R.id.text_number)
        val btnCall = dialog.findViewById<ImageButton>(R.id.btn_call)
        val btnSms = dialog.findViewById<ImageButton>(R.id.btn_message)
        val imageView = dialog.findViewById<ImageView>(R.id.image_bottom_sheet)

        nameText!!.text = name
        numberText!!.text = number
        dialog.show()
        btnCall!!.setOnClickListener {
            intentCall(number)
        }
        btnSms!!.setOnClickListener {
            intentMessage(name)
        }
        imageView!!.setBackgroundColor(
            when (random) {
                0 -> Color.BLUE
                1 -> Color.RED
                2 -> Color.GREEN
                3 -> Color.CYAN
                else -> Color.DKGRAY
            }
        )
    }

    private fun intentCall(number: String) {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
        startActivity(intent)
    }

    private fun intentMessage(name: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:$name"))
        startActivity(intent)
    }

    private fun bottomSheetThird() {
        val view = layoutInflater.inflate(R.layout.fragment_third, null)
        val dialog = activity?.let { BottomSheetDialog(it) }
        dialog!!.setContentView(view)
        dialog.show()
        val editName = dialog.findViewById<TextInputEditText>(R.id.edit_name_third)
        val editNumber = dialog.findViewById<TextInputEditText>(R.id.edit_number_third)
        val saveBtn = dialog.findViewById<MaterialButton>(R.id.save_btn_third)
        saveBtn!!.setOnClickListener {
            if (editName!!.text!!.isEmpty()) {
                editName.error = getString(R.string.plz_name)
            }
            if (editNumber!!.text!!.isEmpty()) {
                editNumber.error = getString(R.string.plz_number)
            } else {
                val name = editName.text.toString().trim()
                val number = editNumber.text.toString().trim()
                if (name.length < 2 || number.length < 9) {
                    Toast.makeText(
                        activity,
                        "Please enter the name and number correctly!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    contactList.add(Contact(name, number))
                    arraySearch.add(name)
                    Toast.makeText(activity, "Storage completed successfully", Toast.LENGTH_SHORT)
                        .show()
                    dialog.dismissWithAnimation = true
                    dialog.dismiss()
                }
            }
        }
    }
}
