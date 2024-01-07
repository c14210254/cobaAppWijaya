package paba.uas.cobaappwijaya

data class OrderItem(
    val namaPemesan: String,
    val namaBarang: String,
    val qtyBarang: Int,
    val totalHarga: Double,
    val orderDate: String// Assuming you want to include the price in the order item
) {
    companion object {
        const val DATE_FORMAT = "dd-MM-yyyy"
    }
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "namaPemesan" to namaPemesan,
            "namaBarang" to namaBarang,
            "qtyBarang" to qtyBarang,
            "totalHarga" to totalHarga,
            "orderDate" to orderDate
        )
    }
}

