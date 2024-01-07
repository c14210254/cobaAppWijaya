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
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
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
class SharedViewModel : ViewModel() {
    var hargaBarang: String? = null
}
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
    private val db = FirebaseFirestore.getInstance()
    private lateinit var totalHarga : TextView
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var hargaBarang: String? = ""

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
        inputNama = view.findViewById(R.id.etNamaCustomer)
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
        pindahAddPage = view.findViewById(R.id.btnAddBarang)

        pindahAddPage.setOnClickListener {
            navigateToEditProfile()
        }
        return view
    }
    private fun addOrderItem() {

        val namaPemesan = inputNama.text.toString()
        val namaBarang = inputNamaBarang.text.toString()
        val qtyBarangString = inputQtyBarang.text.toString()
        val tanggalPemesanan = inputTanggal.text.toString()

        if (namaPemesan.isNotEmpty() && namaBarang.isNotEmpty() && qtyBarangString.isNotEmpty()) {
            try {
                val qtyBarang = qtyBarangString.toInt()
                if (qtyBarang > 0) {
                    // Retrieve price from Firebase and calculate total price
                    fetchPriceAndAddToOrder(namaPemesan, namaBarang, qtyBarang, tanggalPemesanan)
                } else {
                    showToast("Quantity must be greater than 0")
                }
            } catch (e: NumberFormatException) {
                showToast("Invalid quantity format")
            }
        } else {
            showToast("Please fill in all fields")
        }
    }
    private fun fetchPriceAndAddToOrder(namaPemesan: String, namaBarang: String, qtyBarang: Int, tanggalPemesanan: String) {
        // Define the document reference for the product in Firestore (assuming 'products' is the collection)
        val productRef = db.collection("products").document(namaBarang)

        productRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val hargaBarang = document.getDouble("price") ?: 0.0

                    // Calculate total price
                    val totalHarga = hargaBarang * qtyBarang

                    // Adding order to Firebase
                    addOrderToFirebase(namaPemesan, namaBarang, qtyBarang, totalHarga, tanggalPemesanan)

                    // Adding order to the local list
                    val newOrderItem = OrderItem(namaPemesan, namaBarang, qtyBarang, totalHarga, tanggalPemesanan)
                    orderList.add(newOrderItem)
                    orderAdapter.notifyDataSetChanged()
                    clearInputFields()
                } else {
                    showToast("Product not found in database")
                }
            }
            .addOnFailureListener { e ->
                // Handle errors
                Log.e("Firestore", "Error getting document", e)
            }
    }
    private fun addOrderToFirebase(namaPemesan: String, namaBarang: String, qtyBarang: Int, totalHarga: Double, tanggalPemesanan: String) {
        // Define the document reference for the order in Firestore (assuming 'orders' is the collection)
        val ordersRef = db.collection("orders").document(namaPemesan)

        // Create a map with order details
        val orderData = mapOf(
            "namaPemesan" to namaPemesan,
            "namaBarang" to namaBarang,
            "qtyBarang" to qtyBarang,
            "hargaBarang" to totalHarga,
            "tanggalPemesanan" to tanggalPemesanan
        )

        // Set the order data in Firestore
        ordersRef.set(orderData)
            .addOnSuccessListener {
                Log.d("Firestore", "Order added successfully: $namaPemesan")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error adding order", e)
            }
    }


    private fun clearInputFields() {
        inputNamaBarang.text.clear()
        inputQtyBarang.text.clear()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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