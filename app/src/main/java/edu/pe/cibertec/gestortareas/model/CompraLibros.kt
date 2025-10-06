package edu.pe.cibertec.gestortareas.model

data class CompraLibros(val items: List<Libro>) {
    val totalLibros = items.sumOf { it.cantidad }
    val subtotal = items.sumOf { it.calcularSubtotal() }
    val descuentoPct: Double = when (totalLibros) {
        in 1..2 -> 0.0
        in 3..5 -> 0.10
        in 6..10 -> 0.15
        else -> if (totalLibros > 10) 0.20 else 0.0
    }
    val descuentoMonto = subtotal * descuentoPct
    val totalFinal = subtotal - descuentoMonto
}