package com.example.antiestafas.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.antiestafas.navigation.DestinosNavegacion
import com.example.antiestafas.navigation.GrafoNavegacion

@Composable
fun ContenedorPrincipalApp() {
    val navController = rememberNavController()
    val listaMenu = listOf(
        DestinosNavegacion.Inicio,
        DestinosNavegacion.UrlCheck,
        DestinosNavegacion.WikiEstafas,
        DestinosNavegacion.Detecciones
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF1E293B)) {
                listaMenu.forEach { destino ->
                    NavigationBarItem(
                        selected = rutaActual == destino.ruta,
                        onClick = {
                            if (rutaActual != destino.ruta) {
                                navController.navigate(destino.ruta) {
                                    popUpTo(DestinosNavegacion.Inicio.ruta) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = { Icon(destino.icono, contentDescription = destino.titulo) },
                        label = { Text(destino.titulo) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF38BDF8),
                            unselectedIconColor = Color.Gray,
                            selectedTextColor = Color(0xFF38BDF8),
                            unselectedTextColor = Color.Gray
                        )
                    )
                }
            }
        }
    ) { paddingValores ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValores)
                .background(Color(0xFF0F172A))
        ) {
            // Aquí se llama al grafo de navegación
            GrafoNavegacion(navController = navController)
        }
    }
}