package br.com.fiap.nira.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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

// Definição de Cores para fácil reutilização
val PurpleStart = Color(0xFF4A00E0)
val PurpleEnd = Color(0xFF8E2DE2)
val DarkPurple = Color(0xFF4A00E0)
val ButtonYellow = Color(0xFFFCA400)
val TextWhite = Color.White

// Modelo de dados para um contato
data class Contact(val name: String, val phone: String)

@Composable
fun ContactsScreen() {
    val contacts = remember {
        mutableStateListOf(
            Contact("Ana", "(11) 98765-4321"),
            Contact("João", "(11) 98765-4321"),
            Contact("Laura", "(11) 98765-4321")
        )
    }

    val showDialog = remember { mutableStateOf(false) }

    fun onRemoveContact(contact: Contact) {
        contacts.remove(contact)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(PurpleStart, PurpleEnd)
                )
            )
    ) {
        Scaffold(
            topBar = { TopBar() },
            // bottomBar = { BottomNavBar() },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Contatos de confiança",
                    color = TextWhite,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.dongle_regular)),
                    modifier = Modifier
                        .padding(vertical = 28.dp)
                )

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(contacts) { contact ->
                        ContactItem(contact = contact, onRemove = { onRemoveContact(contact) })
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { showDialog.value = true },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFFFFF)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Adicionar Contato",
                        tint = Color.Black
                    )
                    Text(
                        text = "Adicionar Contato",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.dongle_regular))
                        ),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                DefaultMessage()

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { /* Ação de enviar localização */ },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF39E0F)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Enviar localização agora",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        modifier = Modifier.padding(vertical = 10.dp),
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.dongle_regular))
                        ),
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    if (showDialog.value) {
        AddContactDialog(
            onAddContact = { name, phone ->
                contacts.add(Contact(name, phone))
                showDialog.value = false
            },
            onDismiss = { showDialog.value = false }
        )
    }
}

@Composable
fun TopBar() {
    Row(
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
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "",
            color = TextWhite,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ContactItem(contact: Contact, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = DarkPurple, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = contact.name,
                color = TextWhite,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.dongle_regular)),
                    fontSize = 34.sp
                )
            )
            Text(
                text = contact.phone,
                color = TextWhite,
                fontSize = 16.sp
            )
        }
        IconButton(
            onClick = onRemove,
            modifier = Modifier
                .size(32.dp)
                .background(Color.Red, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remover Contato",
                tint = TextWhite
            )
        }
    }
}

@Composable
fun DefaultMessage() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Mensagem padrão",
            color = TextWhite,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Preciso de ajuda. Minha localização é: https://maps.app.goo.gl/1234",
            color = TextWhite,
            fontSize = 14.sp
        )
    }
}

@Composable
fun BottomNavBar() {
    NavigationBar(
        containerColor = DarkPurple,
        contentColor = TextWhite
    ) {
        NavigationBarItem(
            selected = false,
            onClick = { /* Ação para Início */ },
            icon = { Icon(Icons.Default.Home, contentDescription = "Início") },
            label = { Text("Início") },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = TextWhite,
                unselectedTextColor = TextWhite,
                indicatorColor = DarkPurple
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* Ação para Mapa */ },
            icon = { Icon(Icons.Default.Map, contentDescription = "Mapa") },
            label = { Text("Mapa") },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = TextWhite,
                unselectedTextColor = TextWhite,
                indicatorColor = DarkPurple
            )
        )
        NavigationBarItem(
            selected = true,
            onClick = { /* Ação para Contatos */ },
            icon = { Icon(Icons.Default.Contacts, contentDescription = "Contatos") },
            label = { Text("Contatos") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ButtonYellow,
                selectedTextColor = ButtonYellow,
                indicatorColor = DarkPurple,
                unselectedIconColor = TextWhite,
                unselectedTextColor = TextWhite
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* Ação para Mais */ },
            icon = { Icon(Icons.Default.MoreHoriz, contentDescription = "Mais") },
            label = { Text("Mais") },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = TextWhite,
                unselectedTextColor = TextWhite,
                indicatorColor = DarkPurple
            )
        )
    }
}

@Composable
fun AddContactDialog(
    onAddContact: (name: String, phone: String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Adicionar Novo Contato",
                color = Color.Black
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black,
                        cursorColor = Color.Black
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Telefone") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black,
                        cursorColor = Color.Black
                    )
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank() && phone.isNotBlank()) {
                        onAddContact(name, phone)
                    }
                }
            ) {
                Text("Adicionar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        containerColor = Color.White
    )
}

@Preview(showBackground = true)
@Composable
fun ContactsScreenPreview() {
    ContactsScreen()
}