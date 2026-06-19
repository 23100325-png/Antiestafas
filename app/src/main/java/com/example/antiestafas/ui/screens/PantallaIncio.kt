package com.example.antiestafas.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.antiestafas.navigation.DestinosNavegacion
import com.example.antiestafas.data.HistorialEscaneo
import com.example.antiestafas.data.NumeroReportado
import com.example.antiestafas.data.RecentScanUI
import com.example.antiestafas.data.SupabaseRepository
import com.example.antiestafas.ui.screens.components.CardEstadistica
import kotlinx.coroutines.launch

@Composable
fun ActionCard(
    title: String,
    description: String,
    icon: ImageVector,
    containerColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .height(130.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Icono en círculo
            Surface(
                color = Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Column {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
fun PantallaInicio(navController: NavController) {
    val scope = rememberCoroutineScope()

    // Estados para los datos de Supabase
    var totalMensajes by remember { mutableIntStateOf(0) }
    var alertasMensajes by remember { mutableIntStateOf(0) }
    var totalUrls by remember { mutableIntStateOf(0) }
    var alertasUrls by remember { mutableIntStateOf(0) }
    var totalLlamadasReportadas by remember { mutableIntStateOf(0) }
    var totalAlertasDetectadas by remember { mutableIntStateOf(0) }

    // Estado para escaneos recientes
    var escaneosRecientes by remember { mutableStateOf<List<RecentScanUI>>(emptyList()) }
    var cargandoRecientes by remember { mutableStateOf(true) }
    var verTodo by remember { mutableStateOf(false) }

    // Estados para el Diálogo de Detalles
    var mostrarDialogo by remember { mutableStateOf(false) }
    var tituloDialogo by remember { mutableStateOf("") }
    var cargandoDetalles by remember { mutableStateOf(false) }
    var listaMensajesDetalle by remember { mutableStateOf<List<HistorialEscaneo>>(emptyList()) }
    var listaNumerosDetalle by remember { mutableStateOf<List<NumeroReportado>>(emptyList()) }
    var tipoDetalle by remember { mutableStateOf("") } // "mensajes", "urls", "numeros"

    // Estado para reporte de número (Caller ID)
    var mostrarDialogoReporte by remember { mutableStateOf(false) }
    var numeroAReportar by remember { mutableStateOf("") }
    var modalidadAReportar by remember { mutableStateOf("Extorsión") }
    var descripcionAReportar by remember { mutableStateOf("") }
    var enviandoReporte by remember { mutableStateOf(false) }
    var expandidoModalidad by remember { mutableStateOf(false) }
    val opcionesModalidad = listOf("Extorsión", "Cuento del familiar", "Falsos premios", "Smishing", "Gota a gota", "Otros")

    // Función para refrescar todos los datos
    suspend fun refrescarDatos() {
        // 1. Obtener estadísticas de Mensajes (SMS y Texto)
        val (tm, am) = SupabaseRepository.obtenerEstadisticasMensajes()
        totalMensajes = tm
        alertasMensajes = am

        // 2. Obtener estadísticas de URLs
        val (tu, au) = SupabaseRepository.obtenerEstadisticasUrls()
        totalUrls = tu
        alertasUrls = au

        // 3. Obtener total de números reportados (total de filas de la tabla)
        totalLlamadasReportadas = SupabaseRepository.obtenerTotalNumerosReportados()

        // 4. Calcular total de Alertas Detectadas (suma de alertas TRUE de sms, texto y url)
        totalAlertasDetectadas = am + au

        // 5. Obtener escaneos recientes (10 o todos)
        cargandoRecientes = true
        escaneosRecientes = SupabaseRepository.obtenerEscaneosRecientes(if (verTodo) null else 10)
        cargandoRecientes = false
    }

    LaunchedEffect(verTodo) {
        refrescarDatos()
    }

    // Escuchar cambios en tiempo real (vía SharedFlow)
    LaunchedEffect(Unit) {
        SupabaseRepository.cambiosFlow.collect {
            refrescarDatos()
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "¡Bienvenido!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🛡️", fontSize = 40.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tu dispositivo está protegido",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF38BDF8)
                    )
                    Text(
                        text = "Análisis automático activo",
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                }
            }
        }

        item {
            Text(
                text = "Resumen de análisis",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Usamos Column y Row en lugar de Grid para estar dentro de un LazyColumn
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        CardEstadistica(
                            titulo = "Mensajes\nEscaneados",
                            valor = totalMensajes,
                            icono = Icons.Default.Email,
                            colorIcono = Color(0xFF38BDF8),
                            subValor = alertasMensajes,
                            subTitulo = "Alertas",
                            onClick = {
                                tituloDialogo = "Mensajes con Alerta"
                                tipoDetalle = "mensajes"
                                mostrarDialogo = true
                                cargandoDetalles = true
                                scope.launch {
                                    listaMensajesDetalle = SupabaseRepository.obtenerListaMensajesAlertas()
                                    cargandoDetalles = false
                                }
                            }
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        CardEstadistica(
                            titulo = "Enlaces\nAnalizados",
                            valor = totalUrls,
                            icono = Icons.Default.Info,
                            colorIcono = Color(0xFFFB923C),
                            subValor = alertasUrls,
                            subTitulo = "Alertas",
                            onClick = {
                                tituloDialogo = "Enlaces con Alerta"
                                tipoDetalle = "urls"
                                mostrarDialogo = true
                                cargandoDetalles = true
                                scope.launch {
                                    listaMensajesDetalle = SupabaseRepository.obtenerListaUrlsAlertas()
                                    cargandoDetalles = false
                                }
                            }
                        )
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        CardEstadistica(
                            titulo = "Llamadas\nReportadas",
                            valor = totalLlamadasReportadas,
                            icono = Icons.Default.Phone,
                            colorIcono = Color(0xFF4ADE80),
                            onClick = {
                                tituloDialogo = "Números Reportados"
                                tipoDetalle = "numeros"
                                mostrarDialogo = true
                                cargandoDetalles = true
                                scope.launch {
                                    listaNumerosDetalle = SupabaseRepository.obtenerListaNumerosReportados()
                                    cargandoDetalles = false
                                }
                            }
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        CardEstadistica(
                            titulo = "Alertas\nDetectadas",
                            valor = totalAlertasDetectadas,
                            icono = Icons.Default.Warning,
                            colorIcono = Color(0xFFF87171),
                            esAlerta = totalAlertasDetectadas > 0,
                            onClick = {
                                navController.navigate(DestinosNavegacion.Detecciones.ruta) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Botones de acción rediseñados (Action Cards)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ActionCard(
                        title = "URL Check",
                        description = "Copia y pega cualquier link",
                        icon = Icons.Default.Link,
                        containerColor = Color(0xFF1D4ED8), // Azul vibrante
                        modifier = Modifier.weight(1f),
                        onClick = {
                            navController.navigate(DestinosNavegacion.UrlCheck.ruta) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    ActionCard(
                        title = "Reportar número",
                        description = "Reporta cualquier número",
                        icon = Icons.Default.Phone,
                        containerColor = Color(0xFF1E293B), // Gris azulado oscuro
                        modifier = Modifier.weight(1f),
                        onClick = { mostrarDialogoReporte = true }
                    )
                }
            }
        }

        // --- SECCIÓN DE ESCANEOS RECIENTES ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Escaneos Recientes",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                TextButton(onClick = { verTodo = !verTodo }) {
                    Text(
                        text = if (verTodo) "Ver menos" else "Ver todo",
                        color = Color(0xFF38BDF8),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        if (cargandoRecientes && escaneosRecientes.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF38BDF8))
                }
            }
        } else if (escaneosRecientes.isEmpty()) {
            item {
                Text(
                    "No hay escaneos recientes.",
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }
        } else {
            items(escaneosRecientes) { scan ->
                CardRecentScan(scan)
            }
        }
    }

    // --- DIÁLOGOS (Fuera del LazyColumn) ---
    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = {
                Text(text = tituloDialogo, color = Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                Box(modifier = Modifier.fillMaxWidth().heightIn(max = 450.dp)) {
                    if (cargandoDetalles) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = Color(0xFF38BDF8)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            if (tipoDetalle == "numeros") {
                                items(listaNumerosDetalle) { item ->
                                    ItemDetalleSimplificado(
                                        principal = item.numero,
                                        secundario = item.modalidad
                                    )
                                }
                            } else {
                                items(listaMensajesDetalle) { item ->
                                    ItemDetalleSimplificado(
                                        principal = item.origen_datos,
                                        secundario = item.detalle_ia ?: item.resultado.uppercase()
                                    )
                                }
                            }

                            if (tipoDetalle != "numeros" && listaMensajesDetalle.isEmpty()) {
                                item {
                                    Text(
                                        "No hay registros sospechosos.",
                                        color = Color.Gray,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            } else if (tipoDetalle == "numeros" && listaNumerosDetalle.isEmpty()) {
                                item {
                                    Text(
                                        "No hay números reportados.",
                                        color = Color.Gray,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("Cerrar", color = Color(0xFF38BDF8), fontWeight = FontWeight.Bold)
                }
            },
            containerColor = Color(0xFF1E293B),
            shape = RoundedCornerShape(20.dp)
        )
    }

    if (mostrarDialogoReporte) {
        AlertDialog(
            onDismissRequest = { if (!enviandoReporte) mostrarDialogoReporte = false },
            title = { Text("Reportar Número", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = numeroAReportar,
                        onValueChange = { numeroAReportar = it },
                        label = { Text("Número de teléfono") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color(0xFF334155),
                            unfocusedContainerColor = Color(0xFF334155)
                        )
                    )

                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = modalidadAReportar,
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("Modalidad") },
                            trailingIcon = {
                                IconButton(onClick = { expandidoModalidad = !expandidoModalidad }) {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.White)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = Color(0xFF334155),
                                unfocusedContainerColor = Color(0xFF334155)
                            )
                        )
                        DropdownMenu(
                            expanded = expandidoModalidad,
                            onDismissRequest = { expandidoModalidad = false },
                            modifier = Modifier.background(Color(0xFF1E293B))
                        ) {
                            opcionesModalidad.forEach { opcion ->
                                DropdownMenuItem(
                                    text = { Text(opcion, color = Color.White) },
                                    onClick = {
                                        modalidadAReportar = opcion
                                        expandidoModalidad = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = descripcionAReportar,
                        onValueChange = { descripcionAReportar = it },
                        label = { Text("Descripción (Opcional)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color(0xFF334155),
                            unfocusedContainerColor = Color(0xFF334155)
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (numeroAReportar.isNotBlank()) {
                            enviandoReporte = true
                            scope.launch {
                                val exito = SupabaseRepository.registrarOActualizarNumeroSospechoso(
                                    numero = numeroAReportar,
                                    modalidad = modalidadAReportar,
                                    descripcion = descripcionAReportar
                                )
                                if (exito) {
                                    totalLlamadasReportadas = SupabaseRepository.obtenerTotalNumerosReportados()
                                    mostrarDialogoReporte = false
                                    numeroAReportar = ""
                                    descripcionAReportar = ""
                                    modalidadAReportar = "Extorsión"
                                }
                                enviandoReporte = false
                            }
                        }
                    },
                    enabled = !enviandoReporte,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF87171))
                ) {
                    if (enviandoReporte) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                    else Text("Enviar Reporte", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoReporte = false }, enabled = !enviandoReporte) {
                    Text("Cancelar", color = Color.Gray)
                }
            },
            containerColor = Color(0xFF1E293B)
        )
    }
}

@Composable
fun CardRecentScan(scan: RecentScanUI) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono circular
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(Color(0xFF334155).copy(alpha = 0.5f), RoundedCornerShape(22.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (scan.type.lowercase()) {
                        "sms" -> Icons.Default.Email
                        "url" -> Icons.Default.Language
                        "llamada" -> Icons.Default.Phone
                        else -> Icons.Default.Info
                    },
                    contentDescription = null,
                    tint = Color(0xFF38BDF8),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = scan.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1
                )
                Text(
                    text = scan.subtitle,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            // Badge de estado
            val isRisk = scan.isRisk
            val badgeColor = if (isRisk) Color(0xFFFEE2E2) else Color(0xFFDCFCE7)
            val textColor = if (isRisk) Color(0xFFEF4444) else Color(0xFF16A34A)
            val label = if (isRisk) "Riesgo" else "Seguro"
            val icon = if (isRisk) Icons.Default.Warning else Icons.Default.CheckCircle

            Surface(
                color = badgeColor,
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(icon, contentDescription = null, tint = textColor, modifier = Modifier.size(12.dp))
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                }
            }
        }
    }
}

@Composable
fun ItemDetalleSimplificado(principal: String, secundario: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF334155).copy(alpha = 0.5f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = principal,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                fontSize = 15.sp,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = secundario,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (secundario.contains("SOSPECHOSO") || secundario.contains("ALERTA") || secundario.isNotEmpty())
                    Color(0xFFF87171) else Color(0xFF4ADE80)
            )
        }
    }
}
