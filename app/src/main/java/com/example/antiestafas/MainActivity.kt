package com.example.antiestafas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.example.antiestafas.ui.screens.ContenedorPrincipalApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                // Tu contenedor limpio que llama al Grafo de Navegación
                ContenedorPrincipalApp()
            }
        }
    }
}