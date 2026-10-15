# XrayNG v2.0.0 — Modernized UI, Multi-Source Subscriptions & PRO Restrictions

This release modernizes the primary interface with an ergonomic bottom test action bar, integrates dedicated Xray, WireGuard, Https/Sucks, and Iranian community Telegram proxy subscriptions with live health indicators, introduces PRO sharing restrictions on built-in configs, and prepares ABI-split release packages for v2.0.0.

---

### User Review & Critical Decisions

> [!IMPORTANT]
> The following requirements have been confirmed based on your inputs and will be fully implemented:

- **Confirmed Decision 1 (PRO Restriction Behavior)**: Built-in configurations in default groups (`Xray`, `Wireguard`) are protected against copying and sharing within the app. Their share action will display a "PRO" badge and be disabled; tapping it triggers the notification:
  > *«امکان بازنشر کانفیگهای درونبرنامه ای در نسخه ی Xrayng PRO در دسترس خواهد بود»*
  User-added configs (imported manually or via custom subscriptions) remain fully shareable.
- **Confirmed Decision 2 (Bottom Test Action Bar Layout)**: The test buttons are relocated from the top menu/header to the bottom of the screen (anchored above the bottom connection bar) with a side-by-side horizontal arrangement:
  - **2/3 Width**: **Real delay** (URL Ping test) with high-visibility latency styling.
  - **1/3 Width**: **Gemini pass** with the authentic Google Gemini icon and dedicated neon-cyan/gemini styling.
- **Confirmed Decision 3 (Subscriptions & Auto-Update)**:
  - **Xray**: Default VLESS source set to `https://raw.githubusercontent.com/aishervin/V2flair/refs/heads/main/clean_sub.txt` under group title `Xray`.
  - **Wireguard**: Multi-config source added from `https://raw.githubusercontent.com/aishervin/V2flair/refs/heads/main/xrayng.txt`. The parser splits multiple `[Interface]` blocks into individual profiles under group title `Wireguard`.
  - **Https/Sucks**: Existing HTTP/SOCKS proxy group renamed to `Https/Sucks`.
  - **Telegram proxy**: Iranian community verified proxy source added (`https://raw.githubusercontent.com/SoliSpirit/mtproto/master/all_proxies.txt` / MTProto & SOCKS5), with blue **«Telegram pass»** indicator for responsive proxies.
  - **Galaxy Conversion**: SOCKS5 proxies from `Https/Sucks` are automatically converted to Telegram Galaxy link format (`tg://socks?...`) and made available in the Telegram proxy collection.
  - **Auto-Update**: All subscriptions set to auto-update automatically every 1 hour (60 minutes).
- **Confirmed Decision 4 (Build & Release)**: Version bumped to `v2.0.0` (version code incremented) with release APK generation split by architecture (`arm64-v8a`, `armeabi-v7a`, `x86_64`, `x86`, and universal).

---

### 1. Overview & Core Concept

- **What It Does**: Enhances XrayNG with specialized proxy categories, streamlined two-speed testing (Real delay + Gemini Pass) conveniently located for one-thumb reach, automated Telegram proxy harvesting with live verification, and commercial-tier PRO protection for built-in configs.
- **Target Audience / Persona**: Power users, freedom-of-information seekers, and Iranian community members needing resilient, multi-protocol connectivity (Xray, WireGuard, MTProto, SOCKS5) with immediate visual feedback on which servers pass strict DPI inspection and Google Gemini access.
- **Key Value**: One-tap speed and censorship verification directly at your fingertips without digging into nested menus, plus automatic sync and format adaptation for Telegram.

---

### 2. User Experience & Visual Design

#### Bottom Test Action Bar
- **Positioning**: Fixed right above the bottom connection bar, spanning full screen width with 12dp horizontal padding.
- **Layout Ratio**: `Row(Modifier.fillMaxWidth())` where the Real delay button takes `Modifier.weight(2f)` (66.6%) and the Gemini Pass button takes `Modifier.weight(1f)` (33.3%).
- **Aesthetic Distinction**:
  - **Real Delay (2/3)**: Sleek dark surface with warm amber/orange accent border (`Color(0xFFFF7A00)`), displaying speed/tachometer icon, "Real delay" label, and batch test progress.
  - **Gemini Pass (1/3)**: Google Gemini authentic 4-point sparkle gradient icon, cyan/indigo highlight border (`Color(0xFF4E82EE)` to `Color(0xFF9B72CB)`), and "Gemini pass" label.
