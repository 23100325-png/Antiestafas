package com.example.antiestafas.data

import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object SupabaseRepository {

    // 1. Buscar si un número está reportado
    suspend fun buscarNumeroSospechoso(numeroBuscado: String): NumeroReportado? {
        return withContext(Dispatchers.IO) {
            try {
                val resultado = SupabaseClient.client.postgrest["numeros_reportados"]
                    .select {
                        filter { eq("numero", numeroBuscado) }
                    }.decodeList<NumeroReportado>()
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

    // 3. Obtener el total de mensajes (sms y texto) y sus alertas
    suspend fun obtenerEstadisticasMensajes(): Pair<Int, Int> {
        return withContext(Dispatchers.IO) {
            try {
                val todos = SupabaseClient.client.postgrest["historial_escaneos"]
                    .select {
                        filter {
                            or {
                                eq("tipo_escaneo", "sms")
                                eq("tipo_escaneo", "texto")
                            }
                        }
                    }.decodeList<HistorialEscaneo>()
                
                val total = todos.size
                val alertas = todos.count { it.alerta_generada }
                total to alertas
            } catch (e: Exception) {
                e.printStackTrace()
                0 to 0
            }
        }
    }

    // 4. Obtener el total de URLs y sus alertas
    suspend fun obtenerEstadisticasUrls(): Pair<Int, Int> {
        return withContext(Dispatchers.IO) {
            try {
                val todos = SupabaseClient.client.postgrest["historial_escaneos"]
                    .select {
                        filter {
                            eq("tipo_escaneo", "url")
                        }
                    }.decodeList<HistorialEscaneo>()
                
                val total = todos.size
                val alertas = todos.count { it.alerta_generada }
                total to alertas
            } catch (e: Exception) {
                e.printStackTrace()
                0 to 0
            }
        }
    }

    // 5. Obtener total de números reportados (total de filas)
    suspend fun obtenerTotalNumerosReportados(): Int {
        return withContext(Dispatchers.IO) {
            try {
                val resultado = SupabaseClient.client.postgrest["numeros_reportados"]
                    .select().decodeList<NumeroReportado>()
                resultado.size
            } catch (e: Exception) {
                e.printStackTrace()
                0
            }
        }
    }
}
