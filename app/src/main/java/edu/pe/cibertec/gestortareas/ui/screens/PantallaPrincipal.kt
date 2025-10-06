package edu.pe.cibertec.gestortareas.ui.screens

import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import edu.pe.cibertec.gestortareas.ui.components.LibroMundoApp

@Composable
fun PantallaPrincipal() {
    MaterialTheme {
        Surface {
            LibroMundoApp()
        }
    }
}