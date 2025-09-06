package br.com.fiap.nira.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import br.com.fiap.nira.R
import br.com.fiap.nira.ui.theme.NiraTheme
import br.com.fiap.nira.ui.theme.White
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.launch
import kotlin.math.*
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen() {
    val cs = MaterialTheme.colorScheme
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val isPreview = androidx.compose.ui.platform.LocalInspectionMode.current

    // Local inicial (São Paulo)
    val start = LatLng(-23.5505, -46.6333)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(start, 12f)
    }

    // Places
    val placesClient: PlacesClient = remember {
        if (!Places.isInitialized()) {
            Places.initialize(context, context.getString(R.string.google_maps_key))
        }
        Places.createClient(context)
    }

    // Fused Location
    val fused by remember { mutableStateOf(LocationServices.getFusedLocationProviderClient(context)) }

    // Permissões (FINE + COARSE)
    var hasFine by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
        )
    }
    var hasCoarse by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
        )
    }
    val permLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        hasFine = result[Manifest.permission.ACCESS_FINE_LOCATION] == true
        hasCoarse = result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }
    LaunchedEffect(Unit) {
        if (!hasFine && !hasCoarse) {
            permLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // Busca manual (cidade)
    var query by remember { mutableStateOf("") }

    // Chips de filtro
    val categorias = listOf(
        "Delegacias" to "Delegacia da Mulher",
        "Abrigos" to "Abrigo para mulheres",
        "Centros de apoio" to "Centro de apoio à mulher"
    )
    var idxSel by remember { mutableStateOf(0) }
    val termoAtual = categorias[idxSel].second

    // Resultado
    data class PlaceInfo(val name: String, val address: String?, val phone: String?, val latLng: LatLng)
    var resultado by remember { mutableStateOf<PlaceInfo?>(null) }
    var loading by remember { mutableStateOf(false) }

    // distância esférica (m)
    fun distanceMeters(a: LatLng, b: LatLng): Double {
        val R = 6371000.0
        val dLat = Math.toRadians(b.latitude - a.latitude)
        val dLng = Math.toRadians(b.longitude - a.longitude)
        val s1 = sin(dLat / 2).pow(2.0)
        val s2 = cos(Math.toRadians(a.latitude)) * cos(Math.toRadians(b.latitude)) * sin(dLng / 2).pow(2.0)
        return 2 * R * asin(sqrt(s1 + s2))
    }

    // Busca por perto: restringe região + pega melhor candidato por distância
    fun buscarPerto(term: String, latLng: LatLng, recenterToResult: Boolean = true) {
        loading = true

        val d = 0.05 // ~5 km
        val bounds = RectangularBounds.newInstance(
            LatLng(latLng.latitude - d, latLng.longitude - d),
            LatLng(latLng.latitude + d, latLng.longitude + d)
        )

        val token = AutocompleteSessionToken.newInstance()

        val builder = FindAutocompletePredictionsRequest.builder()
            .setQuery(term)
            .setSessionToken(token)
            .setCountries(listOf("BR"))
            .setLocationBias(bounds)

        // Algumas versões não têm esse setter
        try { builder.setLocationRestriction(bounds) } catch (_: Throwable) {}

        val req = builder.build()

        placesClient.findAutocompletePredictions(req)
            .addOnSuccessListener { resp ->
                val preds = resp.autocompletePredictions.take(5)
                if (preds.isEmpty()) {
                    resultado = null
                    loading = false
                    return@addOnSuccessListener
                }

                val fields = listOf(
                    Place.Field.NAME, Place.Field.ADDRESS,
                    Place.Field.PHONE_NUMBER, Place.Field.LAT_LNG, Place.Field.TYPES
                )

                var best: Place? = null
                var bestDist = Double.MAX_VALUE
                var processed = 0
                val total = preds.size

                preds.forEach { p ->
                    val fetch = FetchPlaceRequest.newInstance(p.placeId, fields)
                    placesClient.fetchPlace(fetch)
                        .addOnSuccessListener { r ->
                            val plc = r.place
                            val pos = plc.latLng
                            if (pos != null) {
                                val preferido =
                                    termoAtual.contains("Delegacia", ignoreCase = true) &&
                                            (plc.types ?: emptyList()).any { it == Place.Type.POLICE }
                                val dist = distanceMeters(latLng, pos) - if (preferido) 200.0 else 0.0
                                if (dist < bestDist) {
                                    bestDist = dist
                                    best = plc
                                }
                            }
                        }
                        .addOnCompleteListener {
                            processed++
                            if (processed == total) {
                                if (best != null) {
                                    val posOk = best!!.latLng ?: latLng
                                    resultado = PlaceInfo(
                                        name = best!!.name ?: term,
                                        address = best!!.address,
                                        phone = best!!.phoneNumber,
                                        latLng = posOk
                                    )
                                    if (recenterToResult) {
                                        scope.launch {
                                            cameraPositionState.animate(
                                                CameraUpdateFactory.newLatLngZoom(posOk, 15f)
                                            )
                                        }
                                    }
                                } else {
                                    resultado = null
                                }
                                loading = false
                            }
                        }
                }
            }
            .addOnFailureListener {
                resultado = null
                loading = false
            }
    }

    // Rebuscar ao parar de mover OU trocar chip
    LaunchedEffect(cameraPositionState.isMoving, idxSel) {
        if (!cameraPositionState.isMoving) {
            buscarPerto(termoAtual, cameraPositionState.position.target)
        }
    }

    // Usar minha localização (robusto: current + fallback)
    fun usarMinhaLocalizacao() {
        if (!hasFine && !hasCoarse) {
            Toast.makeText(context, "Permita acesso à localização para usar este recurso.", Toast.LENGTH_SHORT).show()
            permLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            return
        }

        val locationEnabled = try {
            Settings.Secure.getInt(
                context.contentResolver,
                Settings.Secure.LOCATION_MODE
            ) != Settings.Secure.LOCATION_MODE_OFF
        } catch (_: Exception) { true }
        if (!locationEnabled) {
            Toast.makeText(context, "Ative a localização do dispositivo.", Toast.LENGTH_SHORT).show()
            context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            return
        }

        val cts = CancellationTokenSource()
        fused.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cts.token)
            .addOnSuccessListener { loc ->
                if (loc != null) {
                    val here = LatLng(loc.latitude, loc.longitude)
                    scope.launch {
                        cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(here, 15f))
                    }
                    // Não recentra no resultado ao usar o FAB
                    buscarPerto(termoAtual, here, recenterToResult = false)
                } else {
                    fused.lastLocation
                        .addOnSuccessListener { last ->
                            if (last != null) {
                                val here = LatLng(last.latitude, last.longitude)
                                scope.launch {
                                    cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(here, 15f))
                                }
                                buscarPerto(termoAtual, here, recenterToResult = false)
                            } else {
                                Toast.makeText(context, "Não foi possível obter sua localização agora.", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Erro ao obter localização (fallback).", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Erro ao obter localização atual.", Toast.LENGTH_SHORT).show()
            }
    }

    Box(Modifier.fillMaxSize()) {
        // MAPA (com clique em POI e no mapa)
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = !isPreview && (hasFine || hasCoarse)
            ),
            uiSettings = MapUiSettings(
                myLocationButtonEnabled = true,
                zoomControlsEnabled = false,
                compassEnabled = true
            ),
            onPOIClick = { poi ->
                val fields = listOf(
                    Place.Field.NAME,
                    Place.Field.ADDRESS,
                    Place.Field.PHONE_NUMBER,
                    Place.Field.LAT_LNG
                )
                val req = FetchPlaceRequest.newInstance(poi.placeId, fields)
                placesClient.fetchPlace(req)
                    .addOnSuccessListener { r ->
                        val p = r.place
                        val pos = p.latLng ?: cameraPositionState.position.target
                        resultado = PlaceInfo(
                            name = p.name ?: poi.name,
                            address = p.address,
                            phone = p.phoneNumber,
                            latLng = pos
                        )
                        scope.launch {
                            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(pos, 15f))
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Não foi possível obter detalhes do local.", Toast.LENGTH_SHORT).show()
                    }
            },
            onMapClick = { latLng ->
                buscarPerto(termoAtual, latLng, recenterToResult = true)
                scope.launch {
                    cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
                }
            }
        ) {
            resultado?.let { place ->
                MarkerInfoWindow(
                    state = MarkerState(place.latLng),
                    title = place.name,
                    snippet = place.address ?: "",
                    onClick = { marker ->
                        scope.launch {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(place.latLng, 15f)
                            )
                        }
                        false
                    }
                ) { marker ->
                    Column(Modifier.padding(8.dp)) {
                        Text(marker.title ?: place.name, fontWeight = FontWeight.Bold)
                        marker.snippet?.takeIf { it.isNotBlank() }?.let { Text(it) }
                    }
                }
            }
        }

        // TOPO: busca + chips (em card elevado)
        Column(
            Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 12.dp, start = 16.dp, end = 16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Pesquisar cidade") },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = White,
                    unfocusedContainerColor = White,
                    disabledContainerColor = White,
                    focusedBorderColor = cs.primary,
                    unfocusedBorderColor = White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = cs.primary
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        val pos = try {
                            val geo = android.location.Geocoder(context, Locale("pt", "BR"))
                            val res = geo.getFromLocationName("$query, Brasil", 1)
                            res?.firstOrNull()?.let { LatLng(it.latitude, it.longitude) }
                        } catch (_: Exception) { null }
                        if (pos != null) {
                            scope.launch {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newLatLngZoom(pos, 13f)
                                )
                            }
                        } else {
                            Toast.makeText(context, "Cidade não encontrada", Toast.LENGTH_SHORT).show()
                        }
                    }
                ),
                trailingIcon = {
                    IconButton(onClick = {
                        val pos = try {
                            val geo = android.location.Geocoder(context, Locale("pt", "BR"))
                            val res = geo.getFromLocationName("$query, Brasil", 1)
                            res?.firstOrNull()?.let { LatLng(it.latitude, it.longitude) }
                        } catch (_: Exception) { null }
                        if (pos != null) {
                            scope.launch {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newLatLngZoom(pos, 13f)
                                )
                            }
                        }
                    }) { Icon(Icons.Default.Search, contentDescription = "Buscar") }
                }
            )

            Spacer(Modifier.height(10.dp))

            ElevatedCard(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = White)
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    itemsIndexed(categorias) { i, (label, _) ->
                        FilterChip(
                            selected = i == idxSel,
                            onClick = { idxSel = i },
                            label = { Text(label) },
                            shape = RoundedCornerShape(18.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = cs.primary,
                                selectedLabelColor = cs.onPrimary,
                                containerColor = Color(0xFFF4F4F4),
                                labelColor = Color.Black
                            )
                        )
                    }
                }
            }
        }

        // FAB: Usar minha localização
        ExtendedFloatingActionButton(
            onClick = { usarMinhaLocalizacao() },
            icon = { Icon(Icons.Default.MyLocation, contentDescription = "Minha localização") },
            text = { Text("Usar minha localização") },
            containerColor = cs.primary,
            contentColor = cs.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 140.dp)
        )

        // CARD inferior
        Surface(
            color = cs.primary,
            contentColor = cs.onPrimary,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Column(Modifier.fillMaxWidth().padding(20.dp)) {
                when {
                    loading -> {
                        Text("Procurando ${categorias[idxSel].first.lowercase()} por perto…")
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            color = cs.onPrimary
                        )
                    }
                    resultado != null -> {
                        Text(
                            resultado!!.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(resultado!!.address ?: "Endereço não disponível")
                        Spacer(Modifier.height(16.dp))
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = {
                                    val phone = resultado!!.phone ?: "190"
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                                    context.startActivity(intent)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) { Text("LIGAR", fontWeight = FontWeight.Bold) }

                            OutlinedButton(
                                onClick = {
                                    val dest = resultado!!.latLng
                                    val label = Uri.encode(resultado!!.name)
                                    val gmmUri = Uri.parse("google.navigation:q=${dest.latitude},${dest.longitude}($label)")
                                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmUri)
                                        .apply { setPackage("com.google.android.apps.maps") }
                                    context.startActivity(mapIntent)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(2.dp, cs.onPrimary),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = cs.onPrimary)
                            ) { Text("ROTA", fontWeight = FontWeight.Bold) }
                        }
                    }
                    else -> {
                        Text("Selecione um ponto no mapa, pesquise uma cidade ou use sua localização")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MapScreenPreview() {
    NiraTheme { MapScreen() }
}
