<div align="center">
  <img src="app/src/main/ic_launcher-web.png" width="150" alt="Xrayng Logo">
  <h1 style="color: #FFA500;">Xrayng (SHΞN™)</h1>
  <p><b> کلاینت اختصاصی ، ارتقا یافته و بهینه‌شده بر پایه v2rayNG</b></p>
</div>

<p align="center">
  <a href="https://github.com/aishervin/xrayng/releases/latest">
    <img src="https://img.shields.io/github/v/release/aishervin/xrayng?color=FFA500&label=Latest%20Release&style=for-the-badge" alt="Latest Release">
  </a>
  <a href="https://github.com/aishervin/xrayng/releases/latest">
    <img src="https://img.shields.io/github/downloads/aishervin/xrayng/total?color=FF8C00&style=for-the-badge" alt="Downloads">
  </a>
</p>

<img src="https://user-images.githubusercontent.com/73097560/115834477-dbab4500-a447-11eb-908a-139a6edaec5c.gif"><br><br>


## 🌟 معرفی پروژه
پروژه **Xrayng** یک نسخه ارتقا یافته و سفارشی‌سازی شده از کلاینت محبوب `v2rayNG` است. هدف ما در این پروژه، رفع محدودیت‌ها، بهبود رابط کاربری و اضافه کردن قابلیت‌های کاربردی است که جای خالی آن‌ها به شدت در نسخه اصلی حس می‌شد.

<img src="assets/neon_line.svg" width="100%" height="15">

## 🚀 فاز اول: امکانات فعلی (انجام شده)
در نسخه فعلی (فاز اول)، تمرکز ما بر روی بهبود تجربه کاربری و دسترسی سریع‌تر به اینترنت آزاد بوده است:
*   ✅ **حل مشکل پراکسی‌های HTTP و SOCKS:** انتقال و ایمپورت دسته‌ای (Batch Import) پراکسی‌های SOCKS و HTTP که در نسخه اصلی با مشکل مواجه بود، کاملاً برطرف شده است.
*   ✅ **منابع پیش‌فرض غنی:** اضافه شدن دو مخزن (Subscription) قدرتمند و غنی به صورت درون‌برنامه‌ای، تا کاربران بدون نیاز به جستجو، به سرورهای باکیفیت دسترسی داشته باشند.
*   ✅ **تست پینگ پارالل:** افزایش دوبرابری سرعت تست url کانفیگها با استراتژی اختصاصی.

<img src="assets/neon_line.svg" width="100%" height="15">

## 🚧 فاز دوم: نقشه‌راه (آینده)
در فاز دوم، پروژه وارد مرحله تخصصی **MTProxy** خواهد شد:
*   🔍 **دریافت و تست سلامت:** استخراج پروکسی‌های MTProto از منابع معتبر و تست سلامت و پینگ آن‌ها به صورت خودکار.
*   ✈️ **پراکسی مستقیم تلگرام:** اعمال پراکسی‌های سالم به صورت مستقیم روی کلاینت تلگرام از داخل خود برنامه.
*   🔗 **اشتراک‌گذاری هوشمند:** امکان ارسال و اشتراک‌گذاری مستقیم لیست پراکسی‌های سالم در محیط تلگرام برای سایرین.

<img src="assets/neon_line.svg" width="100%" height="15">

## 🛡️ امنیت و هشدار Google Play Protect
ممکن است در هنگام نصب فایل APK با خطای **"Unsafe App"** یا مسدودسازی موقت توسط **Google Play Protect** مواجه شوید. 
**چرا این اتفاق می‌افتد؟** این هشدار کاملاً طبیعی و غیرقابل اجتناب است؛ زیرا این اپلیکیشن هنوز در گوگل پلی منتشر نشده و با کلیدهای توسعه‌دهنده اختصاصی (Custom Keystore) بیلد و امضا (Sign) شده است. سپر ایمنی گوگل (Play Protect) برنامه‌های خارج از استور خود را به صورت پیش‌فرض ناشناس تلقی کرده و این اخطار را نمایش می‌دهد.

**آیا جای نگرانی است؟** به هیچ وجه! این پروژه کاملاً **منبع‌باز (Open-Source)** است. برای اطمینان صد درصدی از عدم وجود هرگونه کد مخرب، می‌توانید لینک مخزن گیت‌هاب ما را مستقیماً به هوش مصنوعی (مثل ChatGPT یا DeepSeek) بدهید تا سورس کد را بررسی و امنیت آن را برای شما تضمین کند.

### 🤖 بررسی امنیت مخزن توسط هوش مصنوعی
با کلیک روی دکمه‌های زیر، می‌توانید یک درخواست (Prompt) آماده را مستقیماً برای هوش مصنوعی بفرستید تا سورس این مخزن را بررسی کند:
<p align="center">
<a href="https://chatgpt.com/?q=Please+analyze+this+open-source+GitHub+repository+for+any+malicious+code,+backdoors,+or+security+threats:+https://github.com/aishervin/xrayng" target="_blank"><img src="https://img.shields.io/badge/Audit_with-ChatGPT-10a37f?style=for-the-badge&logo=openai&logoColor=white" alt="ChatGPT"></a>
<a href="https://chat.deepseek.com/" target="_blank"><img src="https://img.shields.io/badge/Audit_with-DeepSeek-4d6bfe?style=for-the-badge&logo=deepseek&logoColor=white" alt="DeepSeek"></a>

*(نکته: در دیپ‌سیک، متن زیر را به صورت دستی کپی کرده و در چت ارسال کنید)*
> `Please analyze this open-source Android project for any malicious code or security threats: https://github.com/aishervin/xrayng`

<img src="https://user-images.githubusercontent.com/73097560/115834477-dbab4500-a447-11eb-908a-139a6edaec5c.gif"><br><br>


## 📥 دانلود و نصب
برای دریافت آخرین نسخه پایدار، همیشه از بخش Releases در گیت‌هاب استفاده کنید:
<br>
<p align="center">
  <a href="https://github.com/aishervin/xrayng/releases/latest">
    <img src="https://img.shields.io/badge/Download_Latest_APK-FFA500?style=for-the-badge&logo=android&logoColor=black&scale=1.5" alt="Download APK">
  </a>
</p>
<br>

<img src="https://user-images.githubusercontent.com/73097560/115834477-dbab4500-a447-11eb-908a-139a6edaec5c.gif"><br><br>

<p align="center"><b> ☬Exclusive SHΞN™ made | Xrayng</b></p>

[<img src="https://cdn.iconscout.com/icon/free/png-256/kotlin-283155.png" alt="kotlin" width="100">](https://kotlinlang.org/docs/home.html)
[<img src="https://cdn.iconscout.com/icon/free/png-256/android-3521272-2944776.png" alt="android" width="100">](https://developer.android.com/reference)
