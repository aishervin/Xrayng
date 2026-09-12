with open(".github/workflows/build.yml", "r") as f:
    text = f.read()

import re
old_text = '''      - name: Upload APKs to release
        uses: svenstaro/upload-release-action@v2
        with:
          file: release/**/*.apk
          tag: ${{ inputs.release_tag }}
          file_glob: true
          prerelease: false'''

new_text = '''      - name: Upload APKs to release
        uses: softprops/action-gh-release@v2
        with:
          files: release/**/*.apk
          tag_name: ${{ inputs.release_tag }}
          prerelease: false'''

text = text.replace(old_text, new_text)

with open(".github/workflows/build.yml", "w") as f:
    f.write(text)
