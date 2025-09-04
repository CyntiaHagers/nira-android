package br.com.fiap.nira.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.fiap.nira.ui.theme.NiraTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.nira.R

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

}

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x6F7939A4))
    ) {
        // logo no topo
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 36.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_nira_app),
                contentDescription = "image description",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(101.dp)
                    .height(39.dp)
            )
        }

        // texto no topo
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp),
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

        // organização dos botões centrais
        Column (
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp)

        )
        {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Emergência imediata")
            }
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow)
            ) {
                Text("Encontrar Abrigo")
            }
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow)
            ) {
                Text("Chatbot de ajuda")
            }
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
        ) {

        }
    }
}

@Composable
@Preview
fun HomeScreenPreview() {
    NiraTheme {
        HomeScreen()
    }
}