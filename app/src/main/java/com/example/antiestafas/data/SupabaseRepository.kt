package com.example.antiestafas.data

import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object SupabaseRepository {

    // 1. Buscar si un número está reportado
    suspend fun buscarNumeroSospechoso(numeroBuscado: String): NumeroReportado? {
        return withContext(Dispatchers.IO) {
            try {
                // Forzamos explícitamente el tipo de retorno en la deserialización <NumeroReportado>
                val resultado = SupabaseClient.client.postgrest["numeros_reportados"]
                    .select {
                        filter { eq("numero", numeroBuscado) }
                    }.decodeList<NumeroReportado>() // 🌟 AQUÍ

                resultado.firstOrNull()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    // 2. Insertar un nuevo evento de escaneo en el historial
    suspend fun registrarEscaneo(escaneo: HistorialEscaneo): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                SupabaseClient.client.postgrest["historial_escaneos"].insert(escaneo)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    // 3. Obtener la cantidad de registros según criterios (para tus contadores)
    suspend fun obtenerContadorGoblal(tipo: String? = null, soloAlertas: Boolean = false): Int {
        return withContext(Dispatchers.IO) {
            try {
                // Forzamos explícitamente el tipo de retorno <HistorialEscaneo>
                val resultado = SupabaseClient.client.postgrest["historial_escaneos"]
                    .select {
                        filter {
                            if (tipo != null) eq("tipo_escaneo", tipo)
                            if (soloAlertas) eq("alerta_generada", true)
                        }
                    }.decodeList<HistorialEscaneo>() // 🌟 AQUÍ

                resultado.size
            } catch (e: Exception) {
                e.printStackTrace()
                0
            }
        }
    }
}
