package com.example.antiestafas.ui.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Componente estilizado para cada tarjeta contenedora del Grid
@Composable
fun CardEstadistica(
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