package edu.pe.cibertec.gestortareas.viewmodel


import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import edu.pe.cibertec.gestortareas.model.*

class CarritoViewModel : ViewModel() {

    // Campos
    var titulo by mutableStateOf("")
    var precio by mutableStateOf("")
    var cantidad by mutableStateOf("")
    var categoriaSeleccionada by mutableStateOf<CategoriaLibro?>(null)

    // Carrito
    var carrito = mutableStateListOf<Libro>()

    // Alertas
    var mensajeAlerta by mutableStateOf<String?>(null)
    var libroEliminar by mutableStateOf<Libro?>(null)
    var confirmarLimpiar by mutableStateOf(false)

    fun agregarLibro() {
        val p = precio.toDoubleOrNull()
        val c = cantidad.toIntOrNull()

        when {
            titulo.isBlank() -> mensajeAlerta = "Debe ingresar el título del libro"
            p == null || p <= 0 -> mensajeAlerta = "El precio debe ser mayor a 0"
            c == null || c <= 0 -> mensajeAlerta = "La cantidad debe ser mayor a 0"
            categoriaSeleccionada == null -> mensajeAlerta = "Debe seleccionar una categoría"
            else -> {
                val nuevo = Libro(titulo.trim(), p, c, categoriaSeleccionada!!)
                carrito.add(nuevo)
                limpiarCampos(mantenerCategoria = true)
            }
        }
    }

    fun limpiarCampos(mantenerCategoria: Boolean = false) {
        titulo = ""
        precio = ""
        cantidad = ""
        if (!mantenerCategoria) categoriaSeleccionada = null
    }

    fun eliminarLibro(libro: Libro) {
        carrito.remove(libro)
    }

    fun limpiarCarrito() {
        carrito.clear()
        limpiarCampos()
    }

    fun obtenerCompra(): CompraLibros = CompraLibros(carrito)
}