package com.example.antiestafas.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.antiestafas.data.*
import com.example.antiestafas.data.iaGemini.DiagnosticoIA
import com.example.antiestafas.data.iaGemini.GeminiService
import com.example.antiestafas.data.virusTotal.ResultadoVirusTotal
import com.example.antiestafas.data.virusTotal.VirusTotalService
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaUrlCheck() {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // --- ESTADOS SECCIÓN 1: VIRUSTOTAL (URL) ---
    var inputUrl by remember { mutableStateOf("") }
    var cargandoUrl by remember { mutableStateOf(false) }
    var mostrarResultadoUrl by remember { mutableStateOf(false) }
    var datosResultadoUrl by remember { mutableStateOf(ResultadoVirusTotal()) }

    // --- ESTADOS SECCIÓN 2: GEMINI IA (TEXTO) ---
    var inputTexto by remember { mutableStateOf("") }
    var cargandoTexto by remember { mutableStateOf(false) }
    var mostrarResultadoTexto by remember { mutableStateOf(false) }
    var datosResultadoTexto by remember { mutableStateOf(DiagnosticoIA()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(scrollState)
    ) {
        // ================= SECCIÓN 1: ANALIZADOR DE LINKS =================
        Text("Analizador Inteligente", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("Validación avanzada integrada con la API global de VirusTotal.", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 16.dp))

        OutlinedTextField(
            value = inputUrl,
            onValueChange = { inputUrl = it },
            label = { Text("Pegar enlace a analizar") },
            placeholder = { Text("https://url-sospechosa.com") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF1E293B), unfocusedContainerColor = Color(0xFF1E293B),
                focusedIndicatorColor = Color(0xFF38BDF8), unfocusedIndicatorColor = Color(0xFF0F172A),
                focusedLabelColor = Color(0xFF38BDF8), unfocusedLabelColor = Color.Gray,
                focusedTextColor = Color.White, unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (inputUrl.isNotBlank()) {
                    cargandoUrl = true
                    mostrarResultadoUrl = false
                    scope.launch {
                        val resultado = VirusTotalService.verificarUrlDetallada(inputUrl)
                        datosResultadoUrl = resultado

                        val nuevoEscaneo = HistorialEscaneo(
                            tipo_escaneo = "url",
                            origen_datos = inputUrl,
                            resultado = if (resultado.esPeligrosa) "malicioso" else "seguro",
                            alerta_generada = resultado.esPeligrosa
                        )
                        SupabaseRepository.registrarEscaneo(nuevoEscaneo)
                        cargandoUrl = false
                        mostrarResultadoUrl = true
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38BDF8), disabledContainerColor = Color(0xFF1E293B))
        ) {
            if (cargandoUrl) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp))
            else Text("Escanear con VirusTotal", color = Color.Black, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(visible = mostrarResultadoUrl) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = if (datosResultadoUrl.esPeligrosa) Color(0xFF451A03) else Color(0xFF064E3B))
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(if (datosResultadoUrl.esPeligrosa) "⚠️" else "🛡️", fontSize = 22.sp, modifier = Modifier.padding(end = 10.dp))
                        Text(
                            text = if (datosResultadoUrl.esPeligrosa) "${datosResultadoUrl.deteccionesMaliciosas}/${datosResultadoUrl.totalMotores} security vendors flagged this URL" else "0/${datosResultadoUrl.totalMotores} vendors flagged this URL",
                            fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = if (datosResultadoUrl.esPeligrosa) Color(0xFFF87171) else Color(0xFF4ADE80)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Column(
                    modifier = Modifier.fillMaxWidth().background(Color(0xFF1E293B), RoundedCornerShape(12.dp)).border(1.dp, Color(0xFF334155), RoundedCornerShape(12.dp)).padding(14.dp)
                ) {
                    if (datosResultadoUrl.proveedoresConAlertas.isEmpty()) {
                        Text("✅ Todos los proveedores de seguridad clasificaron este enlace como limpio.", fontSize = 13.sp, color = Color(0xFF4ADE80))
                    } else {
                        datosResultadoUrl.proveedoresConAlertas.forEachIndexed { indice, proveedor ->
                            FilaProveedorAntivirus(nombre = proveedor.first, resultado = proveedor.second)
                            if (indice < datosResultadoUrl.proveedoresConAlertas.lastIndex) {
                                Divider(color = Color(0xFF334155), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                            }
                        }
                    }
                }
            }
        }

        // 📌 DIVISOR DE SECCIONES (Línea elegante gris para separar ambas herramientas)
        Divider(color = Color(0xFF334155), thickness = 1.dp, modifier = Modifier.padding(vertical = 28.dp))

        // ================= SECCIÓN 2: ANALIZADOR DE TEXTO CON IA =================
        Text("Detector de Mensajes con IA", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("Pega el texto de un SMS, correo o WhatsApp sospechoso para ser auditado por Inteligencia Artificial.", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 16.dp))

        OutlinedTextField(
            value = inputTexto,
            onValueChange = { inputTexto = it },
            label = { Text("Pegar contenido del mensaje") },
            placeholder = { Text("Ej: BANCO: Tu cuenta ha sido bloqueada preventivamente, ingresa aquí para reestablecer...") },
            modifier = Modifier.fillMaxWidth().height(120.dp), // Campo más alto para textos largos
            shape = RoundedCornerShape(12.dp),
            maxLines = 5,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF1E293B), unfocusedContainerColor = Color(0xFF1E293B),
                focusedIndicatorColor = Color(0xFFA855F7), unfocusedIndicatorColor = Color(0xFF0F172A), // Color morado de acento para IA
                focusedLabelColor = Color(0xFFA855F7), unfocusedLabelColor = Color.Gray,
                focusedTextColor = Color.White, unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (inputTexto.isNotBlank()) {
                    cargandoTexto = true
                    mostrarResultadoTexto = false
                    scope.launch {
                        // 🌟 1. Analizamos el texto invocando a Gemini
                        val resultadoIA = GeminiService.analizarTextoConIA(inputTexto)
                        datosResultadoTexto = resultadoIA

                        // 2. Registramos la traza en Supabase para alimentar tus contadores generales
                        val nuevoEscaneo = HistorialEscaneo(
                            tipo_escaneo = "sms", // Se asume como mensaje/SMS por defecto
                            origen_datos = if(inputTexto.length > 30) inputTexto.take(30) + "..." else inputTexto,
                            resultado = if (resultadoIA.esEstafa) "sospechoso" else "seguro",
                            alerta_generada = resultadoIA.esEstafa
                        )
                        SupabaseRepository.registrarEscaneo(nuevoEscaneo)

                        cargandoTexto = false
                        mostrarResultadoTexto = true
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA855F7), disabledContainerColor = Color(0xFF1E293B)) // Botón Morado (Estilo IA)
        ) {
            if (cargandoTexto) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp))
            else Text("Analizar Mensaje con IA", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🧠 PANEL DE RESPUESTA DE LA INTELIGENCIA ARTIFICIAL
        AnimatedVisibility(visible = mostrarResultadoTexto) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (datosResultadoTexto.esEstafa) Color(0xFF451A03) else Color(0xFF064E3B)
                )
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    // Fila de encabezado del veredicto
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(if (datosResultadoTexto.esEstafa) "🤖 🚨" else "🤖 🛡️", fontSize = 20.sp, modifier = Modifier.padding(end = 8.dp))
                        Text(
                            text = datosResultadoTexto.veredicto.uppercase(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (datosResultadoTexto.esEstafa) Color(0xFFF87171) else Color(0xFF4ADE80)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Cuerpo de la explicación detallada
                    Text(
                        text = datosResultadoTexto.explicacion,
                        fontSize = 14.sp,
                        lineHeight = 19.sp,
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}

@Composable
fun FilaProveedorAntivirus(nombre: String, resultado: String) {
    val esLimpio = resultado.lowercase() == "clean" || resultado.lowercase() == "unrated"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = nombre,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White
        )
        Text(
            text = resultado,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (esLimpio) Color(0xFF4ADE80) else Color(0xFFF87171)
        )
    }
}