path = "app/src/main/java/com/saltech/urdocs/ui/screens/LetterAssistantScreen.kt"
with open(path, "r", encoding="utf-8") as f:
    content = f.read()

old = '''    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {'''
new = '''    Box(modifier = Modifier.fillMaxSize()) {
    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {'''
assert content.count(old) == 1, "OLD COLUMN NOT FOUND OR NOT UNIQUE"
content = content.replace(old, new, 1)

old_item_block = '''            if (isTyping) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AssistantAvatar()
                        Spacer(Modifier.width(10.dp))
                        Box(
                            modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(UrBubbleDark).padding(14.dp)
                        ) {
                            ThinkingIndicator()
                        }
                    }
                }
            }'''
assert old_item_block in content, "OLD ITEM BLOCK NOT FOUND"
content = content.replace(old_item_block, "", 1)

old_send_row_end = '''            Box(
                modifier = Modifier.size(38.dp).clip(CircleShape).background(UrPink)
                    .clickable(enabled = inputText.isNotBlank() && !isTyping) { handleUserInput(inputText) },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(18.dp))
            }
        }
    }
}'''
new_send_row_end = '''            Box(
                modifier = Modifier.size(38.dp).clip(CircleShape).background(UrPink)
                    .clickable(enabled = inputText.isNotBlank() && !isTyping) { handleUserInput(inputText) },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(18.dp))
            }
        }
    }

    if (isTyping) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.85f)),
            contentAlignment = Alignment.Center
        ) {
            ThinkingIndicator()
        }
    }
    }
}'''
assert old_send_row_end in content, "SEND ROW END NOT FOUND"
content = content.replace(old_send_row_end, new_send_row_end, 1)

with open(path, "w", encoding="utf-8") as f:
    f.write(content)
print("DONE")
