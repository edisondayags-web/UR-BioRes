path = "app/src/main/java/com/saltech/urdocs/ui/screens/LettersScreen.kt"
with open(path, "r", encoding="utf-8") as f:
    content = f.read()

old = '''                "all" -> AllTemplatesContent(
                    onBack = { screenState = "hub" },
                    onPick = { type -> selectedType = type; screenState = "form" }
                )'''
new = '''                "all" -> AllTemplatesContent(
                    onBack = { screenState = "hub" },
                    onPick = { type -> onNavigate(com.saltech.urdocs.navigation.Screen.LetterAssistant.createRoute(type.name)) }
                )'''
assert old in content, "OLD NOT FOUND"
content = content.replace(old, new, 1)

with open(path, "w", encoding="utf-8") as f:
    f.write(content)
print("DONE")
