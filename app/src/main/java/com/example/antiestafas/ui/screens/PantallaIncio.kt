package com.example.antiestafas.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.antiestafas.data.SupabaseRepository
import com.example.antiestafas.ui.screens.components.CardEstadistica
import kotlinx.coroutines.launch

@Composable
fun PantallaInicio() {
    val scope = rememberCoroutineScope()
    var totalMensajes by remember { mutableStateOf(0) }
    var totalUrls by remember { mutableStateOf(0) }
    var totalLlamadas by remember { mutableStateOf(0) }
    var totalAlertas by remember { mutableStateOf(0) }

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

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("¡Bienvenido!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(bottom = 16.dp))

        Card(
            modifier = Modifier.fillMaxWidth().height(160.dp).padding(bottom = 24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🛡️", fontSize = 40.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Tu dispositivo está protegido", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF38BDF8))
                Text("Análisis automático activo", fontSize = 12.sp, color = Color.LightGray)
            }
        }

        Text("Resumen de análisis", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Gray, modifier = Modifier.padding(bottom = 12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item { CardEstadistica("Mensajes\nEscaneados", totalMensajes, Icons.Default.Email, Color(0xFF38BDF8)) }
            item { CardEstadistica("Enlaces\nAnalizados", totalUrls, Icons.Default.Info, Color(0xFFFB923C)) }
            item { CardEstadistica("Llamadas\nAnalizadas", totalLlamadas, Icons.Default.Phone, Color(0xFF4ADE80)) }
            item { CardEstadistica("Alertas\nDetectadas", totalAlertas, Icons.Default.Warning, Color(0xFFF87171), esAlerta = totalAlertas > 0) }
        }
    }
}