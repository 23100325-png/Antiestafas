package com.example.antiestafas.data

import kotlinx.serialization.Serializable

// Mapea la tabla 'numeros_reportados'
@Serializable
data class NumeroReportado(
    val id: Long? = null,
    val numero: String,
    val modalidad: String,
    val descripcion: String? = null,
    val contador_reportes: Int? = null,
    val creado_el: String? = null
)

// Mapea la tabla 'historial_escaneos'
@Serializable
data class HistorialEscaneo(
    val id: Long? = null,
    val tipo_escaneo: String,    // 'sms', 'whatsapp', 'gmail', 'llamada', 'url'
    val origen_datos: String,
    val resultado: String,       // 'seguro', 'sospechoso'
    val detalle_ia: String? = null, // Explicación de por qué fue marcado
    val alerta_generada: Boolean = false,
    val creado_el: String? = null
)

data class RecentScanUI(
    val id: Long,
    val title: String,
    val type: String,
    val subtitle: String,
    val isRisk: Boolean,
    val timestamp: String?
)
