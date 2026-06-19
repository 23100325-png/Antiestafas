package com.example.antiestafas.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.antiestafas.data.HistorialEscaneo
import com.example.antiestafas.data.SupabaseRepository
import kotlinx.coroutines.launch

@Composable
fun PantallaDetecciones() {
    val scope = rememberCoroutineScope()
    var totalEscaneos by remember { mutableIntStateOf(0) }
    var totalAlertas by remember { mutableIntStateOf(0) }
    var listaAlertas by remember { mutableStateOf<List<HistorialEscaneo>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }

    // Función para refrescar datos
    suspend fun cargarDatos() {
        val stats = SupabaseRepository.obtenerEstadisticasGlobales()
        totalEscaneos = stats.first
        totalAlertas = stats.second
        
        val msgs = SupabaseRepository.obtenerListaMensajesAlertas()
        val urls = SupabaseRepository.obtenerListaUrlsAlertas()
        listaAlertas = (msgs + urls).sortedByDescending { it.creado_el }
        
        cargando = false
    }

    LaunchedEffect(Unit) {
        cargarDatos()
    }

    // Escuchar cambios en tiempo real
    LaunchedEffect(Unit) {
        SupabaseRepository.cambiosFlow.collect {
            cargarDatos()
        }
    }

    val riesgoRatio = if (totalEscaneos > 0) (totalAlertas.toFloat() / totalEscaneos.toFloat()).coerceIn(0f, 1f) else 0f
    val porcentajeRiesgo = (riesgoRatio * 100).toInt()
    
    // Interpolación de color: 0% Verde -> 100% Rojo
    val colorBase = lerp(Color(0xFF10B981), Color(0xFFEF4444), riesgoRatio)
    val colorFondoOscuro = lerp(Color(0xFF064E3B), Color(0xFF7F1D1D), riesgoRatio)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(colorFondoOscuro, Color(0xFF0F172A))
                )
            )
    ) {
        if (cargando) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icono de Escudo
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Surface(
                        modifier = Modifier.size(100.dp),
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.1f),
                        border = null
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            // Círculos concéntricos (opcional, estilo imagen)
                            Box(modifier = Modifier.size(80.dp).background(Color.White.copy(alpha = 0.05f), CircleShape))
                            Icon(
                                imageVector = if (riesgoRatio > 0.5f) Icons.Default.ShieldMoon else Icons.Default.Shield,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(50.dp)
                            )
                            if (riesgoRatio > 0.3f) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp).align(Alignment.Center)
                                )
                            }
                        }
                    }
                }

                // Badge de Alerta
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Surface(
                        color = if (riesgoRatio > 0.5f) Color(0xFFFACC15) else Color(0xFF4ADE80),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(modifier = Modifier.size(8.dp).background(Color.White, CircleShape))
                            Text(
                                text = if (riesgoRatio > 0.5f) "CRITICAL ALERT" else "SYSTEM SECURE",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                }

                // Título Principal
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (riesgoRatio > 0.5f) "POSIBLES FRAUDES \nDETECTADOS" else "FRAUDES NO\nENCONTRADOS",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        lineHeight = 34.sp
                    )
                }

                // Descripción
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (riesgoRatio > 0.1f) 
                            "Nuestros sistemas han detectado $totalAlertas potenciales indicadores de fraude en su reciente actividad."
                            else "Your activity is being monitored and no suspicious patterns have been detected.",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }

                // Barra de Nivel de Riesgo
                item {
                    Spacer(modifier = Modifier.height(40.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "NIVEL DE RIESGO",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                        Text(
                            text = when {
                                porcentajeRiesgo > 75 -> "CRITICO — $porcentajeRiesgo%"
                                porcentajeRiesgo > 40 -> "MEDIO — $porcentajeRiesgo%"
                                else -> "LOW — $porcentajeRiesgo%"
                            },
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    // Barra de progreso personalizada
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(riesgoRatio)
                                .fillMaxHeight()
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(Color(0xFFFACC15), Color(0xFFEF4444))
                                    ),
                                    RoundedCornerShape(6.dp)
                                )
                        )
                    }
                }

                // Sección de Alertas (WHY WAS THIS FLAGGED?)
                item {
                    Spacer(modifier = Modifier.height(40.dp))
                    Text(
                        text = "POR QUE ESTA ALERTANDO?",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (listaAlertas.isEmpty()) {
                    item {
                        Text(
                            "No recent alerts to display.",
                            color = Color.Gray,
                            modifier = Modifier.padding(vertical = 20.dp)
                        )
                    }
                } else {
                    items(listaAlertas) { alerta ->
                        ItemAlertaEstiloImagen(alerta)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ItemAlertaEstiloImagen(alerta: HistorialEscaneo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.08f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono con círculo
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = when (alerta.tipo_escaneo.lowercase()) {
                            "url" -> Icons.Default.Link
                            "sms", "texto" -> Icons.Default.HistoryEdu
                            "llamada" -> Icons.Default.Phone
                            else -> Icons.Default.Warning
                        },
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = when (alerta.tipo_escaneo.lowercase()) {
                            "url" -> "Link sospechoso"
                            "sms", "texto" -> "Mensaje sospechoso"
                            "llamada" -> "Número Sospechoso"
                            else -> "Threat Detected"
                        },
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "HIGH",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    text = alerta.detalle_ia ?: alerta.origen_datos,
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.5f),
                    maxLines = 2
                )
            }
        }
    }
}
