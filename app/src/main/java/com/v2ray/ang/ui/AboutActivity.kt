package com.v2ray.ang.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.v2ray.ang.BuildConfig
import com.v2ray.ang.R
import com.v2ray.ang.core.CoreNativeManager
import com.v2ray.ang.ui.base.BaseComponentActivity
import com.v2ray.ang.ui.compose.AppTopBar
import com.v2ray.ang.ui.compose.NavigationBarsSpacer
import com.v2ray.ang.ui.compose.SettingsMenuItem

class AboutActivity : BaseComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @Composable
    override fun ScreenContent() {
        SeekdeepAboutScreen(onBackClick = { finish() })
    }
}

@Composable
private fun SeekdeepAboutScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val version = "v${BuildConfig.VERSION_NAME} (${CoreNativeManager.getLibVersion()})"

    fun open(url: String) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (_: Exception) {
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            AppTopBar(
                title = "About Seekdeep",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.shen_brand_icon),
                contentDescription = "Seekdeep",
                modifier = Modifier
                    .padding(top = 28.dp, bottom = 10.dp)
                    .size(110.dp)
            )
            Text(
                text = "Seekdeep",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Exclusive SHΞN™ made",
                color = Color(0xFFFF7A00),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = version,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 22.dp)
            )

            SettingsMenuItem(
                icon = painterResource(R.drawable.ic_source_code_24dp),
                title = "Github: aishervin",
                subtitle = "github.com/aishervin",
                onClick = { open("https://github.com/aishervin") }
            )
            SettingsMenuItem(
                icon = painterResource(R.drawable.ic_telegram_24dp),
                title = "Telegram: shervini",
                subtitle = "t.me/shervini",
                onClick = { open("https://t.me/shervini") }
            )
            SettingsMenuItem(
                icon = painterResource(R.drawable.ic_telegram_24dp),
                title = "T Channel",
                subtitle = "telegramer.pages.dev",
                onClick = { open("https://telegramer.pages.dev") }
            )
            SettingsMenuItem(
                icon = painterResource(R.drawable.ic_source_code_24dp),
                title = "X: shervinonx",
                subtitle = "x.com/shervinonx",
                onClick = { open("https://x.com/shervinonx") }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 22.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Developed by ☬SHΞN™",
                    color = Color(0xFFFF7A00),
                    style = MaterialTheme.typography.labelLarge
                )
            }
            NavigationBarsSpacer()
        }
    }
}
