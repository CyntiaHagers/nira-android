package br.com.fiap.nira.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
// SOLUÇÃO 4: Importação do ícone AutoMirrored para corrigir o aviso de depreciação
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
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
import br.com.fiap.nira.R

// SOLUÇÃO 2: O Composable agora recebe estado e eventos como parâmetros (State Hoisting)
@Composable
fun ChatbotScreen(
    message: String,
    onMessageChange: (String) -> Unit,
    onSendMessage: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF7939A4))
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_nira_app),
            contentDescription = "Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(145.dp)
                .height(85.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Acolhimento Virtual",
                style = TextStyle(
                    fontSize = 40.sp,
                    lineHeight = 40.sp,
                    fontFamily = FontFamily(Font(R.font.dongle_regular)),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF512DA8), shape = RoundedCornerShape(32.dp))
                .padding(24.dp)
        ) {
            Text(
                text = "Olá, estou aqui para te ajudar.\nO que você precisa agora?",
                fontSize = 20.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Recomendo transformar estes botões em um componente reutilizável, como na análise anterior
        Box(
            modifier = Modifier
                .background(Color.Red, shape = RoundedCornerShape(18.dp))
                .padding(horizontal = 15.dp, vertical = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🏠", fontSize = 22.sp, modifier = Modifier.padding(end = 8.dp))
                Text(text = "Preciso de um abrigo", fontSize = 20.sp, color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .background(Color.Red, shape = RoundedCornerShape(18.dp))
                .padding(horizontal = 15.dp, vertical = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "💬", fontSize = 22.sp, modifier = Modifier.padding(end = 8.dp))
                Text(text = "Quero apoio psicológico", fontSize = 20.sp, color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .background(Color.Red, shape = RoundedCornerShape(18.dp))
                .padding(horizontal = 15.dp, vertical = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🚨", fontSize = 22.sp, modifier = Modifier.padding(end = 8.dp))
                Text(text = "Preciso denunciar", fontSize = 20.sp, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(24.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = message, // Usa o estado vindo do parâmetro
                onValueChange = onMessageChange, // Usa a função vinda do parâmetro
                placeholder = { Text("Digite sua mensagem...", color = Color.Gray) },
                // SOLUÇÃO 3: Usando TextFieldDefaults.colors do Material 3
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black,
                    focusedTextColor = Color.Black
                ),
                modifier = Modifier.weight(1f),
                singleLine = true
            )

            IconButton(onClick = {
                // SOLUÇÃO 2: Chama a função onSendMessage, que agora é um parâmetro
                if (message.isNotBlank()) {
                    onSendMessage()
                }
            }) {
                Icon(
                    // SOLUÇÃO 1 e 4: Usando o ícone corrigido e sem ambiguidade
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Enviar",
                    tint = Color.Black,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatbotScreenPreview() {
    // Para o Preview funcionar, criamos um estado local "falso"
    var message by remember { mutableStateOf("") }

    ChatbotScreen(
        message = message,
        onMessageChange = { newMessage -> message = newMessage },
        onSendMessage = {
            // Lógica de envio (no preview, podemos apenas limpar a mensagem)
            message = ""
        }
    )
}