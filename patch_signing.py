path = "app/build.gradle.kts"
with open(path, "r", encoding="utf-8") as f:
    content = f.read()

old = '''    buildTypes {
        release {'''

new = '''    signingConfigs {
        getByName("debug") {
            storeFile = file("debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
        }
        release {'''

assert old in content, "OLD NOT FOUND"
content = content.replace(old, new, 1)

with open(path, "w", encoding="utf-8") as f:
    f.write(content)
print("DONE")
