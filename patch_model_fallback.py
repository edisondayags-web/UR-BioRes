path = "app/src/main/java/com/saltech/urdocs/data/GeminiRepository.kt"
with open(path, "r", encoding="utf-8") as f:
    content = f.read()

old_line = '    private val model = "qwen/qwen-2.5-72b-instruct:free"'
new_line = '''    private val modelFallbacks = listOf(
        "openai/gpt-oss-20b:free",
        "openai/gpt-oss-120b:free",
        "meta-llama/llama-3.3-70b-instruct:free",
        "openrouter/free"
    )'''
assert old_line in content, "OLD MODEL LINE NOT FOUND"
content = content.replace(old_line, new_line, 1)

old_body = '''        val body = JSONObject().apply {
            put("model", model)
            put("messages", messages)
        }'''
new_body = '''        val body = JSONObject().apply {
            put("models", org.json.JSONArray(modelFallbacks))
            put("messages", messages)
        }'''
assert old_body in content, "OLD BODY NOT FOUND"
content = content.replace(old_body, new_body, 1)

with open(path, "w", encoding="utf-8") as f:
    f.write(content)

print("DONE")
