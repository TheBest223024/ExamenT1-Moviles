package edu.pe.cibertec.gestortareas.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import edu.pe.cibertec.gestortareas.model.*
import edu.pe.cibertec.gestortareas.viewmodel.CarritoViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    vm: CarritoViewModel,
    onMostrarResumen: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val formato = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-PE"))

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 🧩 ENCABEZADO
            item {
                Text(
                    "LibroMundo - Carrito de Compras",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // 🧩 CAMPOS DE ENTRADA
            item {
                OutlinedTextField(
                    value = vm.titulo,
                    onValueChange = { vm.titulo = it },
                    label = { Text("Título del Libro") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // 🧩 PRECIO Y CANTIDAD
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = vm.precio,
                        onValueChange = { vm.precio = it },
                        label = { Text("Precio Unitario") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = vm.cantidad,
                        onValueChange = { vm.cantidad = it },
                        label = { Text("Cantidad") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 🧩 CATEGORÍAS
            item {
                Text("Categoría", style = MaterialTheme.typography.bodyMedium)

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    CategoriaLibro.entries.forEach { cat ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = vm.categoriaSeleccionada == cat,
                                onClick = { vm.categoriaSeleccionada = cat }
                            )
                            Text(cat.label)
                        }
                    }
                }
            }

            // 🧩 BOTONES DE ACCIÓN
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {
                        vm.agregarLibro()
                        if (vm.mensajeAlerta == null) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Libro agregado ✅")
                            }
                        }
                    }) {
                        Text("Agregar Libro")
                    }

                    OutlinedButton(onClick = { vm.confirmarLimpiar = true }) {
                        Text("Limpiar Carrito")
                    }
                }
            }

            // 🧩 SECCIÓN DE LIBROS
            item {
                Divider()
                Text("Libros en el carrito:", style = MaterialTheme.typography.titleSmall)
            }

            items(vm.carrito) { libro ->
                Card {
                    Column(Modifier.padding(12.dp)) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(libro.titulo, style = MaterialTheme.typography.titleMedium)
                            IconButton(onClick = { vm.libroEliminar = libro }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                            }
                        }
                        Text("Categoría: ${libro.categoria.label}")
                        Text("Precio: ${formato.format(libro.precio)} | Cantidad: ${libro.cantidad}")
                        Text("Subtotal: S/. ${"%.2f".format(libro.calcularSubtotal())}")
                    }
                }
            }

            // 🧩 RESUMEN DE COMPRA
            item {
                val compra = vm.obtenerCompra()
                Column {
                    Text("Subtotal: S/. ${"%.2f".format(compra.subtotal)}")
                    Text(
                        "Descuento (${(compra.descuentoPct * 100).toInt()}%): " +
                                "-S/. ${"%.2f".format(compra.descuentoMonto)}"
                    )
                    Divider()
                    Text(
                        "Total: S/. ${"%.2f".format(compra.totalFinal)}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            // 🧩 BOTÓN FINAL
            item {
                Button(
                    onClick = {
                        if (vm.carrito.isEmpty()) {
                            vm.mensajeAlerta = "Debe agregar al menos un libro"
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Cálculo realizado correctamente ✅")
                            }
                            onMostrarResumen()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Calcular Total")
                }
            }
        }

        // 🧩 ALERTAS
        vm.mensajeAlerta?.let {
            AlertDialog(
                onDismissRequest = { vm.mensajeAlerta = null },
                confirmButton = {
                    TextButton(onClick = { vm.mensajeAlerta = null }) { Text("OK") }
                },
                title = { Text("Validación") },
                text = { Text(it) }
            )
        }

        vm.libroEliminar?.let { libro ->
            AlertDialog(
                onDismissRequest = { vm.libroEliminar = null },
                confirmButton = {
                    TextButton(onClick = {
                        vm.eliminarLibro(libro)
                        vm.libroEliminar = null
                    }) { Text("Eliminar") }
                },
                dismissButton = {
                    TextButton(onClick = { vm.libroEliminar = null }) { Text("Cancelar") }
                },
                title = { Text("Confirmar") },
                text = { Text("¿Eliminar '${libro.titulo}' del carrito?") }
            )
        }

        if (vm.confirmarLimpiar) {
            AlertDialog(
                onDismissRequest = { vm.confirmarLimpiar = false },
                confirmButton = {
                    TextButton(onClick = {
                        vm.limpiarCarrito()
                        vm.confirmarLimpiar = false
                    }) { Text("Sí, limpiar") }
                },
                dismissButton = {
                    TextButton(onClick = { vm.confirmarLimpiar = false }) { Text("Cancelar") }
                },
                title = { Text("Confirmación") },
                text = { Text("¿Está seguro de limpiar el carrito?") }
            )
        }
    }
}