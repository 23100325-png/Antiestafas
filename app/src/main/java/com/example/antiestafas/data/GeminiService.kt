package com.example.antiestafas.data

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

data class DiagnosticoIA(
    val esEstafa: Boolean = false,
    val veredicto: String = "Desconocido",
    val explicacion: String = ""
)

object GeminiService {
    // 🌟 RECUERDA COLOCAR TU API KEY REAL AQUÍ
    private const val API_KEY = ""

    private val model = GenerativeModel(
        modelName = "gemini-3.5-flash",
        apiKey = API_KEY,
        // En versiones recientes, las systemInstructions se pasan de esta manera estructurada
        systemInstruction = content {
            text("Eres un experto en ciberseguridad especializado en fraudes de Latinoamérica (Phishing, Smishing, ingeniería social). Tu tarea es analizar el texto que te proveerá el usuario. Debes responder estrictamente en formato JSON utilizando exactamente las siguientes llaves, sin textos adicionales fuera del JSON: {\"esEstafa\": true o false, \"veredicto\": \"TIPO DE FRAUDE O MENSAJE SEGURO\", \"explicacion\": \"Explicación detallada de por qué consideras que lo es o no\"}")
        }
    )

    suspend fun analizarTextoConIA(textoMensaje: String): DiagnosticoIA {
        return withContext(Dispatchers.IO) {
            try {
                // 🌟 SOLUCIÓN AL ERROR: Envolver el String dentro de la estructura 'content { text(...) }'
                val response = model.generateContent(content {
                    text(textoMensaje)
                })

                val jsonString = response.text?.trim() ?: ""

                val jsonLimpio = jsonString
                    .replace("```json", "")
                    .replace("```", "")
                    .trim()

                val jsonObject = JSONObject(jsonLimpio)

                return@withContext DiagnosticoIA(
                    esEstafa = jsonObject.optBoolean("esEstafa", false),
                    veredicto = jsonObject.optString("veredicto", "Análisis completado"),
                    explicacion = jsonObject.optString("explicacion", "No se pudo generar una explicación.")
                )
            } catch (e: Exception) {
                // 🌟 Esto imprimirá el error real en la pestaña 'Logcat' de Android Studio
                android.util.Log.e("GEMINI_ERROR", "Fallo total en la API: ${e.message}", e)

                return@withContext DiagnosticoIA(
                    esEstafa = false,
                    veredicto = "Error de conexión",
                    explicacion = "No se pudo conectar con los servidores de la IA. Detalle: ${e.localizedMessage}"
                )
            }
        }
    }
}