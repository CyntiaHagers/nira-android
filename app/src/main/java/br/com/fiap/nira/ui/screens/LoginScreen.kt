package br.com.fiap.nira.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
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

// Definição da classe User para a base de dados
data class User(val email: String, val password: String)

@Composable
fun LoginScreen(
    onEntrar: () -> Unit,
    onCadastrar: () -> Unit,
    onModoSeguro: () -> Unit
) {
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val messageState = remember { mutableStateOf<String?>(null) }
    val messageColorState = remember { mutableStateOf(Color.Red) }
    val showRegisterDialog = remember { mutableStateOf(false) }

    // Simulação de uma base de dados local de usuários
    val userList = remember {
        mutableStateListOf(
            User("teste@fiap.com.br", "123456"),
            User("ana@gmail.com", "ana123")
        )
    }

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
                    .size(160.dp)
                    .padding(
                        top = 50.dp,
                        bottom = 50.dp
                    )
            )

            // campo e-mail
            OutlinedTextField(
                value = emailState.value,
                onValueChange = {
                    emailState.value = it
                    if (it.isNotEmpty()) {
                        messageState.value = null
                    }
                },
                placeholder = {
                    Text(
                        text = "E-mail",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.dongle_regular)),
                            fontSize = 26.sp,
                            color = Color.DarkGray
                        )
                    )
                },
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
                    cursorColor = Color(0xFF7236A8),
                    focusedTextColor = Color.DarkGray,
                    unfocusedTextColor = Color.DarkGray,
                )
            )

            // senha
            OutlinedTextField(
                value = passwordState.value,
                onValueChange = {
                    passwordState.value = it
                    if (it.isNotEmpty()) {
                        messageState.value = null
                    }
                },
                placeholder = {
                    Text(
                        text = "Senha",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.dongle_regular)),
                            fontSize = 26.sp,
                            color = Color.DarkGray
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
                    cursorColor = Color(0xFF7236A8),
                    focusedTextColor = Color.DarkGray,
                    unfocusedTextColor = Color.DarkGray,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // botão entrar
            Button(
                onClick = {
                    if (emailState.value.isBlank() || passwordState.value.isBlank()) {
                        messageState.value = "Por favor, preencha todos os campos."
                        messageColorState.value = Color.Red
                    } else {
                        // AQUI ESTÁ A MUDANÇA: A validação foi removida.
                        // Qualquer e-mail e senha não vazios permitirão o login.
                        messageState.value = null
                        onEntrar()
                    }
                },
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

            // Mensagem de erro
            if (messageState.value != null) {
                Text(
                    text = messageState.value!!,
                    color = messageColorState.value,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botão Cadastrar
            OutlinedButton(
                onClick = {
                    showRegisterDialog.value = true
                },
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
                    color = Color.White,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.dongle_regular)),
                        fontSize = 24.sp
                    )
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

    // AlertDialog de Cadastro
    if (showRegisterDialog.value) {
        RegistrationDialog(
            onDismiss = { showRegisterDialog.value = false },
            onRegister = { email, password ->
                userList.add(User(email, password))
                showRegisterDialog.value = false
                messageState.value = "Usuário cadastrado com sucesso!"
                messageColorState.value = Color.Green
            }
        )
    }
}

@Composable
fun RegistrationDialog(
    onDismiss: () -> Unit,
    onRegister: (String, String) -> Unit
) {
    val emailDialogState = remember { mutableStateOf("") }
    val passwordDialogState = remember { mutableStateOf("") }
    val passwordConfirmDialogState = remember { mutableStateOf("") }
    val dialogErrorState = remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Cadastrar Novo Usuário",
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        },
        text = {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = emailDialogState.value,
                    onValueChange = { emailDialogState.value = it },
                    label = { Text("E-mail") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedLabelColor = Color.DarkGray,
                        unfocusedLabelColor = Color.DarkGray
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = passwordDialogState.value,
                    onValueChange = { passwordDialogState.value = it },
                    label = { Text("Senha") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedLabelColor = Color.DarkGray,
                        unfocusedLabelColor = Color.DarkGray
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = passwordConfirmDialogState.value,
                    onValueChange = { passwordConfirmDialogState.value = it },
                    label = { Text("Confirmar Senha") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedLabelColor = Color.DarkGray,
                        unfocusedLabelColor = Color.DarkGray
                    )
                )
                if (dialogErrorState.value != null) {
                    Text(
                        text = dialogErrorState.value!!,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (emailDialogState.value.isBlank() || passwordDialogState.value.isBlank() || passwordConfirmDialogState.value.isBlank()) {
                        dialogErrorState.value = "Por favor, preencha todos os campos."
                    } else if (passwordDialogState.value != passwordConfirmDialogState.value) {
                        dialogErrorState.value = "As senhas não coincidem."
                    } else {
                        onRegister(emailDialogState.value, passwordDialogState.value)
                        onDismiss()
                    }
                }
            ) {
                Text("Cadastrar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
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