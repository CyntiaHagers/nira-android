package br.com.fiap.nira.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.nira.R
import br.com.fiap.nira.ui.theme.NiraTheme

@Composable
fun HomeScreen(navController: NavController) {
    var showPanicButtonDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x6F7939A4))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo and Welcome Text
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.logo_nira_app),
                contentDescription = "Logo Nira",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(101.dp)
                    .height(39.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Bem-vinda, Maria",
                style = TextStyle(
                    fontSize = 48.sp,
                    lineHeight = 30.sp,
                    fontFamily = FontFamily(Font(R.font.dongle_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFFFFFFFF),
                )
            )

            // Main Action Buttons
            Spacer(modifier = Modifier.height(100.dp)) // Adjust this spacer to center content
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { showPanicButtonDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFA2828)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = "Emergência imediata",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.dongle_regular)),
                            fontSize = 26.sp
                        )
                    )
                }
                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("map") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD740)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = "Encontrar Abrigo",
                        color = Color.Black,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.dongle_regular)),
                            fontSize = 26.sp
                        )
                    )
                }
                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("chatbot") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFFFFF)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = "Chatbot de ajuda",
                        color = Color.Black,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.dongle_regular)),
                            fontSize = 26.sp
                        )
                    )
                }
            }

            // The Spacer with weight(1f) here pushes the content above it
            // and positions the feedback button at the bottom.
            Spacer(modifier = Modifier.weight(1f))

            // Feedback button at the bottom
            Surface(
                color = Color(0xFFFFFFFF),
                contentColor = Color(0xFF222222),
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 4.dp,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable { navController.navigate("feedback") },
            ) {
                Row(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Enviar feedback sobre o app",
                        color = Color(0xFF333333),
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.dongle_regular)),
                            fontSize = 24.sp
                        )
                    )
                    Text(
                        text = "Abrir >",
                        color = Color(0xFF6200EE),
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.dongle_regular)),
                            fontSize = 24.sp
                        )
                    )
                }
            }
        }
    }

    if (showPanicButtonDialog) {
        PanicButtonDialog(
            onDismissRequest = { showPanicButtonDialog = false }
        )
    }
}

@Composable
private fun PanicButtonDialog(onDismissRequest: () -> Unit) {
    val context = LocalContext.current
    val policeNumber = "190"

    // Request permission if not already granted
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted, proceed with the call
            val callIntent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:$policeNumber")
            }
            context.startActivity(callIntent)
            onDismissRequest() // Dismiss the dialog after initiating the call
        }
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Ação de Emergência") },
        text = { Text("Cuidado, ao clicar no botão abaixo, a polícia será acionada silenciosamente.") },
        confirmButton = {
            Button(
                onClick = {
                    when {
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CALL_PHONE
                        ) == PackageManager.PERMISSION_GRANTED -> {
                            // Permission is already granted, make the call
                            val callIntent = Intent(Intent.ACTION_CALL).apply {
                                data = Uri.parse("tel:$policeNumber")
                            }
                            context.startActivity(callIntent)
                            onDismissRequest() // Dismiss the dialog
                        }
                        else -> {
                            // Permission not granted, request it
                            requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFA2828)),
            ) {
                Text("Ligar para a Polícia")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text("Cancelar")
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    NiraTheme {
        val nav = rememberNavController()
        HomeScreen(nav)
    }
}