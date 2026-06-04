package com.example.antiestafas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.* // El asterisco importa automáticamente todos los que necesitas
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.antiestafas.data.SupabaseClient
import com.example.antiestafas.data.SupabaseRepository
import kotlinx.coroutines.launch
import io.github.jan.supabase.android.initializeAndroidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SupabaseClient.client.initializeAndroidContext(this.applicationContext)
        setContent {
            MaterialTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var itemSeleccionado by remember { mutableStateOf(0) }

    val titulos = listOf("Inicio", "URL Check", "Wiki Estafas", "Detecciones")
    // Íconos correspondientes a tus 4 pantallas definidas
    val iconos = listOf(Icons.Default.List, Icons.Default.Info, Icons.Default.Info, Icons.Default.Warning)

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF1E293B)) {
                iconos.forEachIndexed { indice, icono ->
                    NavigationBarItem(
                        selected = itemSeleccionado == indice,
                        onClick = { itemSeleccionado = indice },
                        icon = { Icon(icono, contentDescription = titulos[indice]) },
                        label = { Text(titulos[indice]) },
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
                .background(Color(0xFF0F172A)) // Fondo oscuro general igual a tu Figma
        ) {
            when (itemSeleccionado) {
                0 -> PantallaInicioDefinitiva()
                1 -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("URL Check", color = Color.White) }
                2 -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Wiki Estafas", color = Color.White) }
                3 -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Detecciones (SMS/WA/Gmail)", color = Color.White) }
            }
        }
    }
}

@Composable
fun PantallaInicioDefinitiva() {
    val scope = rememberCoroutineScope()

    // Estados dinámicos conectados a Supabase
    var totalMensajes by remember { mutableStateOf(0) }
    var totalUrls by remember { mutableStateOf(0) }
    var totalLlamadas by remember { mutableStateOf(0) }
    var totalAlertas by remember { mutableStateOf(0) }

    // Carga de datos automáticos de las tablas desde Supabase
    LaunchedEffect(Unit) {
        scope.launch {
            val sms = SupabaseRepository.obtenerContadorGoblal("sms")
            val whatsapp = SupabaseRepository.obtenerContadorGoblal("whatsapp")
            val gmail = SupabaseRepository.obtenerContadorGoblal("gmail")

            totalMensajes = sms + whatsapp + gmail
            totalUrls = SupabaseRepository.obtenerContadorGoblal("url")
            totalLlamadas = SupabaseRepository.obtenerContadorGoblal("llamada")
            totalAlertas = SupabaseRepository.obtenerContadorGoblal(soloAlertas = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        // 1. TÍTULO DE BIENVENIDA O BIENVENIDO DE TU FIGMA
        Text(
            text = "¡Bienvenido!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 2. TARJETA PRINCIPAL (Estado de protección de tu diseño)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)) // Gris azulado
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Puedes cambiar este texto o ícono por el escudo verde/azul que tienes dibujado
                Text(
                    text = "🛡️",
                    fontSize = 40.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tu dispositivo está protegido",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF38BDF8) // Color celeste brillante de acento
                )
                Text(
                    text = "Análisis automático activo",
                    fontSize = 12.sp,
                    color = Color.LightGray
                )
            }
        }

        // 3. TÍTULO DE LA SECCIÓN DE ESTADÍSTICAS
        Text(
            text = "Resumen de análisis",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // 4. EL GRID DE 2x2 IGUAL AL DE TU IMAGEN
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                CardEstadisticaFigma(
                    titulo = "Mensajes\nEscaneados",
                    valor = totalMensajes,
                    icono = Icons.Default.Email,
                    colorIcono = Color(0xFF38BDF8)
                )
            }
            item {
                CardEstadisticaFigma(
                    titulo = "Enlaces\nAnalizados",
                    valor = totalUrls,
                    icono = Icons.Default.Info, // Reemplazable por un ícono de Link
                    colorIcono = Color(0xFFFB923C) // Naranja
                )
            }
            item {
                CardEstadisticaFigma(
                    titulo = "Llamadas\nAnalizadas",
                    valor = totalLlamadas,
                    icono = Icons.Default.Phone,
                    colorIcono = Color(0xFF4ADE80) // Verde
                )
            }
            item {
                CardEstadisticaFigma(
                    titulo = "Alertas\nDetectadas",
                    valor = totalAlertas,
                    icono = Icons.Default.Warning,
                    colorIcono = Color(0xFFF87171), // Rojo
                    esAlerta = totalAlertas > 0
                )
            }
        }
    }
}

// Componente estilizado para cada tarjeta contenedora del Grid
@Composable
fun CardEstadisticaFigma(
    titulo: String,
    valor: Int,
    icono: ImageVector,
    colorIcono: Color,
    esAlerta: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (esAlerta) Color(0xFF451A03) else Color(0xFF1E293B) // Fondo rojizo si hay alertas reales
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = colorIcono,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = valor.toString(),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (esAlerta) Color(0xFFF87171) else Color.White
                )
            }
            Text(
                text = titulo,
                fontSize = 13.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.LightGray
            )
        }
    }
}