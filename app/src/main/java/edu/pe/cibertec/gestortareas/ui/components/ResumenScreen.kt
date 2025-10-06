package edu.pe.cibertec.gestortareas.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.pe.cibertec.gestortareas.viewmodel.CarritoViewModel

@OptIn(ExperimentalMaterial3Api::class) // ✅ Necesario para TopAppBar
@Composable
fun ResumenScreen(
    vm: CarritoViewModel,
    onBack: () -> Unit
) {
    val compra = vm.obtenerCompra()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resumen de Compra") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("⬅ Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Libros totales: ${compra.totalLibros}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Subtotal: S/. ${"%.2f".format(compra.subtotal)}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Descuento: S/. ${"%.2f".format(compra.descuentoMonto)} (${(compra.descuentoPct * 100).toInt()}%)",
                style = MaterialTheme.typography.bodyLarge
            )
            HorizontalDivider()
            Text(
                text = "Total final: S/. ${"%.2f".format(compra.totalFinal)}",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}