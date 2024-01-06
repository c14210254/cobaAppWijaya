package paba.uas.cobaappwijaya

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_addPage.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_addPage : Fragment() {
    val db = Firebase.firestore
    private lateinit var firestore: FirebaseFirestore
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var btnAdd : Button
    private lateinit var inputNamaBarang : EditText
    private lateinit var inputHargaBarang : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_page ,container, false)
        inputNamaBarang = view.findViewById(R.id.etNamaBarang)
        inputHargaBarang = view.findViewById(R.id.etHargaBarang)
        btnAdd = view.findViewById(R.id.btnAdd)

        btnAdd.setOnClickListener {
            addProductToFirestore()
        }

        return view
    }

    private fun addProductToFirestore() {
        val namaBarang = inputNamaBarang.text.toString()
        val hargaBarang = inputHargaBarang.text.toString()

        if (namaBarang.isNotEmpty() && hargaBarang.isNotEmpty()) {
            val product = hashMapOf(
                "name" to namaBarang,
                "price" to hargaBarang.toDouble()
            )

            db.collection("products")
                .document(namaBarang)
                .set(product)
                .addOnSuccessListener {
                    Log.d("Firestore", "Document added successfully: $namaBarang")
                    Toast.makeText(context, "Product added successfully", Toast.LENGTH_SHORT).show()
                    inputNamaBarang.text.clear()
                    inputHargaBarang.text.clear()
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error adding document", e)
                    Toast.makeText(context, "Failed to add product", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "Please fill in both fields", Toast.LENGTH_SHORT).show()
        }
    }
    companion object{
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_addPage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_addPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}