package br.com.fiap.nira

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import br.com.fiap.nira.navigation.AppNavHost
import br.com.fiap.nira.ui.theme.NiraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // (Opcional) desenhar por trás das barras do sistema
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            NiraTheme {
                // 👉 Aqui entra sua navegação (Login é a inicial no NavGraph)
                AppNavHost()
            }
        }
    }
}
