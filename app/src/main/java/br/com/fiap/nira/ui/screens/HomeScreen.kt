package br.com.fiap.nira.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.nira.R
import br.com.fiap.nira.ui.theme.NiraTheme

@Composable
fun HomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x6F7939A4))
            .padding(16.dp)
    ) {
        // logo no topo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_nira_app),
                contentDescription = "Logo Nira",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(101.dp)
                    .height(39.dp)
            )
        }

        // texto de boas-vindas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
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
        }

        // conteúdo central
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { /* TODO: ação de emergência */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Emergência imediata")
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { /* TODO: encontrar abrigo */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD740)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Encontrar Abrigo", color = Color.Black)
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { /* TODO: chatbot */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD740)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Chatbot de ajuda", color = Color.Black)
            }

            Spacer(Modifier.height(24.dp))

            // 1) BOTÃO para ir ao Feedback
            Button(
                onClick = { navController.navigate("feedback") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Deixar Feedback")
            }

            Spacer(Modifier.height(16.dp))

            // 2) BOX/CARD clicável para ir ao Feedback (opção “box” que você pediu)
            Surface(
                color = Color(0xFFFFFFFF),
                contentColor = Color(0xFF222222),
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 4.dp,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .clickable { navController.navigate("feedback") }
            ) {
                Row(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "Enviar feedback sobre o app",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.weight(1f))
                    Text("Abrir >", color = Color(0xFF6200EE))
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    NiraTheme {
        val nav = rememberNavController()
        HomeScreen(nav)
    }
}
