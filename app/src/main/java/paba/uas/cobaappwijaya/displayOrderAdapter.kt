package paba.uas.cobaappwijaya

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class displayOrderAdapter(private val orderList: List<OrderItem>) :
    RecyclerView.Adapter<displayOrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val orderItem = orderList[position]
        holder.bind(orderItem)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textNamaBarang: TextView = itemView.findViewById(R.id.tvNamaBarang)
        private val textQtyBarang: TextView = itemView.findViewById(R.id.tvQty)
        private val textHarga: TextView = itemView.findViewById(R.id.tvHargaSatuan)
        private val btnCancel: ImageButton = itemView.findViewById(R.id.btnHapusBarang)

        fun bind(orderItem: OrderItem) {
            textNamaBarang.text = orderItem.namaBarang
            textQtyBarang.text = orderItem.qtyBarang.toString()
            textHarga.text = "Rp. " + orderItem.totalHarga.toString()
            // Assuming you have a click listener for the cancel button
            btnCancel.setOnClickListener {
                // Handle cancel button click
                // You may want to remove the item from the list or perform any other action
                orderList.toMutableList().removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
                Log.d("Remove Order", "HAPUS")
            }
        }
    }
}