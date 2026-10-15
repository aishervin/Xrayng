package com.v2ray.ang.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun <T> AppDropdownMenuItems(
    items: List<T>,
    labelRes: (T) -> Int,
    iconRes: ((T) -> Int)? = null,
    isPrimary: ((T) -> Boolean)? = null,
    badgeText: ((T) -> String?)? = null,
    onSelected: (T) -> Unit
) {
    items.forEach { item ->
        val primary = isPrimary?.invoke(item) == true
        val icon = iconRes?.invoke(item)?.takeIf { it != 0 }
        val badge = badgeText?.invoke(item)

        val itemModifier = if (primary) {
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 2.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0x33FF7A00), Color(0x12FF9100), Color(0x05FFFFFF))
                    ),
                    RoundedCornerShape(10.dp)
                )
                .border(
                    0.9.dp,
                    Brush.horizontalGradient(listOf(Color(0xFFFF7A00), Color(0x44FFA040))),
                    RoundedCornerShape(10.dp)
                )
        } else {
            Modifier.fillMaxWidth()
        }

        DropdownMenuItem(
            modifier = itemModifier,
            leadingIcon = if (icon != null) {
                {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = if (primary) Color(0xFFFF7A00) else Color(0xFFA2A8BA)
                    )
                }
            } else null,
            text = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (primary) {
                        Text(
                            text = stringResource(labelRes(item)),
                            style = TextStyle(
                                brush = NeonOrangeBrush,
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.3.sp
                            )
                        )
                    } else {
                        Text(
                            text = stringResource(labelRes(item)),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }

                    if (badge != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .background(Color(0x29FF7A00), RoundedCornerShape(6.dp))
                                .border(0.7.dp, Color(0x88FF7A00), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = badge,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFFF9433),
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            },
            onClick = { onSelected(item) }
        )
    }
}
