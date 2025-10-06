package edu.pe.cibertec.gestortareas.model

data class Libro(
    val titulo: String,
    val precio: Double,
    val cantidad: Int,
    val categoria: CategoriaLibro
) {
    fun calcularSubtotal(): Double = precio * cantidad
}