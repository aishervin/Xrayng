package com.v2ray.ang.ui.main

import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.v2ray.ang.R
import com.v2ray.ang.ui.compose.AppDivider
import com.v2ray.ang.ui.compose.verticalScrollbar
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import com.v2ray.ang.ui.compose.NeonOrangeBrush
import com.v2ray.ang.ui.compose.SilverMetallicBrush


enum class MainDestination(@DrawableRes val iconRes: Int, @StringRes val labelRes: Int) {
    Subscriptions(R.drawable.ic_subscriptions_24dp, R.string.title_sub_setting),
    PerAppProxy(R.drawable.ic_per_apps_24dp, R.string.per_app_proxy_settings),
    Routing(R.drawable.ic_routing_24dp, R.string.routing_settings_title),
    UserAssets(R.drawable.ic_file_24dp, R.string.title_user_asset_setting),
    Settings(R.drawable.ic_settings_24dp, R.string.title_settings),
    Logcat(R.drawable.ic_logcat_24dp, R.string.title_logcat),
    CheckUpdate(R.drawable.ic_check_update_24dp, R.string.update_check_for_update),
    BackupRestore(R.drawable.ic_restore_24dp, R.string.title_configuration_backup_restore),
    About(R.drawable.ic_about_24dp, R.string.title_about)
}

private val primaryDrawerItems = listOf(
    MainDestination.Subscriptions,
    MainDestination.PerAppProxy,
    MainDestination.Routing,
    MainDestination.UserAssets,
    MainDestination.Settings
)

private val drawerItems = primaryDrawerItems + listOf(
    MainDestination.Logcat,
    MainDestination.CheckUpdate,
    MainDestination.BackupRestore,
    MainDestination.About
)



@Composable
fun MainDrawerContent(drawerState: DrawerState, onNavigate: (MainDestination) -> Unit) {
    val drawerScrollState = rememberScrollState()
    val context = LocalContext.current

    ModalDrawerSheet(
        drawerState = drawerState,
        modifier = Modifier
            .fillMaxWidth(0.80f)
            .border(
                1.dp,
                Brush.verticalGradient(listOf(Color(0x88FF7A00), Color(0x33A2A8BA), Color(0x22FF7A00))),
                RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)
            ),
        drawerContainerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(drawerScrollState)
                    .verticalScrollbar(drawerScrollState)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF1F222C), Color(0xFF14161E), Color(0xFF0F1116))
                            )
                        )
                        .padding(vertical = 24.dp, horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(92.dp)
                                .background(Color(0x22FF7A00), CircleShape)
                                .border(
                                    1.2.dp,
                                    Brush.linearGradient(
                                        listOf(Color(0xFFFF7A00), Color(0x44A2A8BA), Color(0xFFFF9100))
                                    ),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.shen_brand_icon),
                                contentDescription = null,
                                modifier = Modifier.size(76.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Xrayng",
                            style = TextStyle(
                                brush = SilverMetallicBrush,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.2.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "SHΞN™ PRO CORE",
                            style = TextStyle(
                                brush = NeonOrangeBrush,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp
                            )
                        )
                    }
                }
                drawerItems.forEachIndexed { index, item ->
                    if (index == primaryDrawerItems.size) AppDivider()
                    NavigationDrawerItem(
                        label = {
                            Text(
                                stringResource(item.labelRes),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        },
                        selected = false,
                        onClick = { onNavigate(item) },
                        icon = {
                            Icon(
                                painter = painterResource(item.iconRes),
                                contentDescription = null,
                                tint = Color(0xFFFF7A00),
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }

            AppDivider()
            val footerText = "Exclusive SHΞN™ made"
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        role = Role.Button
                        contentDescription = "$footerText - t.me/shervini"
                    }
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/shervini")).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        try {
                            context.startActivity(intent)
                        } catch (_: Exception) {
                        }
                    }
                    .padding(horizontal = 16.dp, vertical = 14.dp)
                    .navigationBarsPadding(),
                color = Color.Transparent
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = footerText,
                        style = TextStyle(
                            brush = NeonOrangeBrush,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                    )
                }
            }
        }
    }
}
