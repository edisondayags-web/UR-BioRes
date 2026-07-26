path = "app/src/main/java/com/saltech/urdocs/ui/screens/LetterAssistantScreen.kt"
with open(path, "r", encoding="utf-8") as f:
    content = f.read()

old = '''    // No manual scroll needed -- reverseLayout keeps the list anchored to the latest message automatically

    Box(modifier = Modifier.fillMaxSize()) {'''

new = '''    LaunchedEffect(messages.size, isTyping) {
        val lastIndex = messages.size - 1 + if (isTyping) 1 else 0
        if (lastIndex >= 0) {
            listState.animateScrollToItem(lastIndex)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {'''

assert old in content, "BLOCK1 NOT FOUND - screen may already be patched"
content = content.replace(old, new, 1)

old2 = '''        // ---------- Chat list, anchored to the bottom automatically via reverseLayout ----------
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 110.dp, bottom = 72.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp, Alignment.Bottom),
            reverseLayout = true
        ) {
            if (isTyping) {
                item { ThinkingBubble() }
            }
            items(messages.reversed(), key = { it.time + it.text.hashCode() }) { msg ->
                var visible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) { visible = true }
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(animationSpec = tween(400, easing = LinearOutSlowInEasing))
                ) {
                    ChatBubble(msg)
                }
            }
        }'''

new2 = '''        // ---------- Chat list, grows from the top like a normal conversation, auto-scrolls as it overflows ----------
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 110.dp, bottom = 72.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(messages, key = { it.time + it.text.hashCode() }) { msg ->
                var visible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) { visible = true }
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(animationSpec = tween(400, easing = LinearOutSlowInEasing))
                ) {
                    ChatBubble(msg)
                }
            }
            if (isTyping) {
                item { ThinkingBubble() }
            }
        }'''

assert old2 in content, "BLOCK2 NOT FOUND"
content = content.replace(old2, new2, 1)

with open(path, "w", encoding="utf-8") as f:
    f.write(content)
print("DONE")
