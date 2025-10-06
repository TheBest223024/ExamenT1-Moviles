package edu.pe.cibertec.gestortareas.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.pe.cibertec.gestortareas.viewmodel.CarritoViewModel

@Composable
fun LibroMundoApp() {
    // Controlador de navegación
    val navController = rememberNavController()
    // ViewModel compartido entre pantallas
    val carritoViewModel: CarritoViewModel = viewModel()

    Scaffold { paddingValues ->
        // Sistema de navegación principal
        NavHost(
            navController = navController,
            startDestination = "carrito",
            modifier = Modifier.padding(paddingValues) // ✅ elimina warning de padding
        ) {
            // 📘 Pantalla principal del carrito
            composable(route = "carrito") {
                CarritoScreen(
                    vm = carritoViewModel,
                    onMostrarResumen = {
                        navController.navigate("resumen")
                    }
                )
            }

            // 📗 Pantalla del resumen de compra
            composable(route = "resumen") {
                ResumenScreen(
                    vm = carritoViewModel,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}