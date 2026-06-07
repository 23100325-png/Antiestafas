package com.example.antiestafas.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://nkzvikvzfzljgsyrhows.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im5renZpa3Z6ZnpsamdzeXJob3dzIiwicm9sZSI6ImFub24iLCJpYXQiOjE3ODA0OTY3NzEsImV4cCI6MjA5NjA3Mjc3MX0.HEqDnwsThUOR0hyhrIkz3hougl7R20b-rWUuCCY_KQ4"
    ) {
        // Al no declarar un httpEngine manual, el SDK usa su configuración interna
        // predeterminada, evitando errores de referencias no encontradas.

        install(Auth)
        install(Postgrest)
    }
}