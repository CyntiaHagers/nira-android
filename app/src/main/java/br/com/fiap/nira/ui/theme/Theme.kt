package br.com.fiap.nira.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush

// Esquema de cores (pode ajustar conforme evoluir)
private val NiraColorScheme = lightColorScheme(
    primary = PurpleGradient,
    onPrimary = White,
    secondary = PinkGradient,
    onSecondary = White,
    tertiary = OrangePrimary,
    onTertiary = White,
    background = PurpleGradient,
    onBackground = White,
    surface = PurpleGradient,
    onSurface = White
)

/**
 * NiraTheme aplica o MaterialTheme **e** já injeta um fundo de degradê
 * vertical (roxo -> rosa) para toda a UI.
 *
 * Se alguma tela precisar de fundo próprio, basta colocar um `Surface`/`Box`
 * com outro background por cima.
 */
@Composable
fun NiraTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NiraColorScheme
        // Tipografia/padrões podem ser adicionados aqui se desejar
    ) {
        // Fundo global em degradê
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(PurpleGradient, PinkGradient)
                    )
                )
        ) {
            content()
        }
    }
}
