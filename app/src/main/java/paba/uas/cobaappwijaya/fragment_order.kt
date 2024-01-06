package paba.uas.cobaappwijaya

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_order.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_order : Fragment() {
    private lateinit var calendar: Calendar
    private lateinit var today: Date
    private lateinit var pindahAddPage : Button
    private lateinit var inputNama : EditText
    private lateinit var inputTanggal : TextView
    private lateinit var inputNamaBarang : EditText
    private lateinit var inputQtyBarang: EditText
    private lateinit var tambahBarang : Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var orderAdapter: displayOrderAdapter
    private val orderList = mutableListOf<OrderItem>()
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }


    }
    private fun navigateToEditProfile() {
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val toAddPage = fragment_addPage()

        //buat masukin frgmentny
        fragmentTransaction.replace(R.id.fragmentContainerView, toAddPage)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
    private fun showDatePicker() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // menggunakan alart dialog untuk memunculkan date dialog
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                updateDateInView()
            },
            year, month, day
        )
        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        datePickerDialog.show()
    }

    private fun updateDateInView() {
        val myFormat = "dd-MM-yyyy" // sama dengan format date di home
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        inputTanggal.text = sdf.format(calendar.time)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_order ,container, false)
        //DATE
        calendar = Calendar.getInstance()
        today = Date()
        // Membuat objek SimpleDateFormat
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        // Memformat tanggal menjadi string
        val formattedDate = dateFormat.format(today)
        inputTanggal = view.findViewById(R.id.etTanggalOrder)
        inputNamaBarang = view.findViewById(R.id.etNamaBarang)
        inputQtyBarang = view.findViewById(R.id.etQty)
        tambahBarang = view.findViewById(R.id.btnTambahBarang)
        recyclerView = view.findViewById(R.id.rvDisplayOrder)

        // Set up RecyclerView and adapter
        orderAdapter = displayOrderAdapter(orderList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = orderAdapter

        tambahBarang.setOnClickListener {
            addOrderItem()
        }
        inputTanggal = view.findViewById(R.id.etTanggalOrder)
        inputTanggal.setOnClickListener {
            showDatePicker()
            Log.d("DatePicker", "MASOK")
        }
//        pindahAddPage = view.findViewById(R.id.btnAddBarang)
//
//        pindahAddPage.setOnClickListener {
//            navigateToEditProfile()
//        }
        return view
    }
    private fun addOrderItem() {
        val namaBarang = inputNamaBarang.text.toString()
        val qtyBarangString = inputQtyBarang.text.toString()

        if (namaBarang.isNotEmpty() && qtyBarangString.isNotEmpty()) {
            try {
                val qtyBarang = qtyBarangString.toInt()
                if (qtyBarang > 0) {
                    val newOrderItem = OrderItem(namaBarang, qtyBarang)
                    orderList.add(newOrderItem)
                    orderAdapter.notifyDataSetChanged()
                    clearInputFields()
                } else {
                    // Handle the case where qtyBarang is not a positive integer
                }
            } catch (e: NumberFormatException) {
                // Handle the case where qtyBarang is not a valid integer
            }
        } else {
            // Handle the case where namaBarang or qtyBarang is empty
        }
    }

    private fun clearInputFields() {
        inputNamaBarang.text.clear()
        inputQtyBarang.text.clear()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_order.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_order().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}