package com.example.antiestafas.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.antiestafas.ui.screens.*

@Composable
fun GrafoNavegacion(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = DestinosNavegacion.Inicio.ruta,
        modifier = modifier
    ) {
        composable(DestinosNavegacion.Inicio.ruta) { PantallaInicio(navController) }
        composable(DestinosNavegacion.UrlCheck.ruta) { PantallaUrlCheck() }
        composable(DestinosNavegacion.WikiEstafas.ruta) { PantallaWikiEstafas() }
        composable(DestinosNavegacion.Detecciones.ruta) { PantallaDetecciones() }
    }
}