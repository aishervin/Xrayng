package com.v2ray.ang.ui.main

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.v2ray.ang.R
import com.v2ray.ang.dto.entities.ProfileItem
import com.v2ray.ang.enums.EConfigType
import com.v2ray.ang.extension.isComplexType
import com.v2ray.ang.handler.SettingsManager
import com.v2ray.ang.ui.compose.AppDropdownMenuItems
import com.v2ray.ang.ui.compose.SelectListDialog

private enum class ImportMenuAction(
    @StringRes val labelRes: Int,
    val action: MainAction,
    @DrawableRes val iconRes: Int
) {
    QRCode(R.string.menu_item_import_config_qrcode, MainAction.ImportQRcode, R.drawable.ic_scan_24dp),
    Clipboard(R.string.menu_item_import_config_clipboard, MainAction.ImportClipboard, R.drawable.ic_copy),
    LocalFile(R.string.menu_item_import_config_local, MainAction.ImportConfigLocal, R.drawable.ic_file_24dp),
    PolicyGroup(R.string.menu_item_import_config_policy_group, MainAction.ImportManually(EConfigType.POLICYGROUP.value), R.drawable.ic_add_24dp),
    ProxyChain(R.string.menu_item_import_config_proxy_chain, MainAction.ImportManually(EConfigType.PROXYCHAIN.value), R.drawable.ic_add_24dp),
    Vmess(R.string.menu_item_import_config_manually_vmess, MainAction.ImportManually(EConfigType.VMESS.value), R.drawable.ic_add_24dp),
    Vless(R.string.menu_item_import_config_manually_vless, MainAction.ImportManually(EConfigType.VLESS.value), R.drawable.ic_add_24dp),
    Shadowsocks(R.string.menu_item_import_config_manually_ss, MainAction.ImportManually(EConfigType.SHADOWSOCKS.value), R.drawable.ic_add_24dp),
    Socks(R.string.menu_item_import_config_manually_socks, MainAction.ImportManually(EConfigType.SOCKS.value), R.drawable.ic_add_24dp),
    Http(R.string.menu_item_import_config_manually_http, MainAction.ImportManually(EConfigType.HTTP.value), R.drawable.ic_add_24dp),
    Trojan(R.string.menu_item_import_config_manually_trojan, MainAction.ImportManually(EConfigType.TROJAN.value), R.drawable.ic_add_24dp),
    WireGuard(R.string.menu_item_import_config_manually_wireguard, MainAction.ImportManually(EConfigType.WIREGUARD.value), R.drawable.ic_add_24dp),
    Hysteria2(R.string.menu_item_import_config_manually_hysteria2, MainAction.ImportManually(EConfigType.HYSTERIA2.value), R.drawable.ic_add_24dp)
}

enum class MainMoreMenuAction(
    @StringRes val labelRes: Int,
    @DrawableRes val iconRes: Int,
    val isPrimary: Boolean = false,
    val badge: String? = null
) {
    TestAllRealPing(R.string.title_real_ping_all_server, R.drawable.ic_speed_24dp),
    TestGemini(R.string.title_test_gemini, R.drawable.ic_gemini_logo),
    TestAll(R.string.title_ping_all_server, R.drawable.ic_flash_on_24dp),
    UpdateSubscriptions(R.string.title_sub_update, R.drawable.ic_cloud_download_24dp),
    SortByTestResults(R.string.title_sort_by_test_results, R.drawable.ic_outline_filter_alt_24),
    LocateSelected(R.string.title_locate_selected_config, R.drawable.ic_search_24dp),
    ExportTested(R.string.title_export_tested, R.drawable.ic_share_24dp),
    ExportAll(R.string.title_export_all, R.drawable.ic_copy),
    RestartService(R.string.title_service_restart, R.drawable.ic_restore_24dp),
    DeleteInvalid(R.string.title_del_invalid_config, R.drawable.ic_delete_24dp),
    DeleteDuplicate(R.string.title_del_duplicate_config, R.drawable.ic_delete_24dp),
    DeleteAll(R.string.title_del_all_config, R.drawable.ic_delete_24dp)
}

internal enum class ServerMenuAction(
    @StringRes val labelRes: Int,
    val isShareAction: Boolean,
    val supportsComplexProfiles: Boolean,
) {
    ShareQRCode(R.string.share_method_qrcode, isShareAction = true, supportsComplexProfiles = false),
    ShareClipboard(R.string.share_method_clipboard, isShareAction = true, supportsComplexProfiles = false),
    ShareFullContent(R.string.share_method_full_content, isShareAction = true, supportsComplexProfiles = true),
    Edit(R.string.action_edit, isShareAction = false, supportsComplexProfiles = true),
    Delete(R.string.action_delete, isShareAction = false, supportsComplexProfiles = true),
}

internal fun serverMenuActions(
    isComplexProfile: Boolean,
    includeManagementActions: Boolean,
): List<ServerMenuAction> = ServerMenuAction.entries.filter { action ->
    (includeManagementActions || action.isShareAction) && (!isComplexProfile || action.supportsComplexProfiles)
}

@Composable
fun ImportMenuContent(onAction: (MainAction) -> Unit) = AppDropdownMenuItems(
    items = ImportMenuAction.entries,
    labelRes = { it.labelRes },
    iconRes = { it.iconRes },
    onSelected = { onAction(it.action) }
)

@Composable
fun MoreMenuContent(onSelected: (MainMoreMenuAction) -> Unit) = AppDropdownMenuItems(
    items = MainMoreMenuAction.entries,
    labelRes = { it.labelRes },
    iconRes = { it.iconRes },
    isPrimary = { it.isPrimary },
    badgeText = { it.badge },
    onSelected = onSelected
)

@Composable
fun ShareMethodDialog(
    guid: String,
    profile: ProfileItem,
    more: Boolean,
    onDismiss: () -> Unit,
    onAction: (MainAction) -> Unit,
    onRemove: (String) -> Unit,
) {
    val context = LocalContext.current
    val isPro = SettingsManager.isProRestricted(profile.subscriptionId)
    val menuActions = serverMenuActions(
        isComplexProfile = profile.configType.isComplexType(),
        includeManagementActions = more,
    )
    SelectListDialog(
        options = menuActions,
        optionText = { action ->
            val text = stringResource(action.labelRes)
            if (isPro && action.isShareAction) "$text [PRO]" else text
        },
        onSelected = { action ->
            onDismiss()
            if (isPro && action.isShareAction) {
                Toast.makeText(context, R.string.pro_share_restriction_notice, Toast.LENGTH_SHORT).show()
                return@SelectListDialog
            }
            when (action) {
                ServerMenuAction.ShareQRCode -> onAction(MainAction.ShareQRCode(guid))
                ServerMenuAction.ShareClipboard -> onAction(MainAction.ShareClipboard(guid))
                ServerMenuAction.ShareFullContent -> onAction(MainAction.ShareFullContent(guid))
                ServerMenuAction.Edit -> onAction(MainAction.EditServer(guid, profile))
                ServerMenuAction.Delete -> onRemove(guid)
            }
        },
        onDismiss = onDismiss
    )
}