- **Card Badges in Server List**:
  - Passed URL test: Displays latency in green/amber (e.g. `142ms`).
  - Passed Gemini test: Displays `✨ Gemini passed` badge in Google blue.
  - Passed Telegram test: Displays `Telegram pass` badge in distinct Telegram Azure blue (`#0088CC`).
  - Default Built-in Configs: Displays subtle `PRO` pill badge on the share button. Clicking displays the Persian upgrade notification.

---

### 3. Key Product Decisions & Trade-Offs

- **Multi-block WireGuard Parser**: Standard v2rayNG only parsed single WireGuard `.conf` files. We enhance `AngConfigManager.parseCustomConfigServer` and `WireguardFmt` to detect multiple `[Interface]` blocks, parse each section independently, generate identifiable remarks (e.g. `Wireguard #1 - <endpoint>`), and commit them cleanly into the `Wireguard` group.
- **Telegram Proxy Testing Engine**: For Telegram proxies (MTProto and SOCKS5), the app verifies socket reachability or HTTP/TLS handshake to the proxy endpoint, setting a `telegramPassed` flag in `ServerAffiliationInfo` so the blue badge reflects real-time status.
- **SOCKS5 to Telegram Galaxy Conversion**: When `Https/Sucks` is loaded or updated, extracted SOCKS5 endpoints (`socks5://user:pass@host:port`) are mirrored into `tg://socks?server=host&port=port&user=user&pass=pass` format and populated into the Telegram proxy group.
- **Zero Accidental Leaks (PRO Restriction)**: Guard added directly into `MainScreen` / `ShareMethodDialog` / `MainServerPager`: any server profile belonging to the built-in `Xray` or `Wireguard` subscriptions intercept export/share clicks and display the required PRO toast.

---

### 4. Technical Architecture & System Flow

```
┌─────────────────────────────────────────────────────────────┐
│                      XrayNG UI Layer                         │
│  ┌───────────────────────┐       ┌───────────────────────┐  │
│  │    MainServerPager    │       │     MainBottomBar     │  │
│  │ (Server list & badges)│       │  (Connection toggle)  │  │
│  └───────────┬───────────┘       └───────────┬───────────┘  │
│              │                               │              │
│              ▼                               ▼              │
│  ┌───────────────────────────────────────────────────────┐  │
│  │   Bottom Test Action Bar (Above BottomBar)            │  │
│  │   [ 2/3 Real Delay (URL) ] | [ 1/3 Gemini Pass (AI) ]  │  │
│  └───────────────────────────────────────────────────────┘  │
└──────────────────────────────┬──────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                 Engine & Subscription Handlers              │
│  ┌────────────────────────────────────────────────────────┐ │
│  │ SettingsManager & AppConfig                            │ │
│  │  - Xray (clean_sub.txt)                                │ │
│  │  - Wireguard (xrayng.txt multi-block INI parser)       │ │
│  │  - Https/Sucks (ProxyNg JSON)                          │ │
│  │  - Telegram Proxy (SoliSpirit MTProto + SOCKS5)        │ │
│  │  - Auto-Update: 60 Minutes Worker                      │ │
│  └────────────────────────────────────────────────────────┘ │
│  ┌────────────────────────────────────────────────────────┐ │
│  │ Test & Share Protection                                │ │
│  │  - CoreTestService (URL ping, Gemini test, TG ping)    │ │
│  │  - PRO Share Guard: Persian toast for built-ins        │ │
│  └────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

---

### Verification & Delivery Plan

1. **Architecture & Parsing Verification**:
   - Verify WireGuard multi-section parsing via automated test.
   - Verify Telegram proxy parsing and SOCKS5 format conversion.
2. **UI & Layout Verification**:
   - Validate bottom test action bar sizing, 2/3 and 1/3 weights, and Gemini/URL distinct styling.
   - Verify PRO click behavior and message display.
3. **Compilation & Packaging**:
   - Update `versionCode` and `versionName` to `v2.0.0` in `build.gradle.kts`.
   - Run compilation check with `compile_applet`.
   - Run Gradle release build tasks generating ABI-split APKs for all target architectures.
