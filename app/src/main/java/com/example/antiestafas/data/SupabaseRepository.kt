package com.example.antiestafas.data

import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext

object SupabaseRepository {

    private val _cambiosFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val cambiosFlow = _cambiosFlow.asSharedFlow()

    private fun notificarCambio() {
        _cambiosFlow.tryEmit(Unit)
    }

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
                notificarCambio()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    // 2.1. Registrar o actualizar un número sospechoso
    suspend fun registrarOActualizarNumeroSospechoso(numero: String, modalidad: String, descripcion: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // 1. Buscar si ya existe
                val existente = buscarNumeroSospechoso(numero)

                if (existente != null) {
                    // 2. Si existe, actualizar
                    val nuevoContador = (existente.contador_reportes ?: 1) + 1
                    SupabaseClient.client.postgrest["numeros_reportados"].update(
                        {
                            set("modalidad", modalidad)
                            set("descripcion", descripcion)
                            set("contador_reportes", nuevoContador)
                        }
                    ) {
                        filter { eq("numero", numero) }
                    }
                } else {
                    // 3. Si no existe, insertar
                    val nuevo = NumeroReportado(
                        numero = numero,
                        modalidad = modalidad,
                        descripcion = descripcion,
                        contador_reportes = 1
                    )
                    SupabaseClient.client.postgrest["numeros_reportados"].insert(nuevo)
                }
                notificarCambio()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    // 3. Obtener estadísticas para Mensajes (SMS y Texto)
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

    // 4. Obtener estadísticas para URLs
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

    // --- Métodos para obtener Listas Detalladas ---

    suspend fun obtenerListaMensajesAlertas(): List<HistorialEscaneo> {
        return withContext(Dispatchers.IO) {
            try {
                SupabaseClient.client.postgrest["historial_escaneos"]
                    .select {
                        filter {
                            or {
                                eq("tipo_escaneo", "sms")
                                eq("tipo_escaneo", "texto")
                            }
                            eq("alerta_generada", true)
                        }
                    }.decodeList<HistorialEscaneo>()
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    suspend fun obtenerListaUrlsAlertas(): List<HistorialEscaneo> {
        return withContext(Dispatchers.IO) {
            try {
                SupabaseClient.client.postgrest["historial_escaneos"]
                    .select {
                        filter {
                            eq("tipo_escaneo", "url")
                            eq("alerta_generada", true)
                        }
                    }.decodeList<HistorialEscaneo>()
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    suspend fun obtenerListaNumerosReportados(): List<NumeroReportado> {
        return withContext(Dispatchers.IO) {
            try {
                SupabaseClient.client.postgrest["numeros_reportados"]
                    .select().decodeList<NumeroReportado>()
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    suspend fun obtenerHistorialCompleto(): List<HistorialEscaneo> {
        return withContext(Dispatchers.IO) {
            try {
                SupabaseClient.client.postgrest["historial_escaneos"]
                    .select().decodeList<HistorialEscaneo>()
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    suspend fun obtenerEscaneosRecientes(limite: Int? = 10): List<RecentScanUI> {
        return withContext(Dispatchers.IO) {
            try {
                // Obtener historial
                val historialQuery = SupabaseClient.client.postgrest["historial_escaneos"]
                    .select {
                        order("creado_el", order = io.github.jan.supabase.postgrest.query.Order.DESCENDING)
                        if (limite != null) limit(limite.toLong())
                    }.decodeList<HistorialEscaneo>()

                // Obtener números reportados
                val numerosQuery = SupabaseClient.client.postgrest["numeros_reportados"]
                    .select {
                        order("creado_el", order = io.github.jan.supabase.postgrest.query.Order.DESCENDING)
                        if (limite != null) limit(limite.toLong())
                    }.decodeList<NumeroReportado>()

                val combined = mutableListOf<RecentScanUI>()

                historialQuery.forEach {
                    combined.add(
                        RecentScanUI(
                            id = it.id ?: 0L,
                            title = it.origen_datos,
                            type = it.tipo_escaneo,
                            subtitle = "${it.tipo_escaneo.uppercase()} • Escaneo",
                            isRisk = it.alerta_generada,
                            timestamp = it.creado_el
                        )
                    )
                }

                numerosQuery.forEach {
                    combined.add(
                        RecentScanUI(
                            id = it.id ?: 0L,
                            title = it.numero,
                            type = "llamada",
                            subtitle = "${it.modalidad} • Reportado",
                            isRisk = true, // Los reportados siempre son riesgo
                            timestamp = it.creado_el
                        )
                    )
                }

                // Ordenar por fecha y tomar el límite
                val sorted = combined.sortedByDescending { it.timestamp }
                if (limite != null) sorted.take(limite) else sorted
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    // 6. Obtener estadísticas globales para el cálculo de riesgo
    suspend fun obtenerEstadisticasGlobales(): Pair<Int, Int> {
        return withContext(Dispatchers.IO) {
            try {
                val todos = SupabaseClient.client.postgrest["historial_escaneos"]
                    .select().decodeList<HistorialEscaneo>()
                val total = todos.size
                val alertas = todos.count { it.alerta_generada }
                total to alertas
            } catch (e: Exception) {
                e.printStackTrace()
                0 to 0
            }
        }
    }
}
