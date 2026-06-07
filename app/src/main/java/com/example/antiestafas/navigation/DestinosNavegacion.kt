package com.example.antiestafas.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector

sealed class DestinosNavegacion(val ruta: String, val titulo: String, val icono: ImageVector) {
    object Inicio : DestinosNavegacion("inicio", "Inicio", Icons.Default.List)
    object UrlCheck : DestinosNavegacion("url_check", "URL Check", Icons.Default.Info)
    object WikiEstafas : DestinosNavegacion("wiki_estafas", "Wiki Estafas", Icons.Default.Info)
    object Detecciones : DestinosNavegacion("detecciones", "Detecciones", Icons.Default.Warning)
}