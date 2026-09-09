# ShenBypass

Shenbypass is an advanced V2Ray and proxy client for Android, built with Kotlin and Jetpack Compose.

## Features

- **Protocol Support**: Supports VMess, VLESS, Shadowsocks, Trojan, WireGuard, Hysteria2, SOCKS, and HTTP proxies.
- **Modern Jetpack Compose UI**: Complete Material Design 3 interface with server list, drawer navigation, group tabs, and dark/light mode themes.
- **Subscription Management**: Import and manage subscription links, update configs on demand or on a schedule, with QR code sharing and scanning.
- **Flexible Routing**: Custom routing rules (Blacklist, Whitelist, Global, and custom domain/IP rules), with Per-App Proxy configuration.
- **Network Tools & Diagnostics**: Real ping testing, speed testing, logcat viewer, connection status indicators, and quick-settings tile integration.
- **Backup & Restore**: Easily backup configurations to local storage or WebDAV and restore on another device.

## Tech Stack

- **Platform**: Android SDK 37 (Min SDK 24)
- **Language**: Kotlin 2.4+
- **UI Toolkit**: Jetpack Compose & Material 3
- **State & Storage**: MMKV, ViewModel, Coroutines, StateFlow
- **Camera & Scanning**: CameraX + ZXing
- **Build System**: Gradle (Kotlin DSL) with Version Catalog
