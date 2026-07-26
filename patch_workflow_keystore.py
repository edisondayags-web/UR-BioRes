path = ".github/workflows/build.yml"
with open(path, "r", encoding="utf-8") as f:
    content = f.read()

old = '''      - name: Build debug APK
        run: ./gradlew assembleDebug --no-daemon'''

new = '''      - name: Generate debug keystore
        run: |
          keytool -genkey -v -keystore app/debug.keystore -storepass android \\
            -alias androiddebugkey -keypass android -keyalg RSA -keysize 2048 \\
            -validity 10000 -dname "CN=Android Debug,O=Android,C=US"

      - name: Build debug APK
        run: ./gradlew assembleDebug --no-daemon'''

assert old in content, "OLD NOT FOUND"
content = content.replace(old, new, 1)

with open(path, "w", encoding="utf-8") as f:
    f.write(content)
print("DONE")
