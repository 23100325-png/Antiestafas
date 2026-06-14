package com.example.antiestafas.data.virusTotal

data class ResultadoVirusTotal(
    val esPeligrosa: Boolean = false,
    val totalMotores: Int = 0,
    val deteccionesMaliciosas: Int = 0,
    val proveedoresConAlertas: List<Pair<String, String>> = emptyList()
)