package br.com.fiap.nira.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.nira.R

@Composable
fun LoginScreen(
    onEntrar: () -> Unit,
    onCadastrar: () -> Unit,
    onModoSeguro: () -> Unit
) {
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF7236A8), Color(0xFFD8306B))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo_branca),
                contentDescription = "Logo Nira",
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 16.dp)
            )

            // campo e-mail
            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                placeholder = {
                    Text(
                        text = "E-mail",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.dongle_regular)),
                            fontSize = 26.sp
                        )
                    )
                },          // placeholder
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(16.dp),         // mais arredondado (opcional)
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,    // 👈 fundo branco
                    unfocusedContainerColor = Color.White,  // 👈 fundo branco
                    disabledContainerColor = Color.White,
                    focusedBorderColor = Color.Transparent, // sem borda
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color(0xFF7236A8)         // roxo do app (opcional)
                )
            )

            // senha
            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                placeholder = {
                    Text(
                        text = "Senha",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.dongle_regular)),
                            fontSize = 26.sp
                        )
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                trailingIcon = { Icon(Icons.Default.Lock, contentDescription = "Senha") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color(0xFF7236A8)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botão Entrar
            Button(
                onClick = onEntrar,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7A10E)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    "Entrar",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF7236A8),
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.dongle_regular)),
                        fontSize = 26.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botão Cadastrar
            OutlinedButton(
                onClick = onCadastrar,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text ="Cadastrar-se",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.dongle_regular)),
                        fontSize = 26.sp
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botão Modo Seguro
            Button(
                onClick = onModoSeguro,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7236A8)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(Icons.Default.Lock, contentDescription = "Modo Seguro", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "MODO SEGURO",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Modo seguro disponível para sua proteção",
                fontSize = 14.sp,
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        onEntrar = {},
        onCadastrar = {},
        onModoSeguro = {}
    )
}