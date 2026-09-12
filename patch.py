with open("app/src/main/java/com/v2ray/ang/ui/main/MainViewModel.kt", "r") as f:
    text = f.read()

import re
old = '''        when (action) {
            MainAction.Initialize -> initialize()
            MainAction.RefreshGroups -> setupGroupTab(forceRefresh = true)'''

new = '''        when (action) {
            MainAction.ExportTested -> {} // Handled in MainActivity
            MainAction.Initialize -> initialize()
            MainAction.RefreshGroups -> setupGroupTab(forceRefresh = true)'''

if old in text:
    text = text.replace(old, new)
    with open("app/src/main/java/com/v2ray/ang/ui/main/MainViewModel.kt", "w") as f:
        f.write(text)
    print("Patched MainViewModel.kt")
else:
    print("Old string not found")
