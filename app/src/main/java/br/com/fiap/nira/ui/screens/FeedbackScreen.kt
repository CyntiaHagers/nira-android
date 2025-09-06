package br.com.fiap.nira.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.nira.R


@Composable
fun FeedbackScreen(modifier: Modifier = Modifier) {
    // Estados
    var detalhes by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("") }

    // Gradiente de fundo
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF8E24AA), Color(0xFFD81B60))
    )

    Scaffold { paddingValues ->

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(gradient)
                .padding(paddingValues)
        ) {
            // Logo + NIRA no canto superior esquerdo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logos_simbolo),
                    contentDescription = "Logo Nira",
                    modifier = Modifier.size(80.dp)
                )
                Text(
                    text = "NIRA",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(start = 8.dp, bottom = 14.dp)
                )
            }

            // Conteúdo centralizado
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 100.dp, start = 24.dp, end = 24.dp)
            ) {

                // Título
                Text(
                    text = "Denúncia / Feedback",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(start = 12.dp, bottom = 24.dp)
                )

                // Opção: Local fechado ou inseguro
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    RadioButton(
                        selected = selectedOption == "local",
                        onClick = { selectedOption = "local" },
                        colors = RadioButtonDefaults.colors(selectedColor = Color.Red)
                    )
                    Text(
                        text = "Local fechado ou inseguro",
                        color = Color.White,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }

                // Opção: Dados incorretos
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                ) {
                    RadioButton(
                        selected = selectedOption == "dados",
                        onClick = { selectedOption = "dados" },
                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF3F51B5))
                    )
                    Text(
                        text = "Dados incorretos",
                        color = Color.White,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }

                // Campo de texto
                OutlinedTextField(
                    value = detalhes,
                    onValueChange = { detalhes = it },
                    placeholder = { Text("Escreva detalhes aqui...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.1f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = Color.White
                    )
                )

                // Botão enviar
                Button(
                    onClick = {
                        // TODO: enviar selectedOption + detalhes
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA000)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("ENVIAR", color = Color.Black, fontWeight = FontWeight.Bold)
                }

                // Rodapé
                Text(
                    text = "Suas informações são enviadas de forma anônima e criptografada",
                    fontSize = 16.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}